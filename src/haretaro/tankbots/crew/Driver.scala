package haretaro.tankbots.crew

import java.awt.Color
import haretaro.tankbots.commons._
import haretaro.tankbots.crew._
import haretaro.tankbots.math.Vector2
import robocode.AdvancedRobot
import robocode.util.Utils

/**
 * @author Haretaro
 * 運転手
 */
trait Driver extends AdvancedRobot with Commander with GraphicalDebugger with RoboUtil{
  
  /** 重力のデバッグ描画用 (始点,終点) */
  private var gravity = (Vector2(0,0),Vector2(0,0))
  
  private var _nextPosition = Vector2(0,0)
  private var timeLastUpdated = 0l
  
  /** グラフィカルデバッグ用のイベントハンドラーを登録する */
  def initDriver = addOnPaintEventHandler(g => drawLine(g,Color.green,gravity._1,gravity._2))
  
  /** 重力移動 */
  def gravityDrive = {
    val position = Vector2(getX,getY)
    val center = Vector2(getBattleFieldWidth/2,getBattleFieldHeight/2)
    val enemyVectors = enemies
      .map(position - _.linerPrediction(getTime,1))//敵との相対ベクトルをそれぞれ求めて
      .map(r => r * 100000/math.pow(r.magnitude,2))//距離の２乗に反比例した大きさにする
    val enemyVector = enemyVectors match{
      case v if v.isEmpty => (center - position) / 100 //敵の座標がわからなくてベクトルが定まらない場合はマップの中心に向かう
      case v => v.reduceLeft(_+_)/getOthers
    }
    
    val wallVector = (center - position) * 0.0001 * math.pow((center-position).magnitude,2)
    val gravity = enemyVector + wallVector //中心に向かうベクトルを加算して壁にぶつからないようにする
    
    val angle = Utils.normalRelativeAngle(gravity.angle - getHeadingRadians)
    
    val speed = angle match{
      case a if a < -math.Pi/2 || math.Pi/2 < a => 4 + Utils.getRandom.nextInt(5)
      case _ => 8
    }
    
    setTurnRightRadians(angle)
    setMaxVelocity(speed)
    setAhead(100)
    val nextSpeed = getVelocity match{
      case v if speed > v && v + 1 <= speed => v + 1
      case v if v + 1 > speed => speed
      case v if v < speed && v - 2 >= speed => v - 2
      case v if v -2 < speed => speed
    }
    val nextAngle = math.toDegrees(angle) match{
      case ang if math.abs(ang) < maxRateOfRotation => getHeading + ang
      case ang if ang >= maxRateOfRotation => getHeading + maxRateOfRotation
      case ang if ang <= -maxRateOfRotation => getHeading - maxRateOfRotation
      
    }
    _nextPosition = currentPosition + Vector2.fromDegrees(nextSpeed,nextAngle)
    timeLastUpdated = getTime
    this.gravity = (position, gravity + position)
  }
  
  /**
   * 敵が攻撃したら動いて避ける
   */
  def simpleAvoid = {
    nearestEnemy.foreach(e=>{
      val relative = e.lastPosition - currentPosition
      val direction = relative.magnitude match{
        case d if d > 300 => {
          relative.rotateDegrees(45).normalized
        }
        case d if d < 100 => {
          relative.rotateDegrees(75)
        }
        case _ => relative.rotateDegrees(90).normalized
      }
      setTurnRightRadians(Utils.normalRelativeAngle(direction.angle - getHeadingRadians))
      if(e.didShoot){
        setMaxVelocity(8)
        //中心へのベクトルとの内積が小さくなる方向に動く => 壁にぶつからない方向
        if(direction * centralVector > -direction * centralVector){
          setAhead(50)
        }else{
          setAhead(-50)
        }
      }else{
        _nextPosition = currentPosition
      }
    })
    timeLastUpdated = getTime
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
  
  /**
   * @return 次のターンにこのロボットが移動する座標
   */
  def nextPosition = if(getTime == timeLastUpdated){
    _nextPosition
  }else{
    println(getTime,timeLastUpdated)
    throw new Exception("nextPositionを呼び出す前にドライバーにこのターンの移動を決定させてください")
  }
  
  /**
   * @return 前のターンに申告した移動先の座標
   */
  def previousDestination = if(getTime == timeLastUpdated+1){
    _nextPosition
  }else{
    throw new Exception("ドライバーがこのターンの移動先を決定する前に呼び出してください")
  }
}