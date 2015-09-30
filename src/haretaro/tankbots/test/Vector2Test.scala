package haretaro.tankbots.test

import haretaro.tankbots.commons._
import haretaro.tankbots.math.Vector2

/**
 * @author Haretaro
 */
object Vector2Test extends App{
  val vector = Vector2(1,2)
  println(vector)
  
  val vector2 = Vector2.fromTheta(1,math.Pi/2)
  println(vector2)
  
  println(vector + vector2)
  println(vector2.angleDegrees)
}