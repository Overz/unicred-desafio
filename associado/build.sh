#!/bin/sh

./clean.sh

./resources.sh

echo "Buildando.."

mvn package -DskipTests=true
