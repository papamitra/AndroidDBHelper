package org.papamitra.android.dbhelper

abstract class MappedBoolean[A <: Mapper[A]](val fieldOwner: A) extends MappedField[Boolean, A] {
  override def dbColumnTypeStr = "INTEGER"

  override def valToSQL(v:Boolean) = if(v) "1" else "0"
}
