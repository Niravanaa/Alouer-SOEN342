import { z } from "zod";
import { LessonType } from "@prisma/client";

const lessonTypeEnum = z.enum([LessonType.GROUP, LessonType.PRIVATE]);

export const lessonSchema = z
  .object({
    type: lessonTypeEnum,
    title: z.string().min(5).max(100),
    startTime: z.string().refine((val) => !isNaN(Date.parse(val)), {
      message: "Invalid start time",
    }),
    endTime: z.string().refine((val) => !isNaN(Date.parse(val)), {
      message: "Invalid end time",
    }),
  })
  // Ensure endTime is after startTime
  .refine((data) => new Date(data.endTime) > new Date(data.startTime), {
    message: "End time must be after start time",
    path: ["endTime"],
  });

export type LessonSchema = typeof lessonSchema;
