package haretaro.tankbots.t1

import robocode._
import haretaro.tankbots.commons._
import java.awt.Color
import haretaro.tankbots.math._
import haretaro.tankbots.commons.Painter

/**
 * @author Haretaro
 * クルセイダー戦車だ！
 */
class Crusader extends AdvancedRobot with Gunner with Driver with Radarman with GraphicalDebugger{
  
  override def run = {
    initDriver
    
    addOnPaintEventHandler(g =>{
      enemies.map(e => drawLine(g, Color.red, e.position, e.position+e.velocity*10))
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
      setTurnRadarRight(360)
      turnRight(30)
      setTurnRadarRight(-360)
      turnLeft(30)
    }
  }
}