import org.papamitra.android.dbhelper._
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec

import java.lang.reflect.Method

class TestTable extends Mapper[TestTable] {
  object _id extends MappedInt(this)
  object test_txt extends MappedString(this)
  def primaryKeyField = _id
  def dbTableName = "test_table"
}

object TestTable extends TestTable with MetaMapper[TestTable]

class Specs extends Spec with ShouldMatchers {

  describe("Mapper") {
    it("初期化時の値をチェック") {
      val table = new TestTable
      table._id.is should be(None)
      table._id.isAutoGenerated should be(true)

      table.test_txt.is should be(None)
      table.test_txt.isAutoGenerated should be(false)
    }

    it("test_txtに値を設定して取得する") {
      val table = new TestTable
      table.test_txt("test")
      table.test_txt.is should be(Some("test"))
    }
  }

  describe("MetaMapper") {
    it("mappedFieldListが正しく設定されている") {
      TestTable.mappedFieldList.map {
        case TestTable.FieldHolder(name, method, field) =>
          name match {
            case "_id" =>
              val f = method.invoke(TestTable)
              f should be(TestTable._id)
              f should be(field)
              field.isAutoGenerated should be(true)
            case "test_txt" =>
              val f = method.invoke(TestTable)
              f should be(TestTable.test_txt)
              f should be(field)
              field.isAutoGenerated should be(false)
          }
      }
    }
  }
}