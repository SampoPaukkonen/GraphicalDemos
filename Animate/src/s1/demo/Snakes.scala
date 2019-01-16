package s1.demo

import java.awt.image.BufferedImage
import java.awt.BasicStroke
import s1.image.ImageExtensions._
import scala.collection.mutable.Queue
import java.awt.Font
import java.awt.Color._

class Circle(val x: Int, val y: Int)

/**
 * The idea for this effect came from Felix Bade.
 * 
 * The effect draws a continuous stream of filled
 * circles that changes it's course randomly.
 */
object Snakes extends Effect(500, 500) {
  
    // This variable could hold a background image if wanted
    //  See [[DIssolve]] for an exampleon how to load image files
    val background: Option[BufferedImage] = None      
    
    // The circles we draw are in a queue.The latest is
    // always stored in [[last]]
    var last        = new Circle(100, 100)
    val circles     = Queue[Circle](last)
    var direction   = 0.0 
    val step        = 10
    var queueLength = 1
    
    val random = new util.Random
    
    def changeThings() = {
      // Change the direction where to draw the next circle randomly
      direction = direction + (random.nextDouble - 0.5);
      
      // Calculate the new coordinates 
      val xdiff = (math.cos(direction) * step).toInt
      val ydiff = (math.sin(direction) * step).toInt
      
      val x = (last.x + xdiff + 500) % 500
      val y = (last.y + ydiff + 500) % 500
      last  = new Circle(x,y)
      
      // Store the circle in a queue (Think of a buffer where you add in one end and take stuff out from the other)
      circles.enqueue( last )
      queueLength += 1
      
      // If the queue gets big, we delete older circles for a fun effect
      while (queueLength > 600) {
        circles.dequeue()
        queueLength -= 1
      }
    }
    
    //------- drawing -------//
    
    // Thick and thin line widths
    val thick = new BasicStroke(3)
    val thin  = new BasicStroke(1)
    
    // Nice font for the christmas message
    val christmasFont  = new Font("Times", Font.BOLD, 60)
    
    override def makePic(): BufferedImage = {
      val pic = emptyImage
      val g = pic.graphics
      
      // If there was a background we could paint it here
      // The parameters of drawImage are picture, x, y and an ImageObserver which we in this case leave null.
      background foreach (image => g.drawImage(image, 0, 0, null))
      
      // Draw all the circles.
      
      // For more ideas on drawing see:
      //   https://docs.oracle.com/javase/7/docs/api/java/awt/Graphics.html
      //   https://docs.oracle.com/javase/7/docs/api/java/awt/Graphics2D.html
      //   https://docs.oracle.com/javase/tutorial/2d/basic2d/index.html   
      for (c <- circles) {
        g.setColor(WHITE)
        g.setStroke(thin)
        g.fillOval(c.x - 20, c.y - 20, 40, 40)
        
        g.setStroke(thick)
        g.setColor(BLACK)
        g.drawOval(c.x - 20, c.y - 20, 40, 40)
      }
      g.setFont(christmasFont)

      // Notice the fake "shadow" under the text
      g.setColor(BLACK)
      g.drawString("Hyvää joulua!", 22, 42)

      g.setColor(GREEN)
      g.drawString("Hyvää joulua!", 20, 40)
      
      pic
    }
    
    // Effects can also receive information on mouse movements.
    // When a mouse goes to ne coordinates this method gets called.
    
    // We use it to draw still more circles at the mouse location
    override def mouseAt(x: Int, y: Int) = circles enqueue new Circle(x,y)        
 
    // This effect will never end
    def next = false
  }
