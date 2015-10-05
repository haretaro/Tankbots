package haretaro.tankbots.t1


import haretaro.tankbots.commons._
import haretaro.tankbots.math._
import robocode._
import robocode.util.Utils
import haretaro.tankbots.t1.Commander

/**
 * @author Haretaro
 * レーダー手
 */
trait Radarman extends AdvancedRobot with Commander{
  
  /** 指定した場所の辺りを見る */
  def lookAt(point:Vector2) = {
    val direction = point - Vector2(getX,getY)
    val angle = Utils.normalRelativeAngleDegrees(direction.angleDegrees - getRadarHeading)
    val ang:Double = angle match {
      case a if a<0 => a - 10
      case a if a>0 => a + 10
      case a if(a==0 && getTime%2==0) => a + 10
      case a => a - 10
    }
    setTurnRadarRight(ang)
  }
  
  /** レーダーを動かす */
  def radar = {
    getTime match{
      case t if getOthers > 1 && (t/10)%2==0 || enemies.size == 0 => setTurnRadarRight(45)
      case _ => nearestEnemy.map(_.position).orElse(Option(Vector2(0,0))).map(p => lookAt(p))
    }
  }
}