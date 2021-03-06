import sbt._

trait Defaults {
  def androidPlatformName = "android-2.1"
}
class AndroidDBHelper(info: ProjectInfo) extends ParentProject(info) {
  override def shouldCheckOutputDirectories = false
  override def updateAction = task { None }

  lazy val main  = project(".", "AndroidDBHelper", new MainProject(_))
//  lazy val tests = project("tests",  "tests", new TestProject(_), main)

  class MainProject(info: ProjectInfo) extends AndroidProject(info) with Defaults with MarketPublish with TypedResources{
    val keyalias  = "change-me"
    val scalatest = "org.scalatest" % "scalatest" % "1.2"
  }

}
