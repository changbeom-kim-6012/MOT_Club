#!/bin/bash

FILE="application.pid"

if [ -e $FILE ]; then
  echo "Stop Application..."
  kill -15 `cat application.pid`

  echo "sleep 10s..."
  sleep 10s
fi

java -Dspring.profiles.active=$1 -jar ./build/libs/mothub.war