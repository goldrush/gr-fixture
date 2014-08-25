package jp.co.applicative.tool

trait Replacement {

  // 変換テーブル未定義はコンパイルエラーにしたい
  val replaceTable: Map[String, String]

  def replaceSpecialWord(target: String): String = {
    var str = target
    replaceTable.keys.foreach { key =>
      str = str replaceAll (str, replaceTable.getOrElse(str, str))
    }
    str
  }
}

