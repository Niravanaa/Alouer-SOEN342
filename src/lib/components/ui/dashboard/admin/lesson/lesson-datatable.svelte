<script lang="ts">
    import { readable } from "svelte/store";
    import { createTable, Render, Subscribe, createRender } from "svelte-headless-table";
    import * as Table from "$lib/components/ui/table";
    import type { Lesson } from "@prisma/client";
    import { Button } from "$lib/components/ui/button";
    import { addPagination, addSortBy, addTableFilter, addHiddenColumns} from "svelte-headless-table/plugins";
    import ArrowUpDown from "lucide-svelte/icons/arrow-up-down";
    import ChevronDown from "lucide-svelte/icons/chevron-down";
    import { Input } from "$lib/components/ui/input";
    import * as DropdownMenu from "$lib/components/ui/dropdown-menu";
    import * as Dialog from "$lib/components/ui/dialog";
    import LessonDatatableActions from "./lesson-datatable-actions.svelte";
    import LessonForm from "./lesson-form.svelte";

    export let lessons: Lesson[] = [];
    export let lessonForm;
    export let showLessons: boolean;

    let displayAddLessonForm: boolean = false;
    const locationId = lessons[0]['locationId'];

    function getFriendlyDateTime(date: Date) {
      const daysOfWeek = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
      const monthsOfYear = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];

      let day = daysOfWeek[date.getDay()];
      let month = monthsOfYear[date.getMonth()];
      let dayOfMonth = date.getDate();
      let year = date.getFullYear();
      let hours = date.getHours();
      let minutes: string | number = date.getMinutes();
      
      // Pad minutes with leading zero if necessary
      minutes = minutes < 10 ? '0' + minutes : minutes;
      
      // Format hours for 12-hour time format
      const ampm = hours >= 12 ? 'PM' : 'AM';
      hours = hours % 12 || 12; // Convert 24-hour to 12-hour format

      return `${day}, ${month} ${dayOfMonth}, ${year} at ${hours}:${minutes} ${ampm}`;
  }


    const table = createTable(readable(lessons), {
      page: addPagination(),
      sort: addSortBy(),
      filter: addTableFilter({
      fn: ({ filterValue, value }) =>
          value.toLowerCase().includes(filterValue.toLowerCase()),
      }),hide: addHiddenColumns(),
    });

  const columns = table.createColumns([
      table.column({
          accessor: "id",
          header: "ID",
          plugins: {
        sort: {
          disable: true,
        },
      },
      }),
      table.column({
          accessor: "title",
          header: "Title",
          plugins: {
        sort: {
          disable: true,
        },
      },
      }),
      table.column({
          accessor: "type",
          header: "Type",
      }),
      table.column({
          accessor: "isAvailable",
          header: "Assigned to Instructor?",
          plugins: {
            filter: {
          exclude: true,
        },
          }
      }),
      table.column({
          accessor: "instructorId",
          header: "Instructor ID",
          plugins: {
            filter: {
          exclude: true,
        },
      },
      }),
      table.column({
          accessor: "startTime",
          header: "Start Time",
          cell: ({ value }) => {
            return getFriendlyDateTime(value);
          },
          plugins: {
        sort: {
          disable: true,
        },filter: {
          exclude: true,
        },
      },
      }),
      table.column({
          accessor: "endTime",
          header: "End Time",
          cell: ({ value }) => {
            return getFriendlyDateTime(value);
          },
          plugins: {
        sort: {
          disable: true,
        },filter: {
          exclude: true,
        },
      },
      }),
      table.column({
          accessor: "createdAt",
          header: "Date Created",
          cell: ({ value }) => {
              const date = new Date(value);
              const formattedDate = `${date.getMonth() + 1}/${date.getDate()}/${date.getFullYear()}`;
              return formattedDate;
          },
      }),
      table.column({
        accessor: ({ id }) => id,
        header: "",
        cell: ({ value }) => {
          return createRender(LessonDatatableActions, { lesson: lessons.find((lesson) => lesson.id === value) });
        },
      }),
  ]);

  const { headerRows, pageRows, tableAttrs, tableBodyAttrs, pluginStates, flatColumns } = table.createViewModel(columns);
  const { hasNextPage, hasPreviousPage, pageIndex, pageSize } = pluginStates.page;
  pageSize.set(5);
  const { filterValue } = pluginStates.filter;
  const { hiddenColumnIds } = pluginStates.hide;

  const ids = flatColumns.map((col) => col.id);
  let hideForId = Object.fromEntries(ids.map((id) => [id, true]));
 
  $: $hiddenColumnIds = Object.entries(hideForId)
    .filter(([, hide]) => !hide)
    .map(([id]) => id);
 
  const hidableCols = ["type", "isAvailable", "startTime", "endTime"];

</script>

<div>
  <div class="flex items-center py-4">
    <Input
      class="max-w-sm"
      placeholder="Filter titles..."
      type="text"
      bind:value={$filterValue}
    />
    <DropdownMenu.Root>
      <DropdownMenu.Trigger asChild let:builder>
        <Button variant="outline" class="ml-auto" builders={[builder]}>
          Columns <ChevronDown class="ml-2 h-4 w-4" />
        </Button>
      </DropdownMenu.Trigger>
      <DropdownMenu.Content>
        {#each flatColumns as col}
          {#if hidableCols.includes(col.id)}
            <DropdownMenu.CheckboxItem bind:checked={hideForId[col.id]}>
              {col.header}
            </DropdownMenu.CheckboxItem>
          {/if}
        {/each}
      </DropdownMenu.Content>
    </DropdownMenu.Root>
  </div>
<div class="rounded-md border">
    <Table.Root {...$tableAttrs}>
      <Table.Header>
        {#each $headerRows as headerRow}
          <Subscribe rowAttrs={headerRow.attrs()}>
            <Table.Row>
              {#each headerRow.cells as cell (cell.id)}
                <Subscribe attrs={cell.attrs()} let:attrs props={cell.props()} let:props>
                  <Table.Head {...attrs}>
                    {#if ["type", "instructorId", "locationId"].includes(cell.id)}
                  <Button variant="ghost" on:click={props.sort.toggle}>
                    <Render of={cell.render()} />
                    <ArrowUpDown class={"ml-2 h-4 w-4"} />
                  </Button>
                {:else}
                    <Render of={cell.render()} />
                  {/if}
                  </Table.Head>
                </Subscribe>
              {/each}
            </Table.Row>
          </Subscribe>
        {/each}
      </Table.Header>
      <Table.Body {...$tableBodyAttrs}>
        {#each $pageRows as row (row.id)}
          <Subscribe rowAttrs={row.attrs()} let:rowAttrs>
            <Table.Row {...rowAttrs}>
              {#each row.cells as cell (cell.id)}
                <Subscribe attrs={cell.attrs()} let:attrs>
                  <Table.Cell {...attrs}>
                    <Render of={cell.render()} />
                  </Table.Cell>
                </Subscribe>
              {/each}
            </Table.Row>
          </Subscribe>
        {/each}
      </Table.Body>
    </Table.Root>
  </div>
  <div class="flex items-center justify-between space-x-4 py-4">
    <Button variant="outline" size="sm" class="justify-left" on:click={() => { displayAddLessonForm = true; }}>
      Add Lesson
    </Button>
    <div class="justify-end">
      <Button
      variant="outline"
      size="sm"
      on:click={() => ($pageIndex = $pageIndex - 1)}
      disabled={!$hasPreviousPage}>Previous</Button
    >
    <Button
      variant="outline"
      size="sm"
      disabled={!$hasNextPage}
      on:click={() => ($pageIndex = $pageIndex + 1)}>Next</Button
    >
    </div>
  </div>
</div>

<Dialog.Root bind:open={displayAddLessonForm}>
  <Dialog.Content>
    <LessonForm isEditMode={false} lessonId={undefined} locationId={locationId} {lessonForm} bind:showLessons={showLessons}/>
  </Dialog.Content>
</Dialog.Root>