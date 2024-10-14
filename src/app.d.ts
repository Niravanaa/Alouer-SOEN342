declare global {
  namespace App {
    interface Locals {
      user: import("$lib/server/prisma").User | null;
    }
  }
}

export {};
