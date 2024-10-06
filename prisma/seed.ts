import { PrismaClient, Role } from "@prisma/client";
import { hash } from "bcrypt";
import crypto from "crypto";
import { parseArgs } from "node:util"; // Node.js built-in utility module

const prisma = new PrismaClient();

async function seedMain() {
  const saltRounds = 10;

  // Main users to be seeded
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

async function testSeed() {
  const saltRounds = 10;

  // Test users to be seeded
  const testUsers = [
    {
      email: "testinstructor@example.com",
      password: "testinstructor123",
      role: Role.INSTRUCTOR,
    },
    {
      email: "testclient@example.com",
      password: "testclient123",
      role: Role.CLIENT,
    },
    {
      email: "testadmin@example.com",
      password: "testadmin123",
      role: Role.ADMINISTRATOR,
    },
  ];

  for (const user of testUsers) {
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
      `Created test user with email: ${createdUser.email} and role: ${createdUser.role}`,
    );
  }

  const users = await prisma.user.findMany();
  const keys = await prisma.key.findMany();

  console.log("Previewing existing users and keys:");
  console.log("Users:", users);
  console.log("Keys:", keys);
}

async function main() {
  const {
    values: { environment },
  } = parseArgs({ options: { environment: { type: "string" } } });

  switch (environment) {
    case "production":
    case "develop":
      await seedMain();
      break;
    case "preview":
      await testSeed();
      break;
    default:
      console.log(
        `Invalid environment "${environment}". Please provide a valid environment.`,
      );
      break;
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
