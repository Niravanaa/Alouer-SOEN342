import { PrismaClient, Role } from "@prisma/client";
import { hash } from "bcrypt";
import crypto from "crypto";

const prisma = new PrismaClient();

async function main() {
  const saltRounds = 10;

  const users = [
    {
      email: "instructor@example.com",
      password: "instructor123",
      role: Role.INSTRUCTOR,
    },
    {
      email: "client@example.com",
      password: "client123",
      role: Role.CLIENT,
    },
    {
      email: "admin@example.com",
      password: "admin123",
      role: Role.ADMINISTRATOR,
    },
  ];

  for (const user of users) {
    const hashedPassword = await hash(user.password, saltRounds);
    const userId = crypto.randomUUID();

    const createdUser = await prisma.user.upsert({
      where: { email: user.email },
      update: {},
      create: {
        id: userId,
        email: user.email,
        role: user.role,
      },
    });

    await prisma.key.create({
      data: {
        id: crypto.randomUUID(),
        hashed_password: hashedPassword,
        user_id: createdUser.id,
      },
    });

    console.log(
      `Created user with email: ${createdUser.email} and role: ${createdUser.role}`,
    );
  }
}

main()
  .catch((e) => {
    console.error(e);
    process.exit(1);
  })
  .finally(async () => {
    await prisma.$disconnect();
  });
