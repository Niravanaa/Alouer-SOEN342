#!/bin/bash

# Set the SQLite JDBC driver path
JDBC_DRIVER="sqlite-jdbc-3.47.0.0.jar"

DB_FILE="database.db"

sqlite3 "$DB_FILE" < schema.schema

# Compile the Java program
javac -cp ".:$JDBC_DRIVER" Terminal.java

# Run the Java program
java -cp ".:$JDBC_DRIVER" Terminal