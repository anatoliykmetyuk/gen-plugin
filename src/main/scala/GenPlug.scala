import sbt.Keys._
import sbt._

object GenPlug extends AutoPlugin {

  val subscriptDirectory = taskKey[sbt.File]("Clone stuff from github")

  val mySeq = Seq(
    subscriptDirectory := sourceDirectory.value / "subscript",

    managedSources ++= {
      val inputDir = subscriptDirectory.value
      val outputDir = sourceManaged.value / "subscript"
      val inputFiles = (inputDir ** "*.subscript").get
      println("Generating SubScript Sources...")
      val outputFiles = for(inFile <- inputFiles) yield {
        val outFile = new sbt.File(
          outputDir.getAbsolutePath + inFile.getAbsolutePath.drop(inputDir.getAbsolutePath.length).dropRight(9) + "scala"
        )
        val name = inFile.getName
        val objectName = name.slice(name.lastIndexOf('/')+1, name.lastIndexOf('.'))
        val pkgName =
          inFile.getAbsolutePath
            .drop(inputDir.getAbsolutePath.length + 1)
            .toString
            .split("/")
            .dropRight(1)
            .map(s => s"package $s")
            .mkString("\n")

        IO.write(
          outFile,
          s"""
            |$pkgName
            |
            |object $objectName{
            |  def apply(): String = s"My name is $objectName"
            |}
            |
            |${IO.readLines(inFile).map("//"+_).mkString("\n")}
          """.stripMargin
        )
        outFile
      }
      outputFiles
    }
  )
  override val projectSettings = inConfig(Test)(mySeq) ++ inConfig(Compile)(mySeq) ++ Seq(
    watchSources ++= ((subscriptDirectory in Compile).value ** "*.subscript").get
  )
}