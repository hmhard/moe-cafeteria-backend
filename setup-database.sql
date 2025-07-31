-- MOE Cafeteria Management System Database Setup
-- Run this script to create the database and user

-- Create database
CREATE DATABASE IF NOT EXISTS moe_cafeteria 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- Create user (optional - you can use root or create a dedicated user)
-- CREATE USER 'cafeteria_user'@'localhost' IDENTIFIED BY 'cafeteria_password';
-- GRANT ALL PRIVILEGES ON moe_cafeteria.* TO 'cafeteria_user'@'localhost';
-- FLUSH PRIVILEGES;

-- Use the database
USE moe_cafeteria;

-- The application will automatically create tables using JPA/Hibernate
-- Sample data will be inserted by the DataInitializer class

-- Verify database creation
SELECT 'MOE Cafeteria database setup completed successfully!' as status; 