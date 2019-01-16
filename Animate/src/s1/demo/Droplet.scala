package s1.demo
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.awt.Image
import java.io.File
import s1.image.ImageExtensions._
import s1.image.ImageExtensions._
import java.awt.Color._
import java.awt.Color
import java.awt.BasicStroke
import scala.util.Random
import o1._
import scala.math._
import java.awt.geom.Area
import java.awt.Polygon

object Droplet extends Effect(1200, 1200) {
  var clock = 0
  case class Pos(x: Double, y: Double)
  //Below will be all the frames used in the animation
  val frame1 = ImageIO.read(new File("pictures/Frame1.png"))
  val frame2 = ImageIO.read(new File("pictures/Frame2.png"))
  val frame3 = ImageIO.read(new File("pictures/Frame3.png"))
  val frame4 = ImageIO.read(new File("pictures/Frame4.png"))
  val frame5 = ImageIO.read(new File("pictures/Frame5.png"))
  val frame6 = ImageIO.read(new File("pictures/Frame6.png"))
  val frame7 = ImageIO.read(new File("pictures/Frame7.png"))
  var tempFrames = Buffer(frame1, frame2, frame3, frame4, frame5, frame6, frame7)
  
  //Here we scale the images to some size
  var frames = tempFrames.map(z => z.getScaledInstance(50, 50, Image.SCALE_DEFAULT))
  var currentImage = frames.head
  var sinceLast = 0
  
  //Drop will be used to represent  the falling drop. It is used the change frames
  //and store location of the drop
  case class Drop(var position: Pos, var frames: Buffer[java.awt.Image] = frames) {
    var currentVelocity = 0.09
    val acceleration = 0.085
    var sinceLast = 0
    def currentFrame = frames.head
    def changeFrame  = if (frames.size > 1) frames = frames.tail else frames
    def pop = frames = frames.takeRight(1)

    /*
     *By default accelerates the current velocity by one tick
     */
    def accelerate = currentVelocity = currentVelocity + 1 * acceleration
  }

  val droplets = Array.fill[Drop](50)(Drop(Pos(50 + Random.nextInt(width - 50), Random.nextInt(height/3))))
  
  
  //isNear method should not work. 
  def isNear(thisDrop: Drop, anotherDrop: Drop) = {
    pow(50/2, 2) >= pow((thisDrop.position.x - anotherDrop.position.x), 2) + pow((thisDrop.position.y - anotherDrop.position.y),2)
  }
  
  def next = clock > 1600
  
  //In changeThings, if the drops are at a certain height we start to "pop" them by
  //changing the current frame. SinceLast is used to create the effect of the popping
  //i.e. so user has time to notice the difference
  def changeThings() = {
    clock += 1
    for (i <- droplets.indices) {
      val drop = droplets(i)
      if (drop.position.y > (height/2.5) && drop.sinceLast <= 0) {
        drop.changeFrame
        drop.sinceLast = 10
      }
      if (drop.frames.size == 1 ) {
        droplets(i) = Drop(Pos(50 + Random.nextInt(width - 50), Random.nextInt(height/3)))
      }
      else {
        drop.position = Pos(drop.position.x.toInt, drop.position.y.toInt + drop.currentVelocity)
        drop.accelerate
        drop.sinceLast -= 1
        
      }   
    }
  }
  
  def makePic() = {
    val pic = emptyImage
    val graphics = pic.graphics
    graphics.setColor(Color.BLACK)
    graphics.fillRect(0, 0, width, height)
    droplets.foreach(drop => graphics.drawImage(
        drop.currentFrame, drop.position.x.toInt, drop.position.y.toInt, null))
        
    pic
  }
}