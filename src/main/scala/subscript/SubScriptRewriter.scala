package subscript

object SubScriptRewriter {

  def apply(t: Trm): String = t match {
    case Num (x: Int)             => x.toString
    case Sum (lhs: Trm, rhs: Trm) => s"${SubScriptRewriter(lhs)} + ${SubScriptRewriter(rhs)}"
    case Diff(lhs: Trm, rhs: Trm) => s"${SubScriptRewriter(lhs)} - ${SubScriptRewriter(rhs)}"
  }

}