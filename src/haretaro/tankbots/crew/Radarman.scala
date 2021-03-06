package haretaro.tankbots.crew

import haretaro.tankbots.commons.RoboUtil
import haretaro.tankbots.math.Vector2
import robocode._
import robocode.util.Utils

/**
 * @author Haretaro
 * レーダー手
 */
trait Radarman extends AdvancedRobot with Commander with RoboUtil{
  
  /** 指定した場所の辺りを見る */
  def lookAt(point:Vector2) = {
    val direction = point - currentPosition
    val angle = Utils.normalRelativeAngleDegrees(direction.angleDegrees - getRadarHeading)
    val ang:Double = angle match{
      case a if a<0 => a - 22.5
      case a if a>=0 => a + 22.5
    }
    setTurnRadarRight(ang)
  }
  
  /** レーダーを動かす */
  def radar = {
    getTime match{
      case t if getOthers > 1 && (t/10)%4==0 || enemies.size == 0 => setTurnRadarRight(45)
      case _ => nearestEnemy.map(_.lastPosition).orElse(Option(Vector2(0,0))).map(p => lookAt(p))
    }
  }
}