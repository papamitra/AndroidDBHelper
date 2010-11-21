
package org.papamitra.android.dbhelper

import android.content.{ ContentValues, Context }
import android.database.{ Cursor, SQLException }
import android.database.sqlite.{ SQLiteDatabase, SQLiteOpenHelper, SQLiteQueryBuilder }

import scala.collection.mutable.ListBuffer

trait MapperDBHelper[A <: Mapper[A]] { self: MetaMapper[A] =>

  def dbHelper: SQLiteOpenHelper

  private object EmptyCursorIter extends Iterator[Cursor] {
    def hasNext = false
    def next: Cursor = throw new java.util.NoSuchElementException()
  }

  private class CursorIter(cur: Cursor) extends Iterator[Cursor] {
    def hasNext = !cur.isLast()
    def next: Cursor =
      if (cur.moveToNext) cur
      else
        throw new java.util.NoSuchElementException()
  }

  implicit def cursor2Iterable(cur: Cursor): Iterator[Cursor] =
    if (!cur.moveToFirst) EmptyCursorIter
    else {
      cur.moveToPrevious
      new CursorIter(cur)
    }

  def using[A <: { def close(): Unit }, B](param: A)(f: A => B): B =
    try {
      f(param)
    } finally {
      param.close()
    }

  def createTableSQL = "CREATE TABLE " + dbTableName + " (" +
    mappedFieldList.map {
      case FieldHolder(name, _, field) =>
        name + " " + field.dbColumnTypeStr +
          (if (primaryKeyField == field) " PRIMARY KEY AUTOINCREMENT" else "")
    }.mkString(",") +
    ");"

  def dropTableSQL = "DROP TABLE IF EXISTS " + dbTableName + ";"

  def update(mapper: A) {
    val values = new ContentValues()
    mappedFieldList.foreach {
      case FieldHolder(name, meth, _) =>
        val field = meth.invoke(mapper).asInstanceOf[MappedField[AnyRef, A]]
        valput(values, name, field)
    }

    dbHelper.getWritableDatabase.update(dbTableName, values,
      self.primaryKeyField.name + " = " + mapper.primaryKeyField.is.get,
      null)
  }

  def insert(mapper: A) {
    val values = new ContentValues()
    mappedFieldList.foreach {
      case FieldHolder(name, meth, _) =>
        val field = meth.invoke(mapper).asInstanceOf[MappedField[AnyRef, A]]
        if (!field.isAutoGenerated) valput(values, name, field)
    }

    dbHelper.getWritableDatabase.insert(dbTableName, "", values)

  }

  def findAllCursor(where: Option[Where] = None): Cursor = {
    val qb = new SQLiteQueryBuilder()
    qb setTables dbTableName
    where foreach (w => qb.appendWhereEscapeString(w.toSQL))
    qb.query(dbHelper.getReadableDatabase, null, null, null, null, null, null)
  }

  def findAll(where: Option[Where]): Seq[A] =
    using(findAllCursor(where)) { c =>
      val listBuffer = new ListBuffer[A]
      c.foreach { cursor =>
        val mapper = self.create
        mappedFieldList.foreach {
          case FieldHolder(name, meth, _) =>
            val field = meth.invoke(mapper).asInstanceOf[MappedField[_, A]]
            field(dbValue(cursor, field.name))
        }
        listBuffer += mapper
      }
      listBuffer.toList
    }

  def findAll: Seq[A] = findAll(None)
  def findAll(where: Where): Seq[A] = findAll(Some(where))

  def valput(v: ContentValues, name: String, field: MappedField[_, A]) =
    field.is map (_ match {
      case i: Int => v.put(name, i.asInstanceOf[java.lang.Integer])
      case str: String => v.put(name, str.asInstanceOf[java.lang.String])
      case l: Long => v.put(name, l.asInstanceOf[java.lang.Long])
      case d: Double => v.put(name, d.asInstanceOf[java.lang.Double])
      case b: Boolean => v.put(name, (if (b) 1 else 0).asInstanceOf[java.lang.Integer])
      case _ => throw new Exception("wrong valput")
    })

  import scala.reflect.Manifest

  def dbValue[T](cur: Cursor, colName: String)(implicit m: Manifest[T]): T = {
    val colIndex = cur.getColumnIndexOrThrow(colName)
    (m match {
      case c if c equals manifest[Int] => cur.getInt(colIndex)
      case c if c equals manifest[String] => cur.getString(colIndex)
      case c if c equals manifest[Long] => cur.getLong(colIndex)
      case c if c equals manifest[Double] => cur.getDouble(colIndex)
      case c if c equals manifest[Boolean] => cur.getInt(colIndex)
    }).asInstanceOf[T]
  }

}
