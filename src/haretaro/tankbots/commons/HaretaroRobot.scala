package haretaro.tankbots.commons

import robocode._
import java.awt.Color
/**
 * @author Haretaro
 */
trait HaretaroRobot extends Robot{

  def paintStealth() =
    this match{
      case r:Robot => r.setAllColors(new Color(0,0,0))
    }
}