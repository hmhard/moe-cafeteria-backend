#!/bin/bash

# Set Java environment
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

# Clean and build
echo "Cleaning and building..."
./gradlew clean build -x test

# Run the application
echo "Starting the application..."
./gradlew bootRun -x test 