package org.papamitra.android.dbhelper

abstract class MappedDouble[A <: Mapper[A]](val fieldOwner: A) extends MappedField[Double, A] {
  override def dbColumnTypeStr = "REAL"
}
