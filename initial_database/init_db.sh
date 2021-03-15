#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username root --dbname postgres <<-EOSQL
    create schema application;
    create schema liquibase;

EOSQL
