<script lang="ts">
    import type { PageData } from "./$types.js";
    import ClientRegisterForm from "$lib/components/ui/registerForm/client/client-register.svelte";
    import InstructorRegisterForm from "$lib/components/ui/registerForm/instructor/instructor-register.svelte";
    import * as Card from "$lib/components/ui/card";
    export let data: PageData;

    let isInstructor = false;
</script>

<div class="flex items-center justify-center h-screen">
    <Card.Root class="w-full max-w-lg">
        <Card.Header>
            <Card.Title>Register</Card.Title>
            <Card.Description>Fill out the form below to register</Card.Description>
        </Card.Header>
        <Card.Content>
            <div class="flex justify-center mb-4">
                <label>
                    <input type="radio" bind:group={isInstructor} value={false} class="mr-2" />
                    Register as Client
                </label>
                <label class="ml-4">
                    <input type="radio" bind:group={isInstructor} value={true} class="mr-2" />
                    Register as Instructor
                </label>
            </div>
            {#if isInstructor}
                <InstructorRegisterForm data={data.form} />
            {:else}
                <ClientRegisterForm data={data.form} />
            {/if}
        </Card.Content>
        <Card.Footer class="flex justify-end">
            {#if data.error != undefined}
                <div class="notice error">
                    {data.error}
                </div>
            {/if}
            <p class="text-sm">Already have an account? <a href="/login" class="underline">Log in</a></p>
        </Card.Footer>
    </Card.Root>
</div>
