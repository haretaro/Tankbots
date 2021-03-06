package haretaro.tankbots.commons

import haretaro.tankbots.math.Vector2
import robocode._

/**
 * @author Haretaro
 * よく使う表現を集めたトレイト
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
  
  /**
   * @return 旋回できる角度の最大値 deg/turn
   */
  def maxRateOfRotation = 10 - 0.75 * math.abs(getVelocity)
  
  /**
   * @return フィールドの中心への相対ベクトル
   */
  def centralVector = Vector2(getBattleFieldWidth/2, getBattleFieldHeight/2) - currentPosition
}