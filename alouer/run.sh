#!/bin/bash

# Run Maven to clean and package the project
echo "Building the project with Maven..."
mvn clean package

# Check if the build was successful
if [ $? -eq 0 ]; then
  echo "Build successful."

  # Define the path to the generated JAR file
  JAR_FILE=$(ls target/*.jar | head -n 1)

  # Check if the JAR file exists
  if [ -f "$JAR_FILE" ]; then
    echo "Launching the application..."
    java -jar "$JAR_FILE"
  else
    echo "Error: JAR file not found in the target directory."
    exit 1
  fi
else
  echo "Build failed."
  exit 1
fi
