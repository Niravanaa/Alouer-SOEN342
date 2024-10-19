import { Lesson, LessonType, PrismaClient } from "@prisma/client";
import { hash } from "bcrypt";
import { parseArgs } from "node:util";

type User = {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  phone: string;
  role: "CLIENT" | "INSTRUCTOR" | "ADMINISTRATOR";
};

type Location = {
  name: string;
  address: string;
  city: string;
  province: string;
  postalCode: string;
};

const prisma = new PrismaClient();

async function seedUsers(users: User[]) {
  const saltRounds = 10;

  const createdUsers: User[] = [];

  for (const user of users) {
    const hashedPassword = await hash(user.password, saltRounds);

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
      },
    });

    console.log(`Created user with email: ${createdUser.email}`);
    createdUsers.push(createdUser);
  }

  return createdUsers;
}

async function seedLocations(locations: Location[]) {
  const createdLocations: Location[] = [];

  for (const location of locations) {
    const createdLocation = await prisma.location.create({
      data: {
        name: location.name,
        address: location.address,
        city: location.city,
        province: location.province,
        postalCode: location.postalCode,
      },
    });

    console.log(`Created location: ${createdLocation.name}`);
    createdLocations.push(createdLocation);
  }

  return createdLocations;
}

async function seedLessons(locations, users) {
  const lessons = [
    {
      title: "Swimming Lesson",
      type: "GROUP",
      instructorEmail: "instructor@example.com",
      locationName: "Gym",
      startTime: new Date(),
      endTime: new Date(),
      isAvailable: true,
    },
  ];

  const createdLessons: Lesson[] = [];

  for (const lesson of lessons) {
    const instructor = users.find((u) => u.email === lesson.instructorEmail);
    const location = locations.find((l) => l.name === lesson.locationName);

    const createdLesson = await prisma.lesson.create({
      data: {
        title: lesson.title,
        type: LessonType.GROUP,
        isAvailable: lesson.isAvailable,
        instructor: { connect: { id: instructor.id } },
        location: { connect: { id: location.id } },
        startTime: lesson.startTime,
        endTime: lesson.endTime,
      },
    });

    console.log(`Created lesson: ${createdLesson.title}`);
    createdLessons.push(createdLesson);
  }

  return createdLessons;
}

async function seedBookings(users, lessons) {
  const bookings = [
    {
      clientEmail: "client@example.com",
      lessonTitle: "Swimming Lesson",
    },
  ];

  for (const booking of bookings) {
    const client = users.find((u) => u.email === booking.clientEmail);
    const lesson = lessons.find((l) => l.title === booking.lessonTitle);

    await prisma.booking.create({
      data: {
        client: { connect: { id: client.id } },
        lesson: { connect: { id: lesson.id } },
      },
    });

    console.log(`Created booking for client: ${client.email}`);
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

  const locations: Location[] = [
    {
      name: "Gym",
      address: "123 Fitness St",
      city: "Metropolis",
      province: "MT",
      postalCode: "12345",
    },
  ];

  const createdUsers = await seedUsers(users);
  const createdLocations = await seedLocations(locations);
  const createdLessons = await seedLessons(createdLocations, createdUsers);
  await seedBookings(createdUsers, createdLessons);
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
