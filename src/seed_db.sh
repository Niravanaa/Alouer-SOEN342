#!/bin/bash

# Define database file
DB_FILE="database.db"

# Create the database if it doesn't exist
if [ ! -f "$DB_FILE" ]; then
    echo "Database file not found. Please create the database first."
    exit 1
fi

# Execute the seed data SQL file
sqlite3 "$DB_FILE" < seed_data.sql

echo "Database seeded successfully."
