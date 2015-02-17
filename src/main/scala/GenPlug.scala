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

        val output = subscript.SubScriptCompiler.compile(inFile, inputDir)
        IO.write(outFile, output)
        outFile
      }
      outputFiles
    }
  )
  override val projectSettings = inConfig(Test)(mySeq) ++ inConfig(Compile)(mySeq) ++ Seq(
    watchSources ++= ((subscriptDirectory in Compile).value ** "*.subscript").get
  )
}