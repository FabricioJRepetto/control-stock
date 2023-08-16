# Control de Stock
Desktop app desarrollada en Java + swing, con conexión a base de datos MySQL.<br>
Implementación de JDBC (Java DataBase Connectivity) con drivers MySQL.

### Curso: Trabajando una conexión con una base de datos

#### 1 - Introducción
- JDBC
- Drivers
- `DriverManager` y el método `getConnection`

#### 2 - Comandos SQL
- Encapsular funcionalidades: `ConnectionFactory`.
- Ejecutar comandos SQL: `java.sql.Statement`, `execute`, `executeQuery`

#### 3 - Refactorizando
- Seguridad en las queries: SQL injection
- `PreparedStatement`
- Evitar problemas en las transacciones: autoCommit false, `commit`, `rollback`
- Try-with-resources

#### 4 - Pool de conexiones
- Dependencia C3P0 para crear pool de conexiones
- `java.sql.Connection`, `javax.sql.DataSource`

#### 5 - Data Access Object (DAO)
- Mediador entre aplicación / base de datos
- Encapsulación de los métodos de acceso a la DB
- Manejo de `SQLException`

#### 6 - Evitar queries N + 1
- Tablas relacionales
- `JOIN`