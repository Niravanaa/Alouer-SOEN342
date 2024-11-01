INSERT OR IGNORE INTO administrator (firstName, lastName, email, password, isConnected) VALUES
('Admin', 'User', 'admin@example.com', 'password123', FALSE);

INSERT OR IGNORE INTO client (firstName, lastName, email, password, role, isConnected) VALUES
('John', 'Doe', 'john.doe@example.com', 'john123', 'CLIENT', FALSE),
('Jane', 'Smith', 'jane.smith@example.com', 'jane123', 'CLIENT', FALSE),
('Alice', 'Johnson', 'alice.johnson@example.com', 'alice123', 'CLIENT', FALSE);

INSERT OR IGNORE INTO instructor (firstName, lastName, email, password, role, isConnected) VALUES
('Bob', 'Brown', 'bob.brown@example.com', 'bob123', 'INSTRUCTOR', FALSE),
('Sara', 'Davis', 'sara.davis@example.com', 'sara123', 'INSTRUCTOR', FALSE);

INSERT OR IGNORE INTO location (name, address, city, province, postalCode) VALUES
('Community Center', '123 Main St', 'Townsville', 'TS', 'A1B2C3'),
('Learning Academy', '456 Elm St', 'Townsville', 'TS', 'A4B5C6');

INSERT OR IGNORE INTO child (firstName, lastName, dateOfBirth, parentId) VALUES
('Mike', 'Doe', '2015-05-10', 1),
('Emily', 'Doe', '2017-03-15', 1),
('Liam', 'Smith', '2016-08-20', 2);

INSERT OR IGNORE INTO lesson (type, title, locationId, assignedInstructorId, isAvailable, startTime, endTime) VALUES
('PRIVATE', 'Basic Mathematics', 1, 1, TRUE, '10:00:00', '11:00:00'),
('PRIVATE', 'Introduction to Biology', 1, 2, TRUE, '11:00:00', '12:00:00'),
('PRIVATE', 'Creative Arts', 2, 1, TRUE, '13:00:00', '14:00:00');

INSERT OR IGNORE INTO booking (clientId, childId, lessonId) VALUES
(1, 1, 1),
(1, 2, 2),
(2, 3, 1);

INSERT OR IGNORE INTO lesson_schedule (lessonId, dayOfWeek) VALUES
(1, 'MONDAY'),
(1, 'WEDNESDAY'),
(2, 'TUESDAY'),
(2, 'THURSDAY'),
(3, 'FRIDAY');

INSERT OR IGNORE INTO client_children (clientId, childId) VALUES
(1, 1),
(1, 2),
(2, 3);

INSERT OR IGNORE INTO instructor_lessons (instructorId, lessonId) VALUES
(1, 1),
(2, 2),
(1, 3);
