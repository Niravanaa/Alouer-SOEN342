<script lang="ts">
    import Ellipsis from "lucide-svelte/icons/ellipsis";
    import * as DropdownMenu from "$lib/components/ui/dropdown-menu";
    import { Button } from "$lib/components/ui/button";
    import * as Dialog from "$lib/components/ui/dialog";
    import { cn } from "$lib/utils";

    export let email: string;

    const handleDelete = async () => {
        const formData = new FormData();
        formData.append("email", email);

        await fetch("?/deleteUser", {
          method: "POST",
          body: formData,
        });

        window.location.href = "/dashboard";
      };
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
      <Dialog.Root>
        <Dialog.Trigger class={cn(
          "data-[highlighted]:bg-accent data-[highlighted]:text-accent-foreground relative flex cursor-default select-none items-center rounded-sm px-2 py-1.5 text-sm outline-none data-[disabled]:pointer-events-none data-[disabled]:opacity-50"
        )}>Delete</Dialog.Trigger>
        <Dialog.Content>
          <Dialog.Header>
            <Dialog.Title>Are you sure absolutely sure?</Dialog.Title>
            <Dialog.Description>
              This action cannot be undone. This will permanently delete the account
              and associated data from our servers.
            </Dialog.Description>
          </Dialog.Header>
          <Dialog.Footer>
            <Button on:click={handleDelete} variant="destructive">Delete</Button>
          </Dialog.Footer>
        </Dialog.Content>
      </Dialog.Root>
     </DropdownMenu.Group>
    </DropdownMenu.Content>
   </DropdownMenu.Root>