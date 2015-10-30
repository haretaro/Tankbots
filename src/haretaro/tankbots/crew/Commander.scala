package haretaro.tankbots.crew

import haretaro.tankbots.commons._
import haretaro.tankbots.math.Vector2
import robocode._

/**
 * @author Haretaro
 * 情報を管理するトレイト
 */
trait Commander extends AdvancedRobot{
  
  protected var enemies = Set[Enemy]()
  
  //TODO:?? private にするとこのセットを使っていない戦車がabstract method errorを出す.なんで？
  var onFoundEnemyEventHandlers:Set[OnFoundEnemyEvent=>Unit] = Set.empty

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
    enemy.push(e.getTime, position, velocity, e.getEnergy)
    
    val event = OnFoundEnemyEvent(enemy,e.getTime)
    onFoundEnemyEventHandlers.foreach(handler => handler(event))
  }
  
  def addOnFoundEnemyEventHandler(handler:OnFoundEnemyEvent=>Unit) =
    onFoundEnemyEventHandlers = onFoundEnemyEventHandlers + handler
  
  def removeOnFoundEnemyEventHandler(handler:OnFoundEnemyEvent=>Unit) = 
    onFoundEnemyEventHandlers = onFoundEnemyEventHandlers - handler
  
  /**
   * 数ターン以上レーダーが捉えてない敵をリストから消去する
   */
  def updateEnemyInfo = {
    enemies.foreach(e => e.update)
    enemies = enemies.filterNot(e => e.timeLastUpdated < getTime - 50)
  }
  
  /**
   * @return 最も近い敵
   */
  def nearestEnemy = enemies match{
    case e if e.isEmpty => None
    case _ => Option(enemies.minBy(e=>(e.lastPosition - Vector2(getX,getY)).magnitude))
  }
  
  /**
   * @return 予測誤差が小さい敵
   */
  def candidate = {
    if(enemies.size > 0){
      Option(enemies.minBy(e => e.circlarPredictionError(getTime)))
    }else{
      None
    }
  }
  
}