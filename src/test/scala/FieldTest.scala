package org.papamitra.android.dbhelper

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec

import android.database.sqlite.SQLiteOpenHelper

class FieldTestTable extends Mapper[FieldTestTable] {
  object _id extends MappedInt(this)
  object boolean extends MappedBoolean(this)
  def primaryKeyField = _id
  def dbTableName = "test_table"
}

object FieldTestTable extends FieldTestTable with MetaMapper[FieldTestTable] with MapperDBHelper[FieldTestTable]{
  def dbHelper = new SQLiteOpenHelper
}

class MappedBooleanTest extends Spec with ShouldMatchers {
  describe("MappedBoolean") {
      it("値trueを保持"){
	val f = (new FieldTestTable).boolean
	f(true)
	f.is.get should be (true)
      }

      it("boolean === trueが (boolean = 1)というSQLになる"){
	(FieldTestTable.boolean===true).toSQL should be ("(boolean = 1)")
      }
      it("boolean === falseが (boolean = 0)というSQLになる"){
	(FieldTestTable.boolean===false).toSQL should be ("(boolean = 0)")
      }

  }
}
