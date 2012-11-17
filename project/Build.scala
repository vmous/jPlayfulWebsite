import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "jPlayfulWebsite"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      // Add your project dependencies here,
      "org.webjars" % "webjars-play" % "0.1",
      "org.webjars" % "jquery" % "1.8.2",
      "org.webjars" % "bootstrap" % "2.2.1"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
      // Add your own project settings here      
    )

}
