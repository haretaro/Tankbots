package haretaro.tankbots.t1


import haretaro.tankbots.commons._
import haretaro.tankbots.math._
import java.awt.Color
import robocode._
import robocode.util.Utils
import haretaro.tankbots.t1.Commander

/**
 * @author Haretaro
 * 運転手
 */
trait Driver extends AdvancedRobot with Commander with GraphicalDebugger{
  
  /** 重力のデバッグ描画用 (始点,終点) */
  private var gravity = (Vector2(0,0),Vector2(0,0))
  
  /** イベントハンドラーを登録する */
  def initDriver = addOnPaintEventHandler(g => drawLine(g,Color.green,gravity._1,gravity._2))
  
  /** 重力移動 */
  def gravityDrive = {
    val position = Vector2(getX,getY)
    val center = Vector2(getBattleFieldWidth/2,getBattleFieldHeight/2)
    val enemyVectors = enemies
      .map(position - _.linerPrediction(getTime,1))//敵との相対ベクトルをそれぞれ求めて
      .map(r => r * 10000/math.pow(r.magnitude,2))//距離の２乗に反比例した大きさにする
    val enemyVector = enemyVectors match{
      case v if v.isEmpty => (center - position) / 100 //敵の座標がわからなくてベクトルが定まらない場合はマップの中心に向かう
      case v => v.reduceLeft(_+_)
    }
    
    val wallVector = (center - position) * (center - position).magnitude /300
    val gravity = enemyVector + wallVector //中心に向かうベクトルを加算して壁にぶつからないようにする
    
    val angle = Utils.normalRelativeAngle(gravity.angle - getHeadingRadians)
    
    val speed = angle match{
      case a if a < -math.Pi/2 || math.Pi/2 < a => 4 + Utils.getRandom.nextInt(5)
      case _ => 8
    }
    
    setTurnRightRadians(angle)
    setMaxVelocity(speed)
    setAhead(1000)
    this.gravity = (position, gravity + position)
  }
  
  /**
   * 指定の場所に移動する
   * @param destination 目的地の位置ベクトル
   */
  def goTo(destination:Vector2):Unit = {
    val direction = destination - Vector2(getX,getY)
    turnRightRadians(Utils.normalRelativeAngle(direction.angle - getHeadingRadians))
    ahead(direction.magnitude)
  }
  
  /**
   * 指定の場所に移動する
   * @param x,y 目的地の座標
   */
  def goTo(x:Double,y:Double):Unit = goTo(Vector2(x,y))
}