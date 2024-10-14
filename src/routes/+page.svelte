<script lang="ts">
    import { onMount } from "svelte";
    import { toast } from "svelte-sonner";
    import type { PageData } from "./$types";

    export let data: PageData;

    onMount(() => {
        let message = "";

        if (data.toastData) { ({ message } = data.toastData) };

        if (message != "") {
            toast.success(message);
        }
    });
</script>