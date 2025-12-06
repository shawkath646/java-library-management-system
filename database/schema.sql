CREATE DATABASE IF NOT EXISTS library_app;
USE library_app;

CREATE TABLE IF NOT EXISTS books (
    book_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    author VARCHAR(100) NOT NULL,
    publisher VARCHAR(100),
    category VARCHAR(50),
    quantity INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_title (title),
    INDEX idx_author (author),
    INDEX idx_category (category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS members (
    member_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20) NOT NULL,
    address VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_name (name),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS issued_books (
    issue_id INT AUTO_INCREMENT PRIMARY KEY,
    book_id INT NOT NULL,
    member_id INT NOT NULL,
    issue_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE,
    FOREIGN KEY (member_id) REFERENCES members(member_id) ON DELETE CASCADE,
    INDEX idx_book_id (book_id),
    INDEX idx_member_id (member_id),
    INDEX idx_return_date (return_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'Staff',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO users (username, password, role) VALUES 
('admin', 'admin123', 'Admin');

INSERT INTO books (title, author, publisher, category, quantity) VALUES
('The Great Gatsby', 'F. Scott Fitzgerald', 'Scribner', 'Fiction', 5),
('To Kill a Mockingbird', 'Harper Lee', 'J.B. Lippincott & Co.', 'Fiction', 3),
('1984', 'George Orwell', 'Secker & Warburg', 'Science Fiction', 4),
('Pride and Prejudice', 'Jane Austen', 'T. Egerton', 'Romance', 6),
('The Catcher in the Rye', 'J.D. Salinger', 'Little, Brown and Company', 'Fiction', 2);

INSERT INTO members (name, email, phone, address) VALUES
('John Doe', 'john.doe@email.com', '1234567890', '123 Main St, City'),
('Jane Smith', 'jane.smith@email.com', '0987654321', '456 Oak Ave, Town'),
('Bob Johnson', 'bob.johnson@email.com', '5551234567', '789 Pine Rd, Village');

SELECT 'Database library_app created successfully!' AS Status;
