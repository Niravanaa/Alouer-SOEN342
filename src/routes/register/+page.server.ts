import type { PageServerLoad, Actions } from "./$types.js";
import { fail, redirect } from "@sveltejs/kit";
import { superValidate } from "sveltekit-superforms";
import { zod } from "sveltekit-superforms/adapters";

import { clientRegisterSchema } from "$lib/components/ui/registerForm/client/schema";
import { instructorRegisterSchema } from "$lib/components/ui/registerForm/instructor/schema";
import { createUser } from "$lib/server/models/user.model";

import { toastStore } from "$lib/stores/toastStore";

export const load: PageServerLoad = async () => {
  return {
    form: await superValidate(zod(clientRegisterSchema)),
    error: "",
  };
};

export const actions: Actions = {
  default: async (event) => {
    const formData = await event.request.formData();
    const registrationType = formData.get("isInstructor"); // Get the registration type
    console.log(registrationType);
    let form;
    // Check whether the user is registering as a client or an instructor
    if (registrationType === "true") {
      // Instructor registration
      form = await superValidate(formData, zod(instructorRegisterSchema));
      if (!form.valid) {
        return fail(400, {
          form,
          error: "Form not valid. Try again.",
        });
      }

      // Process instructor registration
      const {
        firstName = "",
        lastName = "",
        email = "",
        password = "",
        phone = "",
      } = form.data;

      const result = await createUser(
        firstName,
        lastName,
        email,
        password,
        phone,
        "INSTRUCTOR",
      );
      if (result.error) {
        return fail(500, {
          form,
          error: result.error,
        });
      }
    } else {
      // Client registration
      form = await superValidate(formData, zod(clientRegisterSchema));
      if (!form.valid) {
        return fail(400, {
          form,
          error: "Form not valid. Try again.",
        });
      }

      // Process client registration
      const {
        firstName = "",
        lastName = "",
        email = "",
        password = "",
        phone = "",
      } = form.data;

      const result = await createUser(
        firstName,
        lastName,
        email,
        password,
        phone,
        "CLIENT",
      );
      if (result.error) {
        return fail(500, {
          form,
          error: result.error,
        });
      }
    }

    toastStore.set({
      type: "success",
      message: "Registration successful! Please log in.",
    });

    throw redirect(302, "/login");
  },
};
