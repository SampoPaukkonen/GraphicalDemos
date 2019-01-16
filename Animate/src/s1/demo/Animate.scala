package s1.demo

import scala.swing.SimpleSwingApplication
import scala.swing.Swing._ // This adds a few convenient shortcuts
import scala.swing.Frame
import scala.collection.mutable.Buffer
import scala.swing.MainFrame


/**
 * Animate inherits SimpleSwingApplication which makes
 * this a graphical user interface
 */
object Animate extends SimpleSwingApplication {

  // In this list you can blace all the effects you have
  // Effects are changed when the current effect returns true from it's
  // "next"-method
  val area = new DemoArea(Buffer(Droplet, Parallax))
  
  val top = new MainFrame() {
    preferredSize = (1200, 1200)    
    contents = area
  }

  top.visible = true
  
  // This sets the delay between frames
  area.startAnimating(10)
}


