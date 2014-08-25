package jp.co.applicative.tool

import org.scalatest.FunSpec

class ReplacementTest extends FunSpec {

  object YamlReplacement extends Replacement {
    val replaceTable = Map(
      """%current_date%""" -> """<%= Time.now.to_s(:db) %>"""
    )
  }

  object SQLReplacement extends Replacement {
    val replaceTable = Map(
      """%current_date%""" -> """CURRENT_TIMESTAMP"""
    )
  }

  // Yaml
  describe ("Replace to Special Words then export .yml files.") {
    it ("non-special word") {
      val str = """non-special word"""
      assert(YamlReplacement.replaceSpecialWord(str) === str)
    }

    it ("special word: %current_date%") {
      val str = """%current_date%"""
      assert(YamlReplacement.replaceSpecialWord(str) === """<%= Time.now.to_s(:db) %>""")
    }
  }

  // SQL
  describe ("Replace to Special Words then export .sql files.") {
    it ("non-special word") {
      val str = """non-special word"""
      assert(SQLReplacement.replaceSpecialWord(str) === str)
    }

    it ("special word: %current_date%") {
      val str = """%current_date%"""
      assert(SQLReplacement.replaceSpecialWord(str) === """CURRENT_TIMESTAMP""")
    }
  }
}

