package jp.co.applicative.tool

import java.io.File
import java.nio.channels.FileChannel
import java.io.FileInputStream
import java.io.FileOutputStream

object App {

  def main(args: Array[String]): Unit = {

    //input check
    args.length match {
      case 0 => throw new IllegalArgumentException("第一引数にFixtureファイルのパスを指定してください。")
      case _ =>
    }

    val inputFile = new File(args(0))
    if (!inputFile.exists()) {
      throw new IllegalArgumentException("ファイルが存在しません。" + inputFile.getName())
    }

    inputFile.getPath() match {
      case f if f.endsWith("xls") =>
      case f if f.endsWith("xlsx") =>
      case _ => throw new IllegalArgumentException("Excelファイルではありません。" + inputFile.getName())
    }

    val outPath = inputFile.getCanonicalPath().lastIndexOf(".") match {
      case i if i > 0 => inputFile.getCanonicalPath().substring(0, i)
      case _ => inputFile.getCanonicalPath()
    }

    val outDirectory = new File(outPath)
    outDirectory match {
      case o if o.exists() == false => {
        throw new IllegalArgumentException("出力先フォルダが存在しません。" + outDirectory.getPath())
      }
      case o if o.isDirectory() == false => {
        throw new IllegalArgumentException("出力先がフォルダではありません。" + outDirectory.getPath())
      }
      case _ =>
    }

    //Fixture Create
    println(f"fixturesを[${outPath}]に作成します。")
    FixtureCreator.create(inputFile.getPath(), outPath)
    println("fixturesを作成しました。")

    //end process
    val basePath = outDirectory.getParentFile().getParentFile().getParentFile().getCanonicalPath
    println(basePath)
    val copyPath = outDirectory.getName() match {
      case "develop" => joinPath(basePath, "Base", "fixtures", "develop")
      case "init" => joinPath(basePath, "Base", "fixtures", "init")
      case "test" => joinPath(basePath, "Base", "test", "fixtures")
      case _ => ""
    }
    if (copyPath != "") waitUserInput(outPath, copyPath)

  }

  def waitUserInput(outPath: String, copyPath: String): Unit = {
    print(f"\nfixturesを[${copyPath}]にコピーしますか？(y/n):")
    readChar match {
      case 'y' => {
        copyFixture(outPath, copyPath)
        println("コピーしました。")
      }
      case 'n' => println("終了します。")
      case _ => waitUserInput(outPath, copyPath)
    }
  }

  def copyFixture(fromDirPath: String, toDirPath: String) = {
    val fromDir = new File(fromDirPath)
    fromDir.listFiles.foreach(f => {
      val inputChannel = (new FileInputStream(f)).getChannel
      val outputChannel = new FileOutputStream(toDirPath + "/" + f.getName())
      outputChannel.getChannel.transferFrom(inputChannel, 0, inputChannel.size)
    })
  }

  def joinPath(pathes: String*): String = pathes.foldLeft("") { (s, p) => (new File(s, p)).getPath() }
}