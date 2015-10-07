package haretaro.tankbots.t1

import java.awt.Color
import haretaro.tankbots.commons._
import haretaro.tankbots.math.Vector2
import haretaro.tankbots.visual.{Chameleon, Painter}
import robocode._

/**
 * @author Haretaro
 * クルセイダー戦車だ！
 */
class Crusader extends AdvancedRobot with Gunner with Driver with Radarman with GraphicalDebugger{
  
  override def run = {
    initDriver
    
    addOnPaintEventHandler(g =>{
      enemies.map(e => drawLine(g, Color.red, e.lastPosition, e.lastPosition+e.lastVelocity*10))
      enemies.map(e => drawRect(g, Color.red, e.linerPrediction(getTime,0) - Vector2(16,16), 32, 32))
    })
    
    Painter.paintTan(this)
    if(getRoundNum == 3) Painter.paintStealth(this)
    setAdjustGunForRobotTurn(true)
    
    while(true){
      updateEnemyInfo
      radar
      if(getTime % 5 == 0 ) nearestEnemy.map(e => roughLinerPrediction(e,2))
      reservedFire
      gravityDrive
      execute
    }
  }
  
  /** 勝利のダンスを踊る */
  override def onWin(e:WinEvent) = {
    setBulletColor(Color.red)
    val chameleon = Chameleon(this)
    
    while(true){
      chameleon.update
      setFire(3)
      setAhead(0)
      setTurnGunRight(300)
      setTurnRadarRight(360)
      setTurnRight(30)
      execute
    }
  }
}