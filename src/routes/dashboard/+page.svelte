<script lang="ts">
  import AdminDashboard from "$lib/components/ui/dashboard/admin/admin-dashboard.svelte";
  import { onMount } from "svelte";
  import { toast } from "svelte-sonner";
  import type { PageData } from "./$types"; 
  
  export let data: PageData;

  let { clients, instructors, bookings, lessons, locations, currentTab, lessonForm } = data;

  const { user } = data;

  onMount(() => {
      let message = "";
      
      if (data.toastData) { ({ message } = data.toastData) };

      console.log(data.toastData);

      if (message != "") {
          toast.success(message);
      }
  });

  </script>
  
  {#if user.role === "ADMINISTRATOR"}
  <AdminDashboard {clients} {instructors} {bookings} {lessons} {locations} {user} {currentTab} {lessonForm}></AdminDashboard>
  {/if}