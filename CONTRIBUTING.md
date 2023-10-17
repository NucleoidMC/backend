# Guide for working on this

## Things you need

- Java
- Gradle
- Time (potentially lots of it)
- A Java IDE (IntelliJ/Eclipse/VSCode)

## Project overview

The backend lives in `app/`. Each "module" has a package under `xyz.nucleoid.backend`, containing all of its functionality.

The typical files in a module are (using leaderboards as an example):
- `Leaderboards`: Core module functionality, providing web handlers and holding configuration/database connection objects.
- `LeaderboardsDb`: Code for interacting with the database, encapsulating the SQL queries and holding a reference to the connection pool.
- `LeaderboardEntry`: Models specific to this module.

## Web router

The HTTP server and routing are handled by Javalin, and all routes are located under the `/v2/` prefix (which makes urls like `https://api.nucleoid.xyz/v2/leaderboards`), which typically are namespaced by their module (eg. leaderboards is under `/v2/leaderboards/`). 

## Database

We don't use any ORM framework, just HikariCP as a connection pool, so modules keep all their SQL isolated inside a dedicated database class.

### Adding migrations

1. Create a new class under `xyz.nucleoid.backend.database.migrations` which extends from `Migration`. 
2. The name you pass to `super()` should be unique, and I would recommend just using the name of your class file to ensure this.
3. Place all your SQL in `public static final String`s at the top of the class. Use `applyMigration` to perform whatever operations are needed (`CREATE TABLE` etc.). Don't bother with `IF NOT EXISTS`, the migration will only be run once per database.
4. Add the migration class to the **end** of the list in `xyz.nucleoid.backend.database.Migrations`
5. Next time you launch the backend, it will automagically apply your migration. If it fails, it will log an error and crash, so you can fix the migration and try again.
