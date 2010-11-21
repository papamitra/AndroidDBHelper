package org.papamitra.android.dbhelper

import android.database.DatabaseUtils

abstract class MappedString[A <: Mapper[A]](val fieldOwner: A) extends MappedField[String, A] {
  def dbColumnTypeStr = "TEXT"

  override def valToSQL(v:String) = DatabaseUtils.sqlEscapeString(v)
}
