package com.dansuse.playwithkotlin.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.dansuse.playwithkotlin.model.FavoriteMatch
import com.dansuse.playwithkotlin.model.FavoriteTeam
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
    db.createTable(FavoriteMatch.TABLE_FAVORITE, true,
        FavoriteMatch.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
        FavoriteMatch.EVENT_ID to TEXT + UNIQUE,
        FavoriteMatch.EVENT_DATE to TEXT,
        FavoriteMatch.HOME_SCORE to TEXT,
        FavoriteMatch.AWAY_SCORE to TEXT,
        FavoriteMatch.HOME_NAME to TEXT,
        FavoriteMatch.AWAY_NAME to TEXT,
        FavoriteMatch.HOME_BADGE to TEXT,
        FavoriteMatch.AWAY_BADGE to TEXT
    )

    db.createTable(FavoriteTeam.TABLE_FAVORITE, true,
        FavoriteTeam.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
        FavoriteTeam.TEAM_ID to TEXT + UNIQUE,
        FavoriteTeam.TEAM_NAME to TEXT,
        FavoriteTeam.TEAM_BADGE to TEXT)
  }

  override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    // Here you can upgrade tables, as usual
    db.dropTable(FavoriteMatch.TABLE_FAVORITE, true)
    db.dropTable(FavoriteTeam.TABLE_FAVORITE, true)
  }
}

// Access property for Context
val Context.database: MyDatabaseOpenHelper
  get() = MyDatabaseOpenHelper.getInstance(applicationContext)