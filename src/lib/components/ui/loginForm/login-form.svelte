<script lang="ts">
    import * as Form from "$lib/components/ui/form";
    import { Input } from "$lib/components/ui/input";
    import {
        type SuperValidated,
        type Infer,
        superForm,
    } from "sveltekit-superforms";
    import { zodClient } from "sveltekit-superforms/adapters";
    import { loginFormSchema, type LoginFormSchema } from "./schema";

    export let data: SuperValidated<Infer<LoginFormSchema>>;
 
    const form = superForm(data, {
    validators: zodClient(loginFormSchema),
        onError({ result }) {
            $message = result.error.message || "Unknown error";
        }
    });

    const { form: formData, enhance, message } = form;
</script>
    
<form method="POST" use:enhance>
    <Form.Field {form} name="email">
        <Form.Control let:attrs>
        <Form.Label>Email</Form.Label>
        <Input {...attrs} bind:value={$formData.email} type="email" />
        </Form.Control>
        <Form.Description>Your email address.</Form.Description>
        <Form.FieldErrors />
    </Form.Field>

    <Form.Field {form} name="password">
        <Form.Control let:attrs>
        <Form.Label>Password</Form.Label>
        <Input {...attrs} bind:value={$formData.password} type="password" />
        </Form.Control>
        <Form.Description>Your password (min. 8 characters).</Form.Description>
        <Form.FieldErrors />
    </Form.Field>

      {#if $message}<span class="invalid">{$message}</span>{/if}
    <div class="flex justify-center mt-4">
        <Form.Button>Login</Form.Button>
    </div>
</form>
