package haretaro.tankbots.commons

import haretaro.tankbots.math.Vector2
import robocode._

/**
 * @author Haretaro
 */
trait Commander extends AdvancedRobot{
  
  protected var enemies = Set[Enemy]()

  /**
   * レーダーがスキャンしたロボットをリストに追加する
   */
  override def onScannedRobot(e:ScannedRobotEvent) = {
    
    //敵の座標の相対ベクトル
    val relativePosition = Vector2.fromTheta(e.getDistance, e.getBearingRadians + getHeadingRadians)
    //敵の位置ベクトル
    val position = relativePosition + Vector2(getX,getY)
    //敵の速度ベクトル
    val velocity = Vector2.fromTheta(e.getVelocity, e.getHeadingRadians)
    
    val enemy = enemies.find(_.name == e.getName) match{
      case Some(enemy) =>{
        enemy.position = position
        enemy.velocity = velocity
        enemy.energy = e.getEnergy
        enemy
      }
      case None => {
        val enemy = Enemy(e.getName,position,velocity,e.getEnergy)
        enemies = enemies + enemy
        enemy
      }
    }
    enemy.timeLastUpdated = e.getTime
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
    case _ => Option(enemies.minBy(e=>(e.position - Vector2(getX,getY)).magnitude))
  }
  
}