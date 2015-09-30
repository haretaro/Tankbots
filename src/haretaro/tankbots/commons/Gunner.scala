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
  def linerPrediction(target:Enemy) = {
    val power = 2.0
    def predictionOfHit = {
      var bulletPosition = Vector2(getX,getY)
      val bulletVelocity = Vector2.fromTheta(getGunHeadingRadians,20-3*power)
      var enemyPosition = target.position
      
      for(i <- 1 to 20){
        bulletPosition += bulletVelocity
        enemyPosition += target.velocity
      }
    }
  }
}