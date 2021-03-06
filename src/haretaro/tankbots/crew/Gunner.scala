package haretaro.tankbots.crew

import java.awt.Color

import haretaro.tankbots.commons._
import haretaro.tankbots.crew._
import haretaro.tankbots.math._
import robocode.AdvancedRobot
import robocode.util.Utils

/**
 * @author Haretaro
 * 砲手
 */
trait Gunner extends AdvancedRobot with Commander with RoboUtil with GraphicalDebugger{
  
  private var fireTime:Long = 0
  private var firePower:Double = 2
  private var fireAngle:Double = 0
  
  private var pointOfCol = Vector2(0,0)
  private var timeOfCol = 0l
  private var target = ""
  private var firePos = Vector2(0,0)
  
  def initGunner = addOnPaintEventHandler(g=>{
    drawRect(g,Color.orange,pointOfCol - Vector2(16,16), 32, 32)
    drawLine(g, Color.orange, firePos, pointOfCol)
    drawRect(g, Color.red, firePos + (pointOfCol - firePos).normalized * bulletSpeed(firePower) * (getTime - fireTime) - Vector2(6,6), 12, 12)
  })
  
  def updateGunner = if(timeOfCol == getTime){
    enemies.find(_.name == target).foreach(e => println((e.lastPosition - pointOfCol).magnitude))
    println("bullet error = " + (firePos + (pointOfCol - firePos).normalized * bulletSpeed(firePower) * (getTime - fireTime) - pointOfCol).magnitude)
    println("")
  }
  
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
  
  /** ラフな線形予測射撃を行う */
  def roughLinerPrediction(target:Enemy, power:Double) = {
     orderFire(target.lastPosition + target.lastVelocity * (target.lastPosition - Vector2(getX,getY)).magnitude / bulletSpeed(power),power)
  }
  
  /**
   * 線形予測射撃
   */
  def linerPrediction(target:Enemy, power:Double) = {
    //ある時間tにおける敵との相対位置ベクトルの大きさ - 弾が移動した距離
    //したがってf(t) = 0 となる t で弾が当たる
    val f:Double => Double = t =>
      (target.linerPrediction(getTime,t.asInstanceOf[Int]) - futureLinerPosition(1)).magnitude - bulletSpeed(power) * (t-1)
    
    val timeOfColision = SecantMethod(f,0,1).answer
    timeOfColision match{
      case Some(t) => {
        val pointOfColision = target.linerPrediction(getTime, t.round.asInstanceOf[Int])
        if(isInField(pointOfColision)){
          orderFire(pointOfColision,power)
        }
      }
      case _ => ()
    }
  }
  
  /**
   * 円形予測射撃
   */
  def circlarPrediction(target:Enemy, power:Double, futurePosition:Vector2 = futureLinerPosition(1)):Unit = {
    //ある時間tにおける敵との相対位置ベクトルの大きさ - 弾が移動した距離
    //したがってf(t) = 0 となる t で弾が当たる
    //弾は一秒後に発射されることに注意
    val f:Double => Option[Double] = t =>
      target.circlarPrediction(getTime,t)
      .map(enemyPosition => (enemyPosition - futurePosition).magnitude - bulletSpeed(power) * (t-1))
    
    val timeOfColision = OptionalSecantMethod(f,0,1).answer
    timeOfColision match{
      case Some(t) => {
        target.circlarPrediction(getTime,t) match{
          case Some(pointOfColision) => {
            if(isInField(pointOfColision)) orderFire(pointOfColision,power,futurePosition)
            pointOfCol = pointOfColision
            timeOfCol = getTime + t
            this.target = target.name
            firePos = futurePosition
          }
          case _ => ()
        }
      }
      case _ => ()
    }
  }
  
  /**
   * 射撃を予約する
   */
  def orderFire(point:Vector2, power:Double, futurePosition:Vector2 = futureLinerPosition(1)) = {
    val direction = point - futurePosition
    setTurnGunRightRadians(Utils.normalRelativeAngle(direction.angle - getGunHeadingRadians))
    fireTime = getTime +1
    firePower = power
    fireAngle = Utils.normalAbsoluteAngle(direction.angle)
  }
  
  /**
   * 砲塔の旋回が終わっている場合に予約された射撃を実行する
   */
  def executeFire = {
    //自分の位置の予測と実際の位置の誤差
    val gunAngle = Utils.normalAbsoluteAngleDegrees(getGunHeadingRadians)
    if(getTime == fireTime && fireAngle == gunAngle) setFire(firePower)
  }
}