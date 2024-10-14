import { get } from "svelte/store";
import { toastStore } from "$lib/stores/toastStore";
import type { Actions, PageServerLoad } from "./$types";
import { superValidate } from "sveltekit-superforms";
import { zod } from "sveltekit-superforms/adapters";
import { loginFormSchema } from "$lib/components/ui/loginForm/schema";
import { fail, redirect } from "@sveltejs/kit";
import { loginUser } from "$lib/server/models/user.model";

export const load: PageServerLoad = async () => {
  const toastData = get(toastStore) || { type: "", message: "" };

  toastStore.set(null);

  return {
    toastData: toastData,
    form: await superValidate(zod(loginFormSchema)),
    error: "",
  };
};

export const actions: Actions = {
  default: async (event) => {
    const formData = await event.request.formData();

    const form = await superValidate(formData, zod(loginFormSchema));
    if (!form.valid) {
      return fail(400, {
        form,
      });
    }

    const { email = "", password = "" } = form.data;

    const { error, token } = await loginUser(email, password);

    if (error) {
      return fail(500, {
        form,
        error: error,
      });
    }

    event.cookies.set("AuthorizationToken", `Bearer ${token}`, {
      httpOnly: true,
      path: "/",
      secure: true,
      sameSite: "strict",
      maxAge: 60 * 60 * 24, // 1 day
    });

    toastStore.set({
      type: "success",
      message: "Login successful! Enjoy your stay.",
    });

    throw redirect(302, "/");
  },
};
