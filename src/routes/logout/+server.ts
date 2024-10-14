import { toastStore } from "$lib/stores/toastStore";

export const POST = async ({ cookies, locals }): Promise<Response> => {
  // Clear the AuthorizationToken cookie
  cookies.delete("AuthorizationToken", { path: "/" });

  // Clear the user session
  locals.user = null;

  toastStore.set({
    type: "success",
    message: "Logout Successful. See you later!",
  });

  // Return a basic response with a 200 status
  return new Response(null, { status: 200 });
};
