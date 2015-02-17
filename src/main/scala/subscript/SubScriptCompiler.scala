package subscript

import java.io.File
import org.apache.commons.io._
import scala.util.Try

object SubScriptCompiler {

  /**
   * The compiler compiles an arbitrary file to a valid Scala code.
   */
  def compile(inFile: File, inputDir: File): String = {

    // Get the metadata of the artifact: the file name, the object name, the package name
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

    // Read the contents of the file into a String
    val contents : String   = FileUtils.readFileToString(inFile)

    // Parse it to an AST, using Parboiled parser
    val ast      : Try[Trm] = new SubScriptParser(contents).InputLine.run()

    // Rewrite this AST into a valid Scala code. If something went wrong during parsing,
    // rewrite to a default "println". Probably, we'd rather throw a real exception here...
    val rewritten: String   = 
      ast.map {trm => SubScriptRewriter(trm)}.getOrElse("""println("ERROR WHILE PARSING!")""")

    // Return the generated Scala code to whoever called this method.
    s"""
      |$pkgName
      |
      |object $objectName{
      |  def apply() = $rewritten
      |}
    """.stripMargin
  }

}