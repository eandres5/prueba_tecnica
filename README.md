# Herramientas y tecnologias utilizadas
- **Arquitectura de Microservicios**
- **Dockerizado**
- **Java 21** 
- **PostgreSQL**
- **ActiveMQ**
- **Spring Boot**

# Basta con ejecutar el script siguiente
./build-and-start.sh

# BDD
La base de datos se crea automaticamente gracias al hibernate
pero es necesario ejecturar primero ejecutar el script - 01-init-databases.sql

# Aplicacioens
Cliente Service	http://localhost:8081	8081	Gestión de clientes y personas

Cuenta Service	http://localhost:8082	8082	Gestión de cuentas y movimientos

ActiveMQ Console	http://localhost:8161	8161	Consola de administración de mensajes

PostgreSQL	localhost:5432	5432	Base de datos principal