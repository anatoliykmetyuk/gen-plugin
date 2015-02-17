name := "generator"
organization := "com.ang"
 
scalaVersion in Global := "2.10.3"
 
sbtPlugin := true

libraryDependencies ++= Seq(
  "commons-io" % "commons-io" % "2.4",
  "org.parboiled" %% "parboiled" % "2.0.1"
)
