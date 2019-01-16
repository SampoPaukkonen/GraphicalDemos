package s1.demo
import s1.image.ImageExtensions._
import java.awt.Color._
import java.awt.Color
import java.awt.BasicStroke
import scala.util.Random
import scala.math._

case class Pos(x: Double, y: Double)

object Parallax extends Effect(1800, 1800) {
  
  var clock = 0

  class Star(val radius: Int) {
    private def extraSpeed: Int = Random.nextInt((3)) //Is used so that not all objects go the same speed
    private var speed: Int = (max((radius - 3), 1)).toInt + this.extraSpeed
    private var currentPos = this.randomLaunchPosition    
    private def distanceToOrigin = sqrt(this.currentPos.x * this.currentPos.x + this.currentPos.y * this.currentPos.y)

    def yCoordinate = this.currentPos.y
    def xCoordinate = this.currentPos.x
    /*
     * The stars are supposed to go to the lower left corner so we need to 
     * decrease the x coordinates.
     */
    def newXCoordinate = this.xCoordinate - speed 
    def newYCoordinate(newX: Double) = {
      val r = distanceToOrigin
      sqrt((r * r) - (newX * newX) ) 
    }
    
    private def randomLaunchPosition() = {
      new Pos(Random.nextInt(width), Random.nextInt(height))
    }
    def setPos(x: Int, y: Int) = this.currentPos = new Pos(x, y)
    //The stars my by calling the newXCoordinate and newYCoordinate respectively.
    //The position of the star is then changed accordingly.
    def move() = {
      val oldX = this.pos.x
      val newX = newXCoordinate
      this.currentPos = new Pos(newX, newYCoordinate(newX))
    }
    def pos = this.currentPos
    def spesificPos(x: Int, y: Int) = new Pos(x, y)
  }
  
  object Star {
    def apply(radius: Int): Star = new Star(radius)
  }
  /*
   * StarCollection is used with Star to create a bunch of stars at will.
   */
  object StarCollection {
    def dozenStars(amount: Int): Array[Star] = Array.fill[Star](amount)(Star(Random.nextInt(6)))
  }

  var stars = StarCollection.dozenStars(1200)

  def makePic() = {
    val pic = emptyImage
    val graphics = pic.graphics
    graphics.setColor(Color.BLACK)
    graphics.fillRect(0, 0, width, height)
    graphics.setColor(Color.WHITE)
    stars.foreach(star => graphics.fillOval((star.pos.x).toInt, (star.pos.y).toInt, star.radius, star.radius))
    pic
  }
  //In changeThings, if the star is out of the picture, we then create a new star
  //at a random place to replace the star in question.
  def changeThings(): Unit = {
    
  clock += 1
    for (i <- stars.indices) {
      var star = stars(i)
      star.move()
      var starY = star.yCoordinate
      var starX = star.xCoordinate
      if (starY > height || starX > width || starX < 0.1) {
        stars(i) = new Star(star.radius)
        stars(i).setPos(stars(i).xCoordinate.toInt, Random.nextInt(1))
      }
    }
  }
  
  def next = {
    clock > 1000
  }

}