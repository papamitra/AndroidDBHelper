
package org.papamitra.android.dbhelper

sealed trait Where {
  def toSQL: String

  def and_(rhs:Where) = new And(this, rhs)
  def or_(rhs:Where) = new Or(this, rhs)
}

class And(lhs: Where, rhs: Where) extends Where {
  def toSQL = "(" + lhs.toSQL + " AND " + rhs.toSQL + ")"
}

class Or(lhs: Where, rhs: Where) extends Where {
  def toSQL = "(" + lhs.toSQL + " OR " + rhs.toSQL + ")"
}

case class ===[T, A <: Mapper[A]](lhs: MappedField[T, A], v: T) extends Where {
  def toSQL = "(" + lhs.name + " = " + lhs.valToSQL(v) + ")"
}

case class Gt[T, A <: Mapper[A]](lhs: MappedField[T, A], v: T) extends Where {
  def toSQL = "(" + lhs.name + " > " + lhs.valToSQL(v) + ")"
}

case class Lt[T, A <: Mapper[A]](lhs: MappedField[T, A], v: T) extends Where {
  def toSQL = "(" + lhs.name + " < " + lhs.valToSQL(v) + ")"
}

case class In[T, A <: Mapper[A]](lhs: MappedField[T,A], lst: Seq[T]) extends Where{
  def toSQL = "(" + lhs.name + " IN (" + lst.map(lhs.valToSQL(_)).mkString(",") + "))"
}
