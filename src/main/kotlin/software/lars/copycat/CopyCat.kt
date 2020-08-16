package software.lars.copycat

import java.awt.Point
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JTextField

val systemClipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
var clipboard: String
    get() = systemClipboard.getData(DataFlavor.stringFlavor) as String
    set(value) = systemClipboard.setContents(StringSelection(value), null);

fun main() {
    val frame = JFrame().apply {
        location = Point(0, 0)
        isAlwaysOnTop = true
        setSize(500, 500) //400 width and 500 height
        layout = null //using no layout managers
        toFront()
    }

    frame.addCopyCats("""{{"$1" | translate:lang}}""", """this.translate("$1")""")


    val sheet = JTextField().apply {
        setBounds(100, 50, 250, 30)
        frame.add(this)
    }
    JButton("LanguagePack").apply {
        setBounds(100, 80, 250, 50)
        addActionListener {
            clipboard = clipboard.split('\n').dropLast(1).joinToString("\n") { line ->
                "${sheet.text}." + line.split(' ', '-')
                    .joinToString("") { it.toLowerCase().capitalize() }
                    .filter { it in 'A'..'Z' || it in 'a'..'z' }
            }
        }
        frame.add(this)
    }

    frame.isVisible = true //making the frame visible
}

private fun JFrame.addCopyCats(vararg defaultText: String) =
    (1..defaultText.size).forEach { addCopyCat(it, defaultText[it - 1]) }

private fun JFrame.addCopyCat(count: Int = 1, defaultText: String = "") {
    val text = JTextField().apply {
        text = defaultText
        setBounds(100, 125 * count + 50, 250, 30)
        this@addCopyCat.add(this)
    }
    JButton("CopyCat").apply {
        setBounds(100, 125 * count + 80, 250, 50)
        addActionListener {
            clipboard = text.text.replace("$1", clipboard.trim('\n'))
        }
        this@addCopyCat.add(this)
    }
}
