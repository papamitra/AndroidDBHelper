package org.papamitra.android.dbhelper

abstract class MappedString[A <: Mapper[A]](val fieldOwner: A) extends MappedField[String, A] {
  def dbColumnTypeStr = "TEXT"

  // TODO: escape string
  override def valToSQL(v:String) = "\"" + v + "\""
}
