
package android.database.sqlite

class SQLiteOpenHelper{
  def getReabableDatabase = new SQLiteDatabase
  def getWritableDatabase = new SQLiteDatabase
}

class SQLiteDatabase{
  
}

class SQLiteQueryBuilder{
  def query(db:SQLiteDatabase, lst:AnyRef*){}
}
