package haretaro.tankbots.t2

import haretaro.tankbots.commons.{Enemy, RoboUtil}
import haretaro.tankbots.math.{SecantMethod, Vector2}
import haretaro.tankbots.t1._
import robocode.AdvancedRobot
import robocode.util.Utils

/**
 * @author Haretaro
 * 砲手
 */
trait CirclarGunner extends AdvancedRobot with Commander with RoboUtil{
  
  private var fireTime:Long = 0
  private var power:Double = 2
  
  /**
   * 予約が入ってる場合に射撃を行う
   */
  def reservedFire() = if(getTime == fireTime) fire(power)
  
  /**
   * 指定した場所に砲を向ける
   * @param point 位置ベクトル
   */
  def targetAt(point:Vector2) = {
    val direction = point - Vector2(getX,getY)
    setTurnGunRightRadians(Utils.normalRelativeAngle(direction.angle - getGunHeadingRadians))
  }
  
  /**
   * 指定した場所に砲を向ける
   */
  def targetAt(x:Double, y:Double):Unit = targetAt(Vector2(x,y))
  
  /**
   * 円形予測射撃
   */
  def circlarPrediction(target:Enemy, power:Double):Unit = {
    //TODO: 円形予測ができない場合に対応させる
    if (target.historySize < 2) return
    
    //ある時間tにおける敵との相対位置ベクトルの大きさ - 弾が移動した距離
    //したがってf(t) = 0 となる t で弾が当たる
    val f:Double => Double = t =>
      (target.circlarPrediction(getTime,t.asInstanceOf[Int]).get - futureLinerPosition(1)).magnitude - bulletSpeed(power) * t
    
    val timeOfColision = SecantMethod(f,0,1).answer
    timeOfColision match{
      case Some(t) => {
        val pointOfColision = target.circlarPrediction(getTime, t.round.asInstanceOf[Int]).get
        if(isInField(pointOfColision)){
          targetAt(pointOfColision)
          fireTime = getTime + 1
          this.power = power
        }
      }
      case _ => ()
    }
  }
}