INSERT OR IGNORE INTO administrator (firstName, lastName, email, password) VALUES
('Admin', 'User', 'admin@example.com', 'password123');

INSERT OR IGNORE INTO client (firstName, lastName, email, password, phoneNumber, role) VALUES
('John', 'Doe', 'john.doe@example.com', 'john123', '514-555-1234', 'CLIENT'),
('Jane', 'Smith', 'jane.smith@example.com', 'jane123', '514-555-5678', 'CLIENT'),
('Alice', 'Johnson', 'alice.johnson@example.com', 'alice123', '514-555-8765', 'CLIENT');

INSERT OR IGNORE INTO instructor (firstName, lastName, email, password, phoneNumber, role) VALUES
('Bob', 'Brown', 'bob.brown@example.com', 'bob123', '514-555-4321', 'INSTRUCTOR'),
('Sara', 'Davis', 'sara.davis@example.com', 'sara123', '514-555-6789', 'INSTRUCTOR');

INSERT OR IGNORE INTO location (name, address, city, province, postalCode) VALUES
('Community Center', '123 Main St', 'Townsville', 'TS', 'A1B2C3'),
('Learning Academy', '456 Elm St', 'Townsville', 'TS', 'A4B5C6');

INSERT OR IGNORE INTO child (firstName, lastName, dateOfBirth, parentId) VALUES
('Mike', 'Doe', '2015-05-10 00:00:00', 1),
('Emily', 'Doe', '2017-03-15 00:00:00', 1),
('Liam', 'Smith', '2016-08-20 00:00:00' , 2);

INSERT OR IGNORE INTO lesson (type, title, locationId, assignedInstructorId, startTime, endTime) VALUES
('GROUP', 'Basic Mathematics', 1, 1, '10:00:00', '11:00:00'),
('PRIVATE', 'Introduction to Biology', 1, 2, '11:00:00', '12:00:00'),
('PRIVATE', 'Creative Arts', 2, 1, '13:00:00', '14:00:00');

INSERT OR IGNORE INTO lesson_schedule (lessonId, dayOfWeek) VALUES
(1, 'MONDAY'),
(1, 'WEDNESDAY'),
(2, 'TUESDAY'),
(2, 'THURSDAY'),
(3, 'FRIDAY');


