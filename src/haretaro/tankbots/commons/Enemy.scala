package haretaro.tankbots.commons

import robocode._
import haretaro.tankbots.math.Vector2

/**
 * @author Haretaro
 */
case class Enemy(val name:String, var position:Vector2, var velocity:Vector2, var energy:Double){
  
  var timeLastUpdated:Long = 0
  
  /** @return 速度ベクトルが一定の場合の現在時刻からt時間後の未来位置 */
  def linerPrediction(now:Long, t:Int) = position + velocity * (now -timeLastUpdated + t)
  
}