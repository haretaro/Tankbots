package haretaro.tankbots.crusader

import robocode._
import haretaro.tankbots.commons._
import java.awt.Color
import haretaro.tankbots.math.Vector2

/**
 * @author Haretaro
 * クルセイダー戦車だ！
 */
class Crusader extends AdvancedRobot with Gunner with Driver with Radarman with Painter with GraphicalDebugger{
  
  override def run = {
    
    initDriver
    
    delegates = delegates :+ (() =>{
      lines = lines ++ enemies.map(e => (Color.red, e.position, e.position+e.velocity*10))
    })
    
    paintTan
    //paintStealth
    setAdjustGunForRobotTurn(true)
    
    while(true){
      updateEnemyInfo
      radar
      if(getTime % 5 == 0 ) nearestEnemy.map(e => linerPrediction(e,2))
      reservedFire()
      gravityMove
      execute
    }
  }
  
  /** 勝利のダンスを踊る */
  override def onWin(e:WinEvent) = {
    setBulletColor(Color.red)
    while(true){
      setFire(3)
      setAhead(0)
      setTurnRadarRight(360)
      turnRight(30)
      setTurnRadarRight(-360)
      turnLeft(30)
    }
  }
}