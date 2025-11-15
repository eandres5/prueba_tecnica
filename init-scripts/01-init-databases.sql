CREATE DATABASE clientedb;
CREATE DATABASE cuentasdb;

-- Conceder permisos al usuario postgresadmin
GRANT ALL PRIVILEGES ON DATABASE clientedb TO postgresadmin;
GRANT ALL PRIVILEGES ON DATABASE cuentasdb TO postgresadmin;