<script lang="ts">
    import * as Form from "$lib/components/ui/form";
    import { Input } from "$lib/components/ui/input";
    import { lessonSchema, type LessonSchema } from "$lib/components/ui/dashboard/admin/lesson/lessonSchema";
    import {
        type SuperValidated,
        type Infer,
        superForm,
    } from "sveltekit-superforms";
    import { zodClient } from "sveltekit-superforms/adapters";
    
    export let lessonForm: SuperValidated<Infer<LessonSchema>>;
    export let isEditMode: boolean;
    export let lessonId: number | undefined;
    export let locationId: number; 
    export let showLessons: boolean;

    const form = superForm(lessonForm, {
        validators: zodClient(lessonSchema),
    });

    const { form: formData, enhance } = form;

    const actionUrl = isEditMode
        ? `?/editLesson`
        : "?/addLesson";

    function handleSubmit() {
        showLessons = false;
    }
</script>

<form method="POST" use:enhance action={actionUrl} on:submit={handleSubmit}>
    <input type="hidden" name="locationId" value={locationId} />
    
    <!-- Type Field -->
    <Form.Field {form} name="type">
    <Form.Control let:attrs>
        <Form.Label>Lesson Type</Form.Label>
        <select {...attrs} bind:value={$formData.type}>
        <option value="GROUP">Group</option>
        <option value="PRIVATE">Private</option>
        </select>
    </Form.Control>
    <Form.Description>Select the type of lesson.</Form.Description>
    <Form.FieldErrors />
    </Form.Field>

    <!-- Title Field -->
    <Form.Field {form} name="title">
    <Form.Control let:attrs>
        <Form.Label>Title</Form.Label>
        <Input {...attrs} bind:value={$formData.title} />
    </Form.Control>
    <Form.Description>Enter a title for the lesson.</Form.Description>
    <Form.FieldErrors />
    </Form.Field>

    <!-- Start Time Field -->
    <Form.Field {form} name="startTime">
    <Form.Control let:attrs>
        <Form.Label>Start Time</Form.Label>
        <Input type="datetime-local" {...attrs} bind:value={$formData.startTime} />
    </Form.Control>
    <Form.Description>Select the start time for the lesson.</Form.Description>
    <Form.FieldErrors />
    </Form.Field>

    <!-- End Time Field -->
    <Form.Field {form} name="endTime">
    <Form.Control let:attrs>
        <Form.Label>End Time</Form.Label>
        <Input type="datetime-local" {...attrs} bind:value={$formData.endTime} />
    </Form.Control>
    <Form.Description>Select the end time for the lesson.</Form.Description>
    <Form.FieldErrors />
    </Form.Field>

    <Form.Button>
        {#if isEditMode}
            Save Changes
        {/if}
        {#if !isEditMode}
            Add Lesson
        {/if}
    </Form.Button>
</form>
