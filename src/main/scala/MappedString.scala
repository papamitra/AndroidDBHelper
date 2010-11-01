package org.papamitra.android.dbhelper

abstract class MappedString[A <: Mapper[A]](val fieldOwner: A) extends MappedField[String, A] {
  def dbColumnTypeStr = "TEXT"

  def dbColumnType = classOf[java.lang.String]

  override def isPrimaryKey = false
}
