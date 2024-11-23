CREATE DATABASE financemanagement;
USE financemanagement;

CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE ExpenseCategories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(50) NOT NULL UNIQUE
);


CREATE TABLE Expenses (
    expense_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    category_id INT NOT NULL,
    date DATE NOT NULL,
    description TEXT,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES ExpenseCategories(category_id) ON DELETE CASCADE
);

USE financemanagement;

-- Insert initial users
INSERT INTO Users (username, password, email) VALUES ('john_doe', 'password123', 'john_doe@gmail.com');
INSERT INTO Users (username, password, email) VALUES ('jane_smith', 'password456', 'jane_smith@gmail.com');

-- Insert initial expense categories
INSERT INTO ExpenseCategories (category_name) VALUES ('Food');
INSERT INTO ExpenseCategories (category_name) VALUES ('Transport');
INSERT INTO ExpenseCategories (category_name) VALUES ('Utilities');
INSERT INTO ExpenseCategories (category_name) VALUES ('Entertainment');
