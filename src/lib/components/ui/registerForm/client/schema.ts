import { z } from "zod";

export const clientRegisterSchema = z.object({
  firstName: z.string().min(2).max(50),
  lastName: z.string().min(2).max(50),
  email: z.string().email(),
  password: z.string().min(8),
  phone: z.string().min(10).max(15),
});

export type ClientRegisterFormSchema = typeof clientRegisterSchema;
