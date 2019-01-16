package s1.demo
import s1.image.ImageExtensions._
import java.awt.Color._
import java.awt.BasicStroke
import scala.util.Random

/**
 * This simple effect features a bouncing ball
 */
object BouncingBall extends Effect(500, 500){
  
  // The ball has coordinates and speed
  class Ball(val x: Int, val y:Int, val xSpeed: Int, val ySpeed: Int)
 
  // ...and here is our initial instance
  var ball = new Ball(200, 200, 10, 8)
  
  val random = new Random
  var clock  = 0 
  
  /**
   * Here we draw a BufferedImage on the current state of the [[Effect]]
   */
  def makePic() = {
    // Get an empty space where to draw 
    val pic      = emptyImage
    
    // Get the tools to draw with
    val graphics = pic.graphics

    // We can pick a random color
    val randomColor = new java.awt.Color(random.nextInt(256),random.nextInt(256),40 + random.nextInt(216), 255)

    // And then use it to fill an oval
    graphics.setColor(randomColor)
    graphics.fillOval(ball.x, ball.y, 40, 40)
      
    // Finally we return the picture we created.
    pic
  }
  
  /**
   * Here we modify the state (the position and speed of the ball)
   */
  def changeThings() = {
    clock += 1 
    
    val y = ball.y + ball.ySpeed
    val x = ball.x + ball.xSpeed
    
    val (nextY, nextYSpeed) = 
      if ( y > height) {
        (height - (y-height), -ball.ySpeed)
      } else if (y < 0)
        (-y, -ball.ySpeed)
      else
        (y, ball.ySpeed)
    
    val (nextX, nextXSpeed) = 
      if ( x > width) {
        (width - (x-width), -ball.xSpeed)
      } else if (x < 0)
        (-x, -ball.xSpeed)
      else
        (x, ball.xSpeed)
    
    // We could have done this with a ball with it's coordinates in var's
    // It can also be done in a more functional way, replacing the ball
    // itself
    ball = new Ball(nextX, nextY, nextXSpeed, nextYSpeed)
    
  }
  
  def next = clock > 300
}