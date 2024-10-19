/*
  Warnings:

  - You are about to drop the column `bookingTime` on the `Booking` table. All the data in the column will be lost.
  - You are about to drop the column `isAvailable` on the `Booking` table. All the data in the column will be lost.
  - You are about to drop the column `scheduleId` on the `Lesson` table. All the data in the column will be lost.
  - You are about to drop the column `instructorId` on the `Location` table. All the data in the column will be lost.
  - You are about to drop the column `availability` on the `User` table. All the data in the column will be lost.
  - You are about to drop the column `specialization` on the `User` table. All the data in the column will be lost.
  - You are about to drop the `Schedule` table. If the table is not empty, all the data it contains will be lost.
  - You are about to drop the `TimeSlot` table. If the table is not empty, all the data it contains will be lost.
  - Added the required column `updatedAt` to the `Booking` table without a default value. This is not possible if the table is not empty.
  - Made the column `dateOfBirth` on table `Child` required. This step will fail if there are existing NULL values in that column.
  - Added the required column `updatedAt` to the `Lesson` table without a default value. This is not possible if the table is not empty.
  - Added the required column `postalCode` to the `Location` table without a default value. This is not possible if the table is not empty.

*/
-- DropForeignKey
ALTER TABLE "Lesson" DROP CONSTRAINT "Lesson_scheduleId_fkey";

-- DropForeignKey
ALTER TABLE "Location" DROP CONSTRAINT "Location_instructorId_fkey";

-- DropForeignKey
ALTER TABLE "Schedule" DROP CONSTRAINT "Schedule_locationId_fkey";

-- DropForeignKey
ALTER TABLE "TimeSlot" DROP CONSTRAINT "TimeSlot_scheduleId_fkey";

-- AlterTable
ALTER TABLE "Booking" DROP COLUMN "bookingTime",
DROP COLUMN "isAvailable",
ADD COLUMN     "createdAt" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN     "updatedAt" TIMESTAMP(3) NOT NULL;

-- AlterTable
ALTER TABLE "Child" ALTER COLUMN "dateOfBirth" SET NOT NULL;

-- AlterTable
ALTER TABLE "Lesson" DROP COLUMN "scheduleId",
ADD COLUMN     "createdAt" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN     "isAvailable" BOOLEAN NOT NULL DEFAULT false,
ADD COLUMN     "updatedAt" TIMESTAMP(3) NOT NULL;

-- AlterTable
ALTER TABLE "Location" DROP COLUMN "instructorId",
ADD COLUMN     "postalCode" TEXT NOT NULL;

-- AlterTable
ALTER TABLE "User" DROP COLUMN "availability",
DROP COLUMN "specialization";

-- DropTable
DROP TABLE "Schedule";

-- DropTable
DROP TABLE "TimeSlot";

-- DropEnum
DROP TYPE "DayOfWeek";
