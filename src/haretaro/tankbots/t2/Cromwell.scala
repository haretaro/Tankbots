package haretaro.tankbots.t2

import java.awt.Color
import haretaro.tankbots.commons._
import haretaro.tankbots.crew._
import haretaro.tankbots.math.Vector2
import haretaro.tankbots.t1._
import haretaro.tankbots.visual.{Chameleon, Painter}
import robocode._

/**
 * @author Haretaro
 * 円形予測射撃能力と斥力移動能力をもつクロムウェル戦車
 */
class Cromwell extends AdvancedRobot with Gunner with Driver with Radarman with RoboUtil with Dancer with GraphicalDebugger{
  
  override def run = {
    initDriver
    //initGunner
    
    
    addOnPaintEventHandler(g =>{
      enemies.map(e => drawRect(g, Color.red, e.linerPrediction(getTime,0) - Vector2(16,16), 32, 32))
      for(i <- 0 to 5){
        enemies.map(e => e.circlarPrediction(getTime,i*5).foreach(pos =>
          drawRect(g, Color.cyan, pos - Vector2(16,16), 32, 32)))
      }
    })
    
    
    Painter.paintOliveDrab(this)
    if(getRoundNum == 3) Painter.paintStealth(this)
    setAdjustGunForRobotTurn(true)
    while(true){
      
      updateEnemyInfo
      //updateGunner
      executeFire
      radar
      gravityDrive
      
      if(getTime % 3 == 0 ){
        nearestEnemy.foreach(e => {
          //println(e.circlarPredictionError(getTime))
          circlarPrediction(e,2d,nextPosition)
        })
      }
      execute
    }
  }
  
  /** 勝利のダンスを踊る */
  override def onWin(e:WinEvent) = {
    setBulletColor(Color.red)
    
    getRoundNum%2 match{
      case 0 => trackerDance
      case 1 => chameleonDance
    }
  }
}