
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    name TEXT,
    email TEXT UNIQUE,
    password TEXT,
    phone_number TEXT UNIQUE,
    location TEXT
);


CREATE TABLE tasks (
    task_id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE,
    topic VARCHAR(255),
    description TEXT,
    due_date DATE,
    start_date DATE,
    user_id INTEGER,
    priority VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL
);


CREATE TABLE subtasks (
    subtask_id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE,
    description TEXT,
    task_id INTEGER REFERENCES tasks(task_id) ON DELETE SET NULL
);