package haretaro.tankbots.commons

/**
 * @author Haretaro
 */
object State {
  //ref:http://xerial.org/scala-cookbook/recipes/2012/06/29/enumeration/
  case object Searching extends State(0)
  case object Aiming extends State(1)
  
  val values = Array(Searching, Aiming)
  
  
}

sealed abstract class State(val n:Int)