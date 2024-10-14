import { writable } from "svelte/store";

export const toastStore = writable<{ type: string; message: string } | null>(
  null,
);
