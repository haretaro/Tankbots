package haretaro.tankbots.t1

import haretaro.tankbots.commons.Enemy
import haretaro.tankbots.math.Vector2
import robocode.AdvancedRobot
import robocode.util.Utils

/**
 * @author Haretaro
 * 砲手
 */
trait Gunner extends AdvancedRobot with Commander{
  
  private var fireTime:Long = 0
  private var power:Double = 2
  
  def reservedFire() = if(getTime == fireTime) fire(power)
  
  /**
   * 指定した場所に砲を向ける
   * @param point 位置ベクトル
   */
  def targetAt(point:Vector2) = {
    val direction = point - Vector2(getX,getY)
    setTurnGunRightRadians(Utils.normalRelativeAngle(direction.angle - getGunHeadingRadians))
  }
  
  def targetAt(x:Double, y:Double):Unit = targetAt(Vector2(x,y))
  
  /** ラフな線形予測射撃を行う */
  def roughLinerPrediction(target:Enemy, power:Double) = {
     targetAt(target.lastPosition + target.lastVelocity * (target.lastPosition - Vector2(getX,getY)).magnitude / (20 - 3 * power))
     fireTime = getTime + 1
     this.power = power
  }
}