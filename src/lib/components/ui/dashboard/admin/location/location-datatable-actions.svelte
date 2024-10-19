<script lang="ts">
    import Ellipsis from "lucide-svelte/icons/ellipsis";
    import * as DropdownMenu from "$lib/components/ui/dropdown-menu";
    import { Button } from "$lib/components/ui/button";
    import * as Dialog from "$lib/components/ui/dialog";
    import type { Lesson } from "@prisma/client";
    import LessonDatatable from "$lib/components/ui/dashboard/admin/lesson/lesson-datatable.svelte";

    export let id: string;
    export let lessons: Lesson[];
    export let lessonForm;

    let showLessons: boolean = false;
    let showDeleteDialog: boolean = false;
   </script>
    
   <DropdownMenu.Root>
    <DropdownMenu.Trigger asChild let:builder>
     <Button
      variant="ghost"
      builders={[builder]}
      size="icon"
      class="relative h-8 w-8 p-0"
     >
      <span class="sr-only">Open menu</span>
      <Ellipsis class="h-4 w-4" />
     </Button>
    </DropdownMenu.Trigger>
    <DropdownMenu.Content>
     <DropdownMenu.Group>
      <DropdownMenu.Label>Actions</DropdownMenu.Label>
      <DropdownMenu.Item>
       Edit
      </DropdownMenu.Item>
      <DropdownMenu.Item on:click={() => { showLessons = true }}>
        View Lessons
      </DropdownMenu.Item>
      <DropdownMenu.Item on:click={() => { showDeleteDialog = true }}>
        Delete
      </DropdownMenu.Item>
     </DropdownMenu.Group>
    </DropdownMenu.Content>
   </DropdownMenu.Root>


   <Dialog.Root bind:open={showLessons}>
    <Dialog.Content class="flex justify-center items-center w-11/12 max-w-5xl p-4">
      <div class="p-2 max-w-full overflow-auto">
        <LessonDatatable {lessons} {lessonForm} bind:showLessons={showLessons} ></LessonDatatable>
      </div>
    </Dialog.Content>
  </Dialog.Root>
  
  

  <Dialog.Root bind:open={showDeleteDialog}>
    <Dialog.Content asChild={false}>
      <Dialog.Header>
        <Dialog.Title>Are you sure absolutely sure?</Dialog.Title>
        <Dialog.Description>
          This action cannot be undone. This will permanently delete the account
          and associated data from our servers.
        </Dialog.Description>
      </Dialog.Header>
      <Dialog.Footer>
        <Button variant="destructive">Delete</Button>
      </Dialog.Footer>
    </Dialog.Content>
  </Dialog.Root>

  