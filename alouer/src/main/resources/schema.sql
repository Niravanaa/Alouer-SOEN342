DROP TABLE IF EXISTS `instructor_lessons`;
DROP TABLE IF EXISTS `client_children`;
DROP TABLE IF EXISTS `lesson_schedule`;
DROP TABLE IF EXISTS `lesson`;
DROP TABLE IF EXISTS `booking`;
DROP TABLE IF EXISTS `location`;
DROP TABLE IF EXISTS `instructor`;
DROP TABLE IF EXISTS `client`;
DROP TABLE IF EXISTS `child`;
DROP TABLE IF EXISTS `administrator`;

CREATE TABLE IF NOT EXISTS administrator (
    firstName TEXT NOT NULL DEFAULT 'Admin',
    lastName TEXT NOT NULL DEFAULT 'User',
    email TEXT NOT NULL DEFAULT 'admin@example.com',
    password TEXT NOT NULL DEFAULT 'password123'
);

CREATE TABLE IF NOT EXISTS child (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    firstName TEXT NOT NULL,
    lastName TEXT NOT NULL,
    dateOfBirth DATE NOT NULL,
    parentId INTEGER,
    FOREIGN KEY (parentId) REFERENCES client(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS client (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    firstName TEXT NOT NULL,
    lastName TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL,
    role TEXT NOT NULL CHECK (role IN ('CLIENT'))
);

CREATE TABLE IF NOT EXISTS instructor (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    firstName TEXT NOT NULL,
    lastName TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL,
    role TEXT NOT NULL CHECK (role IN ('INSTRUCTOR'))
);

CREATE TABLE IF NOT EXISTS location (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    address TEXT NOT NULL,
    city TEXT NOT NULL,
    province TEXT NOT NULL,
    postalCode TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS booking (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    clientId INTEGER NOT NULL,
    childId INTEGER,
    lessonId INTEGER NOT NULL,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (clientId) REFERENCES client(id) ON DELETE CASCADE,
    FOREIGN KEY (childId) REFERENCES child(id) ON DELETE SET NULL,
    FOREIGN KEY (lessonId) REFERENCES lesson(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS lesson (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    type TEXT NOT NULL CHECK (type IN ('PRIVATE', 'GROUP')),
    title TEXT NOT NULL,
    locationId INTEGER,
    assignedInstructorId INTEGER,
    isAvailable BOOLEAN DEFAULT TRUE,
    startTime TIME NOT NULL,
    endTime TIME NOT NULL,
    bookingId INTEGER,
    FOREIGN KEY (locationId) REFERENCES location(id) ON DELETE SET NULL,
    FOREIGN KEY (assignedInstructorId) REFERENCES instructor(id) ON DELETE SET NULL,
    FOREIGN KEY (bookingId) REFERENCES booking(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS lesson_schedule (
    lessonId INTEGER NOT NULL,
    dayOfWeek TEXT NOT NULL CHECK (dayOfWeek IN ('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY')),  -- DayOfWeek: Enum values
    FOREIGN KEY (lessonId) REFERENCES lesson(id) ON DELETE CASCADE,
    PRIMARY KEY (lessonId, dayOfWeek)
);

CREATE TABLE IF NOT EXISTS client_children (
    clientId INTEGER NOT NULL,
    childId INTEGER NOT NULL,
    FOREIGN KEY (clientId) REFERENCES client(id) ON DELETE CASCADE,
    FOREIGN KEY (childId) REFERENCES child(id) ON DELETE CASCADE,
    PRIMARY KEY (clientId, childId)
);

CREATE TABLE IF NOT EXISTS instructor_lessons (
    instructorId INTEGER NOT NULL,
    lessonId INTEGER NOT NULL,
    FOREIGN KEY (instructorId) REFERENCES instructor(id) ON DELETE CASCADE,
    FOREIGN KEY (lessonId) REFERENCES lesson(id) ON DELETE CASCADE,
    PRIMARY KEY (instructorId, lessonId)
);
