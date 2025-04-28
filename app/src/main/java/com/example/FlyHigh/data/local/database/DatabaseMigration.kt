package com.example.FlyHigh.data.local.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseMigration {
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Crear la tabla de usuarios
            database.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `users` (
                  `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                  `firebaseUid` TEXT,
                  `username` TEXT NOT NULL,
                  `email` TEXT NOT NULL,
                  `birthDate` INTEGER NOT NULL,
                  `address` TEXT NOT NULL,
                  `country` TEXT NOT NULL,
                  `phoneNumber` TEXT NOT NULL,
                  `acceptEmailsOffers` INTEGER NOT NULL DEFAULT 0
                )
                """
            )

            // Crear índice único para username
            database.execSQL(
                "CREATE UNIQUE INDEX IF NOT EXISTS `index_users_username` ON `users` (`username`)"
            )

            // Crear una tabla temporal para viajes
            database.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `trips_temp` (
                  `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                  `userId` INTEGER NOT NULL,
                  `title` TEXT NOT NULL,
                  `destination` TEXT NOT NULL,
                  `startDate` INTEGER NOT NULL,
                  `endDate` INTEGER NOT NULL,
                  `description` TEXT NOT NULL,
                  `imageUrl` TEXT,
                  FOREIGN KEY(`userId`) REFERENCES `users`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
                )
                """
            )

            // Crear índice para userId en la tabla de viajes
            database.execSQL(
                "CREATE INDEX IF NOT EXISTS `index_trips_userId` ON `trips_temp` (`userId`)"
            )

            // Crear usuario temporal para migración de datos
            database.execSQL(
                """
                INSERT INTO `users` (`username`, `email`, `birthDate`, `address`, `country`, `phoneNumber`, `acceptEmailsOffers`)
                VALUES ('migrated_user', 'migrated@example.com', 0, 'Migration Address', 'Migration Country', '+00000000', 0)
                """
            )

            // Obtener el ID del usuario temporal
            val cursor = database.query("SELECT id FROM users WHERE username = 'migrated_user'")
            cursor.moveToFirst()
            val userId = cursor.getLong(0)
            cursor.close()

            // Migrar datos de la tabla trips a trips_temp
            database.execSQL(
                """
                INSERT INTO `trips_temp` (`id`, `userId`, `title`, `destination`, `startDate`, `endDate`, `description`, `imageUrl`)
                SELECT `id`, $userId, `title`, `destination`, `startDate`, `endDate`, `description`, `imageUrl` FROM `trips`
                """
            )

            // Eliminar tabla antigua
            database.execSQL("DROP TABLE `trips`")

            // Renombrar tabla temporal a tabla principal
            database.execSQL("ALTER TABLE `trips_temp` RENAME TO `trips`")
        }
    }
}