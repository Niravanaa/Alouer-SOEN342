import { PrismaClient } from "@prisma/client";
import { hash } from "bcrypt";
import { parseArgs } from "node:util";

type User = {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  phone: string;
  role: "CLIENT" | "INSTRUCTOR" | "ADMINISTRATOR";
  specialization?: string[];
  availability?: string[];
};

const prisma = new PrismaClient();

async function seedUsers(users: User[]) {
  const saltRounds = 10;

  for (const user of users) {
    const hashedPassword = await hash(user.password, saltRounds);

    // Create or update user
    const createdUser = await prisma.user.upsert({
      where: { email: user.email },
      update: {}, // No updates for existing users
      create: {
        firstName: user.firstName,
        lastName: user.lastName,
        email: user.email,
        phone: user.phone,
        password: hashedPassword,
        role: user.role,
        specialization: user.specialization || [],
        availability: user.availability || [],
      },
    });

    console.log(`Created user with email: ${createdUser.email}`);
  }
}

async function seedMain() {
  // Main users to be seeded
  const users: User[] = [
    {
      firstName: "Instructor",
      lastName: "User",
      email: "instructor@example.com",
      password: "instructor123",
      phone: "123-456-7890",
      role: "INSTRUCTOR",
      specialization: ["Swimming", "Gymnastics"],
      availability: ["MONDAY", "WEDNESDAY"],
    },
    {
      firstName: "Client",
      lastName: "User",
      email: "client@example.com",
      password: "client123",
      phone: "123-456-7890",
      role: "CLIENT",
    },
    {
      firstName: "Admin",
      lastName: "User",
      email: "admin@example.com",
      password: "admin123",
      phone: "",
      role: "ADMINISTRATOR",
    },
  ];

  await seedUsers(users);
}

async function testSeed() {
  // Test users to be seeded
  const testUsers: User[] = [
    {
      firstName: "TestInstructor",
      lastName: "User",
      email: "testinstructor@example.com",
      password: "testinstructor123",
      phone: "123-456-7890",
      role: "INSTRUCTOR",
      specialization: ["Yoga", "Pilates"],
      availability: ["TUESDAY", "THURSDAY"],
    },
    {
      firstName: "TestClient",
      lastName: "User",
      email: "testclient@example.com",
      password: "testclient123",
      phone: "123-456-7890",
      role: "CLIENT",
    },
    {
      firstName: "TestAdmin",
      lastName: "User",
      email: "testadmin@example.com",
      password: "testadmin123",
      phone: "",
      role: "ADMINISTRATOR",
    },
  ];

  await seedUsers(testUsers);

  const clients = await prisma.user.findMany({ where: { role: "CLIENT" } });
  const instructors = await prisma.user.findMany({
    where: { role: "INSTRUCTOR" },
  });
  const admins = await prisma.user.findMany({
    where: { role: "ADMINISTRATOR" },
  });

  console.log("Previewing existing users:");
  console.log("Clients:", clients);
  console.log("Instructors:", instructors);
  console.log("Admins:", admins);
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
      await testSeed();
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
