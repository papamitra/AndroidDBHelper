
package android.database

class Cursor{
  def getColumnIndexOrThrow(colName:String) = 0
  def isLast = false
  def moveToPrevious = true
  def moveToFirst = true
  def close = true

  def getInt(idx:Int) = 1
  def getString(idx:Int) = "test"
  def getLong(idx:Int) = 2
  def getDouble(idx:Int) = 3

  
}
