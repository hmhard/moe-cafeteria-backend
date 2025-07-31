#!/bin/bash

# MOE Cafeteria Backend Startup Script

echo "Starting MOE Cafeteria Backend..."

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "Error: Java is not installed. Please install Java 17 or higher."
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "Error: Java 17 or higher is required. Current version: $JAVA_VERSION"
    exit 1
fi

# Check if MySQL is running (optional)
if command -v mysql &> /dev/null; then
    if ! mysqladmin ping -h localhost --silent; then
        echo "Warning: MySQL does not appear to be running. Please start MySQL before running the application."
        echo "You can start MySQL with: sudo systemctl start mysql"
    fi
fi

# Build the project (skip tests)
echo "Building the project..."
./gradlew build -x test

if [ $? -ne 0 ]; then
    echo "Error: Build failed. Please check the error messages above."
    exit 1
fi

# Run the application
echo "Starting the application..."
./gradlew bootRun

echo "Application stopped." 