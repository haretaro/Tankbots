package haretaro.tankbots.t2

import haretaro.tankbots.commons.{Enemy, RoboUtil}
import haretaro.tankbots.math._
import haretaro.tankbots.t1._
import robocode.AdvancedRobot
import robocode.util.Utils

/**
 * @author Haretaro
 * 砲手
 */
trait CirclarGunner extends AdvancedRobot with Commander with RoboUtil{
  
  private var fireTime:Long = 0
  private var firePower:Double = 2
  private var fireAngle:Double = 0
  
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
    //ある時間tにおける敵との相対位置ベクトルの大きさ - 弾が移動した距離
    //したがってf(t) = 0 となる t で弾が当たる
    val f:Double => Option[Double] = t =>
      target.circlarPrediction(getTime,t.asInstanceOf[Int])
      .map(position => (position - futureLinerPosition(1)).magnitude - bulletSpeed(power) * t)
    
    val timeOfColision = OptionalSecantMethod(f,0,1).answer
    timeOfColision match{
      case Some(t) => {
        val pointOfColision = target.circlarPrediction(getTime, t.round.asInstanceOf[Int]).get
        if(isInField(pointOfColision)){
          orderFire(pointOfColision,power)
        }
      }
      case _ => ()
    }
  }
  
  /**
   * 射撃を予約する
   */
  def orderFire(point:Vector2, power:Double) = {
    val direction = point - currentPosition
    setTurnGunRightRadians(Utils.normalRelativeAngle(direction.angle - getGunHeadingRadians))
    fireTime = getTime +1
    firePower = power
    fireAngle = Utils.normalAbsoluteAngle(direction.angle)
  }
  
  /**
   * 砲塔の旋回が終わっている場合に予約された射撃を実行する
   */
  def executeFire = {
    val gunAngle = Utils.normalAbsoluteAngleDegrees(getGunHeadingRadians)
    if(getTime == fireTime && fireAngle == gunAngle) setFire(firePower)
    println((math.toDegrees(fireAngle),math.toDegrees(gunAngle)))
  }
}