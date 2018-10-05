package com.dansuse.playwithkotlin.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.dansuse.playwithkotlin.model.Favorite
import org.jetbrains.anko.db.*

class MyDatabaseOpenHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "FavoriteEvent.db", null, 1) {
    companion object {
        private var instance: MyDatabaseOpenHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): MyDatabaseOpenHelper {
            if (instance == null) {
                instance = MyDatabaseOpenHelper(ctx.applicationContext)
            }
            return instance as MyDatabaseOpenHelper
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Here you create tables
        db.createTable(Favorite.TABLE_FAVORITE, true,
                Favorite.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                Favorite.EVENT_ID to TEXT + UNIQUE,
                Favorite.EVENT_DATE to TEXT,
                Favorite.HOME_SCORE to TEXT,
                Favorite.AWAY_SCORE to TEXT,
                Favorite.HOME_NAME to TEXT,
                Favorite.AWAY_NAME to TEXT,
                Favorite.HOME_BADGE to TEXT,
                Favorite.AWAY_BADGE to TEXT
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Here you can upgrade tables, as usual
        db.dropTable(Favorite.TABLE_FAVORITE, true)
    }
}

// Access property for Context
val Context.database: MyDatabaseOpenHelper
    get() = MyDatabaseOpenHelper.getInstance(applicationContext)