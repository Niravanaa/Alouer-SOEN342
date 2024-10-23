-- Inserting sample data into Client table
INSERT INTO Client (firstName, lastName, email, password, role) VALUES 
('John', 'Doe', 'john.doe@example.com', 'password123', 'CLIENT'),
('Jane', 'Smith', 'jane.smith@example.com', 'password123', 'CLIENT');

INSERT INTO Administrator (firstName, lastName, email, password) VALUES
('Admin', 'User', 'admin@example.com', 'admin123');

-- Inserting sample data into Instructor table
INSERT INTO Instructor (firstName, lastName, email, password) VALUES 
('Emily', 'Jones', 'emily.jones@example.com', 'password123');

-- Inserting sample data into Location table
INSERT INTO Location (name, address, city, province, postalCode) VALUES 
('Main Studio', '123 Main St', 'Anytown', 'CA', '12345'),
('North Studio', '456 North Rd', 'Anytown', 'CA', '12345');

-- Inserting sample data into Lesson table
INSERT INTO Lesson (type, title, locationId, isAvailable, startTime, endTime) VALUES 
(1, 'Yoga Basics', 1, 1, '2024-01-01 10:00:00', '2024-01-01 11:00:00'),
(2, 'Advanced Yoga', 2, 1, '2024-01-02 10:00:00', '2024-01-02 11:00:00');

-- Inserting sample data into Booking table
INSERT INTO Booking (clientId, lessonId) VALUES 
(1, 1),
(2, 2);
