package haretaro.tankbots.t1

import haretaro.tankbots.commons.Enemy
import haretaro.tankbots.math.{SecantMethod,Vector2}
import robocode.AdvancedRobot
import robocode.util.Utils

/**
 * @author Haretaro
 * 砲手
 */
trait Gunner extends AdvancedRobot with Commander{
  
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
   * @return 指定された位置ベクトルがフィールドの中かどうかを返す
   */
  def isInField(position:Vector2) = 
    0 <= position.x && position.x <= getBattleFieldWidth &&
    0 <= position.y && position.y <= getBattleFieldHeight
  
  /** ラフな線形予測射撃を行う */
  def roughLinerPrediction(target:Enemy, power:Double) = {
     targetAt(target.lastPosition + target.lastVelocity * (target.lastPosition - Vector2(getX,getY)).magnitude / (20 - 3 * power))
     fireTime = getTime + 1
     this.power = power
  }
  
  /**
   * 線形予測射撃
   */
  def linerPrediction(target:Enemy, power:Double) = {
    val myPosition = Vector2(getX,getY) + Vector2.fromTheta(getHeadingRadians,getVelocity)
    
    //ある時間tにおける敵との相対位置ベクトルの大きさ - 弾が移動した距離
    //したがってf(t) = 0 となる t で弾が当たる
    val f:Double => Double = t =>
      (target.linerPrediction(getTime,t.asInstanceOf[Int]) - myPosition).magnitude - (20 -3 * power) * t
    
    val timeOfColision = SecantMethod(f,0,1).answer
    timeOfColision match{
      case Some(t) => {
        val pointOfColision = target.linerPrediction(getTime, t.round.asInstanceOf[Int])
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