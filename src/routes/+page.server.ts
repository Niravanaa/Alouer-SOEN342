import { get } from "svelte/store";
import { toastStore } from "$lib/stores/toastStore";
import type { PageServerLoad } from "./$types";

export const load: PageServerLoad = async () => {
  const toastData = get(toastStore);

  toastStore.set(null);

  return {
    toastData: toastData,
  };
};
