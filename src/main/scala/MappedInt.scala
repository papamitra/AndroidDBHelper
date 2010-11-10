
package org.papamitra.android.dbhelper

abstract class MappedInt[A <: Mapper[A]](val fieldOwner: A) extends MappedField[Int, A] {
  override def dbColumnTypeStr = "INTEGER"
}
