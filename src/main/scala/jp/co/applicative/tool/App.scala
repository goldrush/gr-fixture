package jp.co.applicative.tool

import java.io.File
import java.nio.channels.FileChannel
import java.io.FileInputStream
import java.io.FileOutputStream
import scala.swing._
import scala.swing.event._

object App extends SimpleSwingApplication {

  def top = new MainFrame {
    title = "Fixtures Creater"
    minimumSize = new Dimension(600, 100)
    centerOnScreen
    contents = new BoxPanel(Orientation.Vertical) {
      contents += combo
      contents += inPanel
      contents += outDirPanel
      contents += outPanel
      contents += button
    }
    updatePath(combo.selection.item)
  }

  def inPanel: BoxPanel = new BoxPanel(Orientation.Horizontal) {
    contents += new Label("In Path :")
    contents += inPathText
  }

  def outDirPanel: BoxPanel = new BoxPanel(Orientation.Horizontal) {
    contents += new Label("Out Dir :")
    contents += outDirText
  }

  def outPanel: BoxPanel = new BoxPanel(Orientation.Horizontal) {
    contents += new Label("Out Path:")
    contents += outPathText
  }

  val combo = new ComboBox(Array("init", "develop", "test")) {
    listenTo(selection)
    reactions += {
      case e: SelectionChanged => updatePath(selection.item)
    }
  }

  val button = new Button {
    text = "Create"
    reactions += {
      case e: ButtonClicked => execute(inPathText.text, outPathText.text)
    }
  }

  val appPath = System.getProperty("user.dir")
  val inPathText = new TextField("")
  val outDirText = new TextField(joinPath((new File((new File(appPath)).getParent())).getParent(), "Base")) {
    reactions += {
      case e: ValueChanged => updatePath(combo.selection.item)
    }
  }
  val outPathText = new TextField("")

  def updatePath(str: String): Unit = {
    inPathText.text = joinPath(appPath, str) + ".xls"
    outPathText.text = str match {
      case "init" => joinPath(outDirText.text, "fixtures", "init")
      case "develop" => joinPath(outDirText.text, "fixtures", "develop")
      case "test" => joinPath(outDirText.text, "test", "fixtures")
      case _ => outDirText.text
    }
  }

  def execute(inPath: String, outPath: String) = {
    try {
      checkInPath(inPath)
      checkOutPath(outPath)
      FixtureCreator.create(inPath, outPath)
      Dialog.showMessage(message = "fixturesを[" + outPath + "]に作成しました。")
    } catch {
      case e: Exception => Dialog.showMessage(message = e.getMessage())
    }
  }

  def checkInPath(inPath: String) = {
    if ((new File(inPath)).exists() == false) {
      throw new IllegalArgumentException("ファイルが存在しません。" + inPath)
    }
  }

  def checkOutPath(outPath: String) = {
    val file = new File(outPath)
    if (file.exists() == false) {
      file.mkdirs()
    }
  }

  def joinPath(pathes: String*): String = pathes.foldLeft("") { (s, p) => (new File(s, p)).getPath() }
}