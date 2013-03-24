import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "jPlayfulWebsite"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      // Add your project dependencies here,
//      jdbc, anorm, javaJpa, filters,
      javaCore,
      javaJdbc,
      javaEbean,
      "org.webjars" % "webjars-play" % "2.1.0",
      "org.webjars" % "jquery" % "1.9.1",
      "org.webjars" % "bootstrap" % "2.3.0",
      "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
      // play-authenticate
      "be.objectify" %% "deadbolt-java" % "2.1-SNAPSHOT",
      "com.feth" %% "play-authenticate" % "0.2.5-SNAPSHOT"
    )

    val main = play.Project(appName, appVersion, appDependencies).settings(
      // Add your own project settings here
      
      // play-authenticate
      resolvers += Resolver.url("Objectify Play Repository (release)", url("http://schaloner.github.com/releases/"))(Resolver.ivyStylePatterns),
      resolvers += Resolver.url("Objectify Play Repository (snapshot)", url("http://schaloner.github.com/snapshots/"))(Resolver.ivyStylePatterns),

      resolvers += Resolver.url("play-easymail (release)", url("http://joscha.github.com/play-easymail/repo/releases/"))(Resolver.ivyStylePatterns),
      resolvers += Resolver.url("play-easymail (snapshot)", url("http://joscha.github.com/play-easymail/repo/snapshots/"))(Resolver.ivyStylePatterns),

      resolvers += Resolver.url("play-authenticate (release)", url("http://joscha.github.com/play-authenticate/repo/releases/"))(Resolver.ivyStylePatterns),
      resolvers += Resolver.url("play-authenticate (snapshot)", url("http://joscha.github.com/play-authenticate/repo/snapshots/"))(Resolver.ivyStylePatterns)
    )

}
