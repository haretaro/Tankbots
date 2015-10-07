package haretaro.tankbots.t1

import haretaro.tankbots.commons.Enemy
import haretaro.tankbots.math.Vector2
import robocode._

/**
 * @author Haretaro
 * 情報を管理するトレイト
 */
trait Commander extends AdvancedRobot{
  
  protected var enemies = Set[Enemy]()

  /**
   * レーダーがスキャンしたロボットをリストに追加する
   */
  override def onScannedRobot(e:ScannedRobotEvent) = {
    
    //敵の座標の相対位置ベクトル
    val relativePosition = Vector2.fromTheta(e.getDistance, e.getBearingRadians + getHeadingRadians)
    //敵の位置ベクトル
    val position = relativePosition + Vector2(getX,getY)
    //敵の速度ベクトル
    val velocity = Vector2.fromTheta(e.getVelocity, e.getHeadingRadians)
    
    val enemy = enemies.find(_.name == e.getName) match{
      case Some(enemy) => enemy
      case None => {
        val enemy = Enemy(e.getName)
        enemies = enemies + enemy
        enemy
      }
    }
    enemy.push(e.getTime, position, velocity)
  }
  
  /**
   * 数ターン以上レーダーが捉えてない敵をリストから消去する
   */
  def updateEnemyInfo = {
    enemies = enemies.filter(e=>getTime - e.timeLastUpdated < 20)
  }
  
  /**
   * @return 最も近い敵
   */
  def nearestEnemy = enemies match{
    case e if e.isEmpty => None
    case _ => Option(enemies.minBy(e=>(e.lastPosition - Vector2(getX,getY)).magnitude))
  }
  
}