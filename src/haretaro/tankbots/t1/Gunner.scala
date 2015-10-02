package haretaro.tankbots.t1

import robocode._
import robocode.util.Utils
import haretaro.tankbots.math._
import haretaro.tankbots.commons._

/**
 * @author Haretaro
 * 砲手
 */
trait Gunner extends AdvancedRobot with EnemyInfoManager{
  
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
    println(getTime - target.timeLastUpdated)
     targetAt(target.position + target.velocity * (target.position - Vector2(getX,getY)).magnitude / (20 - 3 * power))
     fireTime = getTime + 1
     this.power = power
  }
}