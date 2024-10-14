<script lang="ts">
    import { onMount } from "svelte";
    import { toast } from "svelte-sonner";
    import type { ActionData, PageData } from "./$types.js";
    import LoginForm from "$lib/components/ui/loginForm/login-form.svelte";
    import * as Card from "$lib/components/ui/card";

    export let data: PageData;
    export let form: ActionData;

    let error: string | undefined;
    $: error = typeof form === "object" && form && 'error' in form ? (form as any).error : "";

    $: if (error) {
        toast.error(error);
    }

    onMount(() => {
        const { message = "" } = data.toastData;

        if (message !== "") {
            toast.success(message);
        }
    });
</script>

<div class="flex items-center justify-center h-screen">
    <Card.Root class="w-full max-w-lg">
        <Card.Header>
            <Card.Title>Login</Card.Title>
            <Card.Description>Enter your credentials to log in</Card.Description>
        </Card.Header>
        <Card.Content>
            <LoginForm data={data.form}></LoginForm>
        </Card.Content>
        <Card.Footer class="flex justify-end">
            <p class="text-sm">Don't have an account? <a href="/register" class="underline">Register here</a></p>
        </Card.Footer>
    </Card.Root>
</div>
