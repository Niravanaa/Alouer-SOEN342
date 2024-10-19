import { JWT_ACCESS_SECRET } from "$env/static/private";
import bcrypt from "bcryptjs";
import jwt from "jsonwebtoken";
import prisma from "$lib/server/prisma";

const createUser = async (
  firstName: string,
  lastName: string,
  email: string,
  password: string,
  phone: string,
  role: "CLIENT" | "INSTRUCTOR",
) => {
  // Check if user exists
  const existingUser = await prisma.user.findUnique({
    where: {
      email,
    },
  });

  if (existingUser) {
    return {
      error: "User already exists",
    };
  }

  try {
    const hashedPassword = await bcrypt.hash(password, 10);

    const user = await prisma.user.create({
      data: {
        firstName,
        lastName,
        email,
        password: hashedPassword,
        phone,
        role,
      },
    });

    return { user };
  } catch (error) {
    console.error(error);
    return {
      error: "Something went wrong",
    };
  }
};

const loginUser = async (email: string, password: string) => {
  // Check if user exists
  const user = await prisma.user.findUnique({
    where: {
      email,
    },
  });

  if (!user) {
    return {
      error: "Invalid credentials",
    };
  }

  // Verify the password
  const passwordIsValid = await bcrypt.compare(password, user.password);

  if (!passwordIsValid) {
    return {
      error: "Invalid credentials",
    };
  }

  const jwtUser = {
    id: user.id,
    email: user.email,
    role: user.role,
  };

  const token = jwt.sign(jwtUser, JWT_ACCESS_SECRET, {
    expiresIn: "1d",
  });

  return { token };
};

export { createUser, loginUser };
