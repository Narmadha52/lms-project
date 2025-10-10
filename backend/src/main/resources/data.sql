-- Sample data for LMS
USE lms_db;

-- Insert sample users
INSERT INTO users (username, email, password, first_name, last_name, role, is_approved) VALUES
('admin', 'admin@lms.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Admin', 'User', 'ADMIN', true),
('instructor1', 'instructor@lms.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'John', 'Instructor', 'INSTRUCTOR', true),
('instructor2', 'jane.instructor@lms.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Jane', 'Smith', 'INSTRUCTOR', true),
('student1', 'student@lms.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Alice', 'Student', 'STUDENT', true),
('student2', 'bob.student@lms.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Bob', 'Johnson', 'STUDENT', true),
('student3', 'charlie.student@lms.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Charlie', 'Brown', 'STUDENT', true);

-- Insert sample categories
INSERT INTO categories (name, description) VALUES
('Programming', 'Software development and programming courses'),
('Web Development', 'Frontend and backend web development'),
('Data Science', 'Data analysis, machine learning, and AI'),
('Design', 'UI/UX design and graphic design'),
('Business', 'Business and entrepreneurship courses'),
('Language', 'Language learning courses');

-- Insert sample courses
INSERT INTO courses (title, description, instructor_id, category, difficulty_level, price, is_published, thumbnail_url) VALUES
('Complete React Development Course', 'Learn React from scratch to advanced concepts including hooks, context, and state management.', 2, 'Web Development', 'INTERMEDIATE', 99.99, true, 'https://via.placeholder.com/300x200?text=React+Course'),
('Spring Boot Masterclass', 'Master Spring Boot framework for building robust Java applications with microservices architecture.', 2, 'Programming', 'ADVANCED', 149.99, true, 'https://via.placeholder.com/300x200?text=Spring+Boot'),
('Data Science with Python', 'Complete data science course covering pandas, numpy, matplotlib, and machine learning.', 3, 'Data Science', 'INTERMEDIATE', 199.99, true, 'https://via.placeholder.com/300x200?text=Data+Science'),
('UI/UX Design Fundamentals', 'Learn the principles of user interface and user experience design.', 3, 'Design', 'BEGINNER', 79.99, true, 'https://via.placeholder.com/300x200?text=UI+UX+Design'),
('JavaScript for Beginners', 'Start your programming journey with JavaScript fundamentals.', 2, 'Programming', 'BEGINNER', 49.99, true, 'https://via.placeholder.com/300x200?text=JavaScript'),
('Advanced Java Programming', 'Deep dive into advanced Java concepts including multithreading, collections, and design patterns.', 2, 'Programming', 'ADVANCED', 129.99, false, 'https://via.placeholder.com/300x200?text=Advanced+Java');

-- Insert sample lessons
INSERT INTO lessons (course_id, title, description, lesson_type, content, order_index, is_published) VALUES
-- React Course Lessons
(1, 'Introduction to React', 'Understanding what React is and why it\'s popular', 'TEXT', 'React is a JavaScript library for building user interfaces, particularly web applications. It was created by Facebook and is now maintained by Facebook and the community.', 1, true),
(1, 'Setting up React Environment', 'How to set up your development environment', 'TEXT', 'We\'ll cover Node.js installation, create-react-app, and VS Code setup for optimal React development.', 2, true),
(1, 'Components and JSX', 'Understanding React components and JSX syntax', 'TEXT', 'Components are the building blocks of React applications. JSX is a syntax extension that allows you to write HTML-like code in JavaScript.', 3, true),
(1, 'State and Props', 'Managing component state and passing data with props', 'TEXT', 'State is local to a component and can be changed. Props are read-only data passed from parent to child components.', 4, true),
(1, 'React Hooks Tutorial', 'Modern React with hooks', 'VIDEO', 'Learn about useState, useEffect, and other React hooks for functional components.', 5, true),

-- Spring Boot Course Lessons
(2, 'Introduction to Spring Boot', 'What is Spring Boot and its advantages', 'TEXT', 'Spring Boot is a framework that simplifies Spring application development by providing auto-configuration and starter dependencies.', 1, true),
(2, 'Creating Your First Spring Boot App', 'Hands-on tutorial for creating a Spring Boot application', 'TEXT', 'We\'ll create a simple REST API using Spring Boot and test it with Postman.', 2, true),
(2, 'Spring Data JPA', 'Working with databases using Spring Data JPA', 'TEXT', 'Learn how to interact with databases using Spring Data JPA repositories and entities.', 3, true),
(2, 'Spring Security Integration', 'Securing your Spring Boot application', 'TEXT', 'Implement authentication and authorization using Spring Security.', 4, true),
(2, 'RESTful API Development', 'Building REST APIs with Spring Boot', 'VIDEO', 'Complete guide to building RESTful APIs with proper error handling and validation.', 5, true),

-- Data Science Course Lessons
(3, 'Python for Data Science', 'Python basics for data analysis', 'TEXT', 'Essential Python concepts for data science including data types, control structures, and functions.', 1, true),
(3, 'NumPy Fundamentals', 'Working with numerical data using NumPy', 'TEXT', 'NumPy is the foundation of the Python data science stack. Learn arrays, operations, and mathematical functions.', 2, true),
(3, 'Pandas Data Manipulation', 'Data analysis with Pandas', 'TEXT', 'Pandas provides powerful tools for data manipulation and analysis. Learn DataFrames, Series, and data cleaning.', 3, true),
(3, 'Data Visualization with Matplotlib', 'Creating charts and graphs', 'TEXT', 'Visualize your data using Matplotlib and Seaborn for better insights.', 4, true),
(3, 'Machine Learning Basics', 'Introduction to machine learning concepts', 'VIDEO', 'Overview of machine learning algorithms and their applications in data science.', 5, true),

-- UI/UX Design Course Lessons
(4, 'Design Principles', 'Fundamental principles of good design', 'TEXT', 'Learn about balance, contrast, hierarchy, and other essential design principles.', 1, true),
(4, 'User Research Methods', 'Understanding your users', 'TEXT', 'Conduct user research to understand user needs, behaviors, and pain points.', 2, true),
(4, 'Wireframing and Prototyping', 'Creating wireframes and prototypes', 'TEXT', 'Learn how to create wireframes and interactive prototypes using tools like Figma.', 3, true),
(4, 'Color Theory and Typography', 'Visual design elements', 'TEXT', 'Master color theory and typography to create visually appealing designs.', 4, true),
(4, 'Usability Testing', 'Testing your designs with real users', 'VIDEO', 'Learn how to conduct usability tests and gather feedback for design improvements.', 5, true),

-- JavaScript Course Lessons
(5, 'JavaScript Basics', 'Variables, data types, and operators', 'TEXT', 'Learn the fundamentals of JavaScript programming language.', 1, true),
(5, 'Functions and Scope', 'Understanding functions and variable scope', 'TEXT', 'Master JavaScript functions, closures, and scope concepts.', 2, true),
(5, 'DOM Manipulation', 'Working with the Document Object Model', 'TEXT', 'Learn how to interact with HTML elements using JavaScript.', 3, true),
(5, 'Events and Event Handling', 'Responding to user interactions', 'TEXT', 'Handle user events like clicks, form submissions, and keyboard input.', 4, true),
(5, 'Asynchronous JavaScript', 'Promises, async/await, and AJAX', 'VIDEO', 'Learn about asynchronous programming in JavaScript.', 5, true);

-- Insert sample enrollments
INSERT INTO enrollments (student_id, course_id, enrolled_at, progress_percentage, is_completed) VALUES
(4, 1, '2024-01-15 10:00:00', 60.00, false),
(4, 3, '2024-01-20 14:30:00', 40.00, false),
(5, 1, '2024-01-18 09:15:00', 80.00, false),
(5, 2, '2024-01-22 16:45:00', 20.00, false),
(6, 4, '2024-01-25 11:20:00', 100.00, true),
(6, 5, '2024-01-28 13:10:00', 30.00, false);

-- Insert sample lesson progress
INSERT INTO lesson_progress (enrollment_id, lesson_id, is_completed, completed_at, time_spent_minutes) VALUES
-- Student 1 (Alice) - React Course
(1, 1, true, '2024-01-15 10:30:00', 25),
(1, 2, true, '2024-01-15 11:00:00', 30),
(1, 3, true, '2024-01-16 09:15:00', 35),
(1, 4, false, NULL, 15),
(1, 5, false, NULL, 0),

-- Student 1 (Alice) - Data Science Course
(2, 11, true, '2024-01-20 15:00:00', 20),
(2, 12, true, '2024-01-21 10:30:00', 45),
(2, 13, false, NULL, 20),
(2, 14, false, NULL, 0),
(2, 15, false, NULL, 0),

-- Student 2 (Bob) - React Course
(3, 1, true, '2024-01-18 09:45:00', 30),
(3, 2, true, '2024-01-18 10:30:00', 35),
(3, 3, true, '2024-01-19 14:00:00', 40),
(3, 4, true, '2024-01-19 15:00:00', 25),
(3, 5, false, NULL, 10),

-- Student 2 (Bob) - Spring Boot Course
(4, 6, true, '2024-01-22 17:00:00', 20),
(4, 7, false, NULL, 15),
(4, 8, false, NULL, 0),
(4, 9, false, NULL, 0),
(4, 10, false, NULL, 0),

-- Student 3 (Charlie) - UI/UX Design Course
(5, 16, true, '2024-01-25 12:00:00', 30),
(5, 17, true, '2024-01-25 14:30:00', 45),
(5, 18, true, '2024-01-26 10:15:00', 50),
(5, 19, true, '2024-01-26 15:45:00', 40),
(5, 20, true, '2024-01-27 11:20:00', 35),

-- Student 3 (Charlie) - JavaScript Course
(6, 21, true, '2024-01-28 13:30:00', 25),
(6, 22, true, '2024-01-28 15:00:00', 30),
(6, 23, false, NULL, 20),
(6, 24, false, NULL, 0),
(6, 25, false, NULL, 0);

-- Insert sample assignments
INSERT INTO assignments (course_id, title, description, due_date, max_points, is_published) VALUES
(1, 'React Component Project', 'Create a todo list application using React components and state management.', '2024-02-15 23:59:59', 100, true),
(1, 'React Hooks Exercise', 'Build a counter application using React hooks (useState, useEffect).', '2024-02-20 23:59:59', 75, true),
(2, 'Spring Boot REST API', 'Create a REST API for a library management system using Spring Boot.', '2024-02-18 23:59:59', 150, true),
(3, 'Data Analysis Project', 'Analyze a dataset and create visualizations using Python and Pandas.', '2024-02-25 23:59:59', 200, true),
(4, 'UI Design Portfolio', 'Create a portfolio website design using Figma.', '2024-02-22 23:59:59', 100, true),
(5, 'JavaScript Calculator', 'Build a calculator application using vanilla JavaScript.', '2024-02-28 23:59:59', 80, true);

-- Insert sample assignment submissions
INSERT INTO assignment_submissions (assignment_id, student_id, submission_text, submitted_at, grade, feedback, graded_at, graded_by) VALUES
(1, 4, 'I created a todo list with add, edit, delete, and mark complete functionality. Used React hooks for state management.', '2024-02-10 15:30:00', 85.00, 'Good implementation! Consider adding local storage for persistence.', '2024-02-12 10:00:00', 2),
(2, 4, 'Counter app with increment, decrement, and reset functionality using useState and useEffect hooks.', '2024-02-18 14:20:00', 90.00, 'Excellent work! Clean code and proper hook usage.', '2024-02-19 09:30:00', 2),
(1, 5, 'Todo list with drag and drop functionality and filtering options.', '2024-02-12 16:45:00', 95.00, 'Outstanding! Great use of additional libraries and features.', '2024-02-13 11:15:00', 2),
(3, 5, 'Library API with CRUD operations for books, authors, and members. Includes proper error handling.', '2024-02-15 12:00:00', 88.00, 'Good structure, but could improve error messages.', '2024-02-16 14:20:00', 2),
(4, 4, 'Analyzed sales data and created interactive charts showing trends and patterns.', '2024-02-20 18:30:00', 92.00, 'Great analysis! Charts are clear and informative.', '2024-02-22 10:45:00', 3),
(5, 6, 'Portfolio design with modern UI, responsive layout, and smooth animations.', '2024-02-18 20:15:00', 88.00, 'Beautiful design! Consider improving accessibility features.', '2024-02-20 16:30:00', 3),
(6, 6, 'Calculator with basic operations and keyboard support.', '2024-02-25 13:45:00', 75.00, 'Good functionality, but could add more advanced operations.', '2024-02-26 09:00:00', 2);

