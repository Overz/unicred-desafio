#!/bin/bash

set -e
set -u

if [ -n "$POSTGRES_MULTIPLE_DATABASES" ]; then
  echo "CREATING DATABASES: $POSTGRES_MULTIPLE_DATABASES"

  for db in $(echo "$POSTGRES_MULTIPLE_DATABASES" | tr ',' ' '); do
    echo "creating database $db"
    psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
			    CREATE USER $db;
			    CREATE DATABASE $db;
			    GRANT ALL PRIVILEGES ON DATABASE $db TO $db;
		EOSQL

    for file in /docker-entrypoint-initdb.d/*.sql; do
      echo "running file $file for database $db"
      psql --username "$POSTGRES_USER" -d "$db" -a -f "$file"
    done
  done

  echo "DATABASES CREATED"
fi
