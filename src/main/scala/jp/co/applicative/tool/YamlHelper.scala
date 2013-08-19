package jp.co.applicative.tool

import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import java.io.StringWriter
import java.util.LinkedHashMap
import org.yaml.snakeyaml.emitter.ScalarAnalysis
import org.yaml.snakeyaml.resolver.Resolver

object YamlHelper {

  def dump(map: LinkedHashMap[String, Object]): String = {

    if (map.isEmpty()) return ""

    val option = new DumperOptions
    option.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN)
    option.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK)
    option.setLineBreak(DumperOptions.LineBreak.getPlatformLineBreak())
    option.setAllowUnicode(true)
    val yaml = new Yaml(option)
    val writer: StringWriter = new StringWriter
    yaml.dump(map, writer)
    writer.toString().replaceAll("'", "") //nownow
  }

}