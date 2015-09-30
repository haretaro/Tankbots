package haretaro.tankbots.commons

import robocode._
import robocode.util.Utils
import haretaro.tankbots.math.Vector2

/**
 * @author Haretaro
 * 砲手
 */
trait Gunner extends AdvancedRobot with EnemyInfoManager{
  
  /**
   * 指定した場所に砲を向ける
   * @param point 位置ベクトル
   */
  def targetAt(point:Vector2):Unit = {
    val direction = point - Vector2(getX,getY)
    setTurnGunRightRadians(Utils.normalRelativeAngle(direction.angle - getGunHeadingRadians))
  }
  
  def targetAt(x:Double, y:Double):Unit = targetAt(Vector2(x,y))
  
  /** 線形予測射撃を行う */
  def linerPrediction(target:Enemy, power:Double) = 
    targetAt(target.position + target.velocity * (target.position - Vector2(getX,getY)).magnitude / (20 - 3 * power))
}