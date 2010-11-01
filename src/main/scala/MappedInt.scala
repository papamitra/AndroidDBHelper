
package org.papamitra.android.dbhelper

abstract class MappedInt[A <: Mapper[A]](val fieldOwner: A) extends MappedField[Int, A] {
  override def dbColumnTypeStr = "INTEGER"

  override def dbColumnType = classOf[java.lang.Integer]

  override def isPrimaryKey = false
}

abstract class MappedIntIndex[A <: Mapper[A]](fieldOwner:A) extends MappedInt(fieldOwner) {
  override def isPrimaryKey = true
}
