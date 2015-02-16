// build root project
lazy val root = Project("testproject", file(".")) dependsOn(genplugin)
 
// depends on the awesomeOS project
lazy val genplugin = file("..").getAbsoluteFile.toURI