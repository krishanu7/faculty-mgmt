-- SQL script to create the exact tables required:
-- Adjust owner/encoding as needed and run as a superuser or a user with CREATE privileges.

CREATE TABLE IF NOT EXISTS employee (
  id SERIAL PRIMARY KEY,
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  email VARCHAR(200)
);

CREATE TABLE IF NOT EXISTS courses (
  id SERIAL PRIMARY KEY,
  code VARCHAR(50) NOT NULL,
  name VARCHAR(200) NOT NULL,
  description TEXT
);

CREATE TABLE IF NOT EXISTS student (
  id SERIAL PRIMARY KEY,
  roll_number VARCHAR(50) UNIQUE,
  first_name VARCHAR(100),
  last_name VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS student_courses (
  id SERIAL PRIMARY KEY,
  student_id INTEGER NOT NULL REFERENCES student(id) ON DELETE CASCADE,
  course_id INTEGER NOT NULL REFERENCES courses(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS faculty_courses (
  id SERIAL PRIMARY KEY,
  employee_id INTEGER NOT NULL REFERENCES employee(id) ON DELETE CASCADE,
  course_id INTEGER NOT NULL REFERENCES courses(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS grades (
  id SERIAL PRIMARY KEY,
  student_id INTEGER NOT NULL REFERENCES student(id) ON DELETE CASCADE,
  course_id INTEGER NOT NULL REFERENCES courses(id) ON DELETE CASCADE,
  marks numeric(5,2)
);

-- Optional sample seed data (uncomment to use)
INSERT INTO employee (first_name, last_name, email) VALUES ('Krishanu','Saha','krishanu1137@gmail.com');
INSERT INTO employee (first_name, last_name, email) VALUES ('Gautam','Agarwal','gaut.ag@gmail.com');
INSERT INTO courses (code, name, description) VALUES ('CS101','Intro to CS','Introductory course');
INSERT INTO courses (code, name, description) VALUES ('CS201','Data Structures','Intermediate');
INSERT INTO student (roll_number, first_name, last_name) VALUES ('R001','Sumit','Agarwal');
INSERT INTO student (roll_number, first_name, last_name) VALUES ('R002','Rounak','Chokara');
INSERT INTO student (roll_number, first_name, last_name) VALUES ('R003','Yash','Agarwal');
INSERT INTO faculty_courses (employee_id, course_id) VALUES (1,1);
INSERT INTO faculty_courses (employee_id, course_id) VALUES (1,2);
INSERT INTO faculty_courses (employee_id, course_id) VALUES (2,2);
INSERT INTO student_courses (student_id, course_id) VALUES (1,1);
INSERT INTO student_courses (student_id, course_id) VALUES (2,1);
INSERT INTO student_courses (student_id, course_id) VALUES (3,2);
