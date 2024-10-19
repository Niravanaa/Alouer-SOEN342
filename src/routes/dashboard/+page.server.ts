import prisma from "$lib/server/prisma";
import { Role } from "@prisma/client";
import type { Actions, PageServerLoad } from "./$types";
import { toastStore } from "$lib/stores/toastStore";
import { get } from "svelte/store";
import { redirect } from "@sveltejs/kit";
import { lessonSchema } from "$lib/components/ui/dashboard/admin/lesson/lessonSchema";
import { superValidate } from "sveltekit-superforms";
import { zod } from "sveltekit-superforms/adapters";

export const load: PageServerLoad = async ({ locals, url }) => {
  const toastData = get(toastStore);

  const currentTab = url.searchParams.get("tab");

  toastStore.set(null);

  return {
    clients: await prisma.user.findMany({
      where: {
        role: {
          equals: "CLIENT",
        },
      },
    }),
    instructors: await prisma.user.findMany({
      where: { role: { equals: "INSTRUCTOR" } },
    }),
    bookings: await prisma.booking.findMany(),
    locations: await prisma.location.findMany(),
    lessons: await prisma.lesson.findMany(),
    user: locals.user,
    toastData: toastData,
    currentTab: currentTab,
    lessonForm: await superValidate(zod(lessonSchema)),
  };
};

export const actions: Actions = {
  deleteUser: async ({ request, locals }) => {
    const formData = await request.formData();
    const email = formData.get("email")?.toString();

    if (locals.user.role != Role.ADMINISTRATOR) {
      return { error: "You are not authorized to delete a user." };
    }

    if (!email) {
      return { error: "Email is required to delete a user." };
    }

    try {
      await prisma.user.delete({
        where: {
          email: email,
        },
      });

      toastStore.set({
        type: "success",
        message: `User with email ${email} has been deleted.`,
      });
    } catch {
      toastStore.set({
        type: "error",
        message: `Failed to delete user.`,
      });
    }

    throw redirect(302, "/login");
  },
  addLesson: async ({ request, locals }) => {
    if (locals.user.role != Role.ADMINISTRATOR) {
      return { error: "You are not authorized to add a lesson." };
    }

    const formData = await request.formData();
    const lessonValidation = await superValidate(formData, zod(lessonSchema));

    if (!lessonValidation.valid) {
      return { error: "Invalid lesson data", lessonForm: lessonValidation };
    }

    try {
      const { type, title, startTime, endTime } = lessonValidation.data;

      await prisma.lesson.create({
        data: {
          type,
          title,
          startTime: new Date(startTime),
          endTime: new Date(endTime),
          locationId: Number(formData.get("locationId")),
          isAvailable: true,
        },
      });

      toastStore.set({
        type: "success",
        message: `Lesson "${title}" has been added successfully.`,
      });
    } catch (error) {
      toastStore.set({
        type: "error",
        message: `Failed to add lesson: ${error}`,
      });
    }

    throw redirect(302, "/dashboard");
  },

  editLesson: async ({ request, locals }) => {
    if (locals.user.role != Role.ADMINISTRATOR) {
      return { error: "You are not authorized to edit a lesson." };
    }

    const formData = await request.formData();
    const lessonValidation = await superValidate(formData, zod(lessonSchema));

    if (!lessonValidation.valid) {
      return { error: "Invalid lesson data", lessonForm: lessonValidation };
    }

    const lessonId = Number(formData.get("lessonId"));

    try {
      const { type, title, startTime, endTime } = lessonValidation.data;

      await prisma.lesson.update({
        where: { id: lessonId },
        data: {
          type,
          title,
          startTime: new Date(startTime),
          endTime: new Date(endTime),
          locationId: Number(formData.get("locationId")),
          isAvailable: formData.get("isAvailable") === "true",
        },
      });

      toastStore.set({
        type: "success",
        message: `Lesson "${title}" has been updated successfully.`,
      });
    } catch (error) {
      toastStore.set({
        type: "error",
        message: `Failed to update lesson: ${error}`,
      });
    }

    throw redirect(302, "/dashboard?tab=lessons");
  },
};
