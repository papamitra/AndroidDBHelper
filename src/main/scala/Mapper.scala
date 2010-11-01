
package org.papamitra.android.dbhelper

trait Mapper[A <: Mapper[A]] {
  //  def getSingleton: MetaMapper[A]
  def primaryKey:MappedField[_, A]
}
