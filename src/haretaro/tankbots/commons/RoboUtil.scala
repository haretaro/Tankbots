package haretaro.tankbots.commons

import haretaro.tankbots.math.Vector2
import robocode._

/**
 * @author Haretaro
 */
trait RoboUtil extends AdvancedRobot{
  
  /**
   * @return このロボットの現在位置
   */
  def currentPosition = Vector2(getX,getY)
  
  /**
   * @return このロボットが等速直線運動した場合のtime時間後の位置
   */
  def futureLinerPosition(time:Int) =
    currentPosition + Vector2.fromTheta(getHeadingRadians,getVelocity) * time
  
  /**
   * @return 指定された位置ベクトルがフィールドの中かどうかを返す
   */
  def isInField(position:Vector2) = 
    0 <= position.x && position.x <= getBattleFieldWidth &&
    0 <= position.y && position.y <= getBattleFieldHeight
  
  /**
   * @return 砲弾の速さ
   */
  def bulletSpeed(power:Double) = 20 - 3 * power
}