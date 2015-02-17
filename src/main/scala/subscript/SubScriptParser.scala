package subscript

import org.parboiled2._

class SubScriptParser(val input: ParserInput) extends Parser {
  def InputLine = rule { Term ~ EOI }

  def Term: Rule1[Trm] = rule {
    Number ~ " plus "  ~ Term ~> {Sum (_, _)} |
    Number ~ " minus " ~ Term ~> {Diff(_, _)} |
    Number
  }

  def Number = rule { capture(Digits) ~> {x: String => Num(x.toInt)} }

  def Digits = rule { oneOrMore(CharPredicate.Digit) }
}

trait Trm
case class Num (n: Int)           extends Trm
case class Sum (n1: Trm, n2: Trm) extends Trm
case class Diff(n1: Trm, n2: Trm) extends Trm

