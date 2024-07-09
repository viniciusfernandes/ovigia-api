#!/bin/bash

# MongoDB connection details
MONGO_HOST="localhost"
MONGO_PORT="27017"
MONGO_USER="admin"
MONGO_PASS="1234"

# Command to check MongoDB health
mongo_status=$(mongosh --host $MONGO_HOST --port $MONGO_PORT --username $MONGO_USER --password $MONGO_PASS --authenticationDatabase admin --eval "db.adminCommand('ping')" --quiet)

if [[ $mongo_status == *"ok"* ]]; then
  echo "MongoDB is healthy"
  exit 0
else
  echo "MongoDB is not healthy"
  exit 1
fi
