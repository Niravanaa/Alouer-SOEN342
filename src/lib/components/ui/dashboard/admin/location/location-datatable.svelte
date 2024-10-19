<script lang="ts"?>
    import { readable, writable } from "svelte/store";
    import { createTable, Render, Subscribe, createRender } from "svelte-headless-table";
    import * as Table from "$lib/components/ui/table";
    import type { Lesson, Location } from "@prisma/client";
    import LocationDatatableActions from "./location-datatable-actions.svelte";
    import { Button } from "$lib/components/ui/button";
    import { addPagination, addSortBy, addTableFilter, addHiddenColumns } from "svelte-headless-table/plugins";
    import ArrowUpDown from "lucide-svelte/icons/arrow-up-down";
    import ChevronDown from "lucide-svelte/icons/chevron-down";
    import { Input } from "$lib/components/ui/input";
    import * as DropdownMenu from "$lib/components/ui/dropdown-menu";

    export let locations: Location[] = [];
    export let lessons: Lesson[] = [];
    export let lessonForm;

    const table = createTable(readable(locations), {
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
        },filter: {
          exclude: true,
        },
      },
      }),
      table.column({
          accessor: "name",
          header: "Name",
      }),
      table.column({
          accessor: "address",
          header: "Address",
          plugins: {
            filter: {
          exclude: true,
        },
          }
      }),
      table.column({
          accessor: "city",
          header: "City",
          plugins: {
            filter: {
          exclude: true,
        },
      },
      }),
      table.column({
          accessor: "province",
          header: "Province",
          plugins: {
            filter: {
              exclude: true,
            },
          },
      }),
      table.column({
          accessor: "postalCode",
          header: "Postal Code",
          plugins: {
            filter: {
          exclude: true,
        },
      },
      }),
      table.column({
        accessor: ({ id }) => id,
        header: "",
        cell: ({ value }) => {
          return createRender(LocationDatatableActions, { id: value.toString(), lessons: lessons.filter(lesson => lesson.locationId === value), lessonForm: lessonForm });
        },
      }),
  ]);

  const { headerRows, pageRows, tableAttrs, tableBodyAttrs, pluginStates, flatColumns } = table.createViewModel(columns);
  const { hasNextPage, hasPreviousPage, pageIndex } = pluginStates.page;
  const { filterValue } = pluginStates.filter;
  const { hiddenColumnIds } = pluginStates.hide;

  const ids = flatColumns.map((col) => col.id);
  let hideForId = Object.fromEntries(ids.map((id) => [id, true]));
 
  $: $hiddenColumnIds = Object.entries(hideForId)
    .filter(([, hide]) => !hide)
    .map(([id]) => id);
 
  const hidableCols = ["name", "city", "province"];

</script>

<div>
  <div class="flex items-center py-4">
    <Input
      class="max-w-sm"
      placeholder="Filter location names..."
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
                    {#if ["name", "address", "city", "province"].includes(cell.id)}
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
  <div class="flex items-center justify-end space-x-4 py-4">
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