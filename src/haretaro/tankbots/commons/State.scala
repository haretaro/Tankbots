package haretaro.tankbots.commons

/**
 * @author Haretaro
 */
object State {
  //ref:http://xerial.org/scala-cookbook/recipes/2012/06/29/enumeration/
  case object searching extends State(0)
  case object aiming extends State(1)
}

sealed abstract class State(val n:Int)