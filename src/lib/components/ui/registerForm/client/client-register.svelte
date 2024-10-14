<script lang="ts">
    import * as Form from "$lib/components/ui/form";
    import { Input } from "$lib/components/ui/input";
    import { clientRegisterSchema, type ClientRegisterFormSchema } from "./schema";
    import {
        type SuperValidated,
        type Infer,
        superForm,
    } from "sveltekit-superforms";
    import { zodClient } from "sveltekit-superforms/adapters";

    export let data: SuperValidated<Infer<ClientRegisterFormSchema>>;

    const form = superForm(data, {
        validators: zodClient(clientRegisterSchema),
    });

    const { form: formData, enhance } = form
</script>
    
<form method="POST" use:enhance>
    <Form.Field {form} name="firstName">
        <Form.Control let:attrs>
        <Form.Label>First Name</Form.Label>
        <Input {...attrs} bind:value={$formData.firstName} />
        </Form.Control>
        <Form.Description>Your first name.</Form.Description>
        <Form.FieldErrors />
    </Form.Field>

    <Form.Field {form} name="lastName">
        <Form.Control let:attrs>
        <Form.Label>Last Name</Form.Label>
        <Input {...attrs} bind:value={$formData.lastName} />
        </Form.Control>
        <Form.Description>Your last name.</Form.Description>
        <Form.FieldErrors />
    </Form.Field>

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
        <Form.Description>Create a strong password (min. 8 characters).</Form.Description>
        <Form.FieldErrors />
    </Form.Field>

    <Form.Field {form} name="phone">
        <Form.Control let:attrs>
        <Form.Label>Phone</Form.Label>
        <Input {...attrs} bind:value={$formData.phone} />
        </Form.Control>
        <Form.Description>Your contact number.</Form.Description>
        <Form.FieldErrors />
    </Form.Field>

    <div class="flex justify-center mt-4">
        <Form.Button>Submit</Form.Button>
    </div>

    <input type="hidden" name="isInstructor" value="false" />
</form>
   