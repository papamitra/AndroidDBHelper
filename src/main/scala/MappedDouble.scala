package org.papamitra.android.dbhelper

abstract class MappedDouble[A <: Mapper[A]](val fieldOwner: A) extends MappedField[Double, A] {
  def dbColumnTypeStr = "REAL"
  def default = 0.0
}
