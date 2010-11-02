
package org.papamitra.android.dbhelper

import android.database.sqlite.{SQLiteOpenHelper,SQLiteDatabase}
import android.content.Context

trait SingleTableDBHelper[A <: Mapper[A]] extends MapperDBHelper[A] { self: MetaMapper[A] =>
  val dbFileName:String
  val dbVersion:Int
  def dbContext:Context

  override def dbHelper = new DBOpenHelper(dbContext)

  def onCreate(db:SQLiteDatabase){
    db.execSQL(createTableSQL)
  }

  def onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
      db.execSQL(dropTableSQL)
      onCreate(db)    
  }

  class DBOpenHelper(c: Context) extends SQLiteOpenHelper(c, dbFileName, null, dbVersion) {
    override def onCreate(db: SQLiteDatabase) {
      self.onCreate(db)
    }

    override def onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
      self.onUpgrade(db, oldVersion, newVersion)
    }
  }
}

