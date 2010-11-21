
package org.papamitra.android.dbhelper

trait MappedField[FieldType <: Any, OwnerType <: Mapper[OwnerType]] {

  def dbColumnTypeStr:String

  def valToSQL(v:FieldType) = v.toString

  def isAutoGenerated:Boolean = fieldOwner.primaryKeyField == this

  private var data: Option[FieldType] = None

  def is: Option[FieldType] = data

  private var _name: Option[String] = None

  def name:String = _name.getOrElse("")

  def fieldOwner: OwnerType

  def set(f: FieldType): Option[FieldType] = { data = Some(f); data }

  def setName(name: String) = {
    _name = Some(name)
    _name
  }

  def apply(v: FieldType): OwnerType = {
    this.set(v)
    fieldOwner
  }

  def ===(rhs: FieldType) = new ===(this, rhs)
  def > (rhs: FieldType) = new Gt(this, rhs)
  def < (rhs: FieldType) = new Lt(this, rhs)
  def in_(rhs: FieldType*) = new In(this, rhs)
}
