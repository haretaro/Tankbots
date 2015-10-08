package haretaro.tankbots.t2

import java.awt.Color

import haretaro.tankbots.commons._
import haretaro.tankbots.math.Vector2
import haretaro.tankbots.t1._
import haretaro.tankbots.visual.{Chameleon, Painter}
import robocode._

/**
 * @author Haretaro
 * 円形予測射撃能力と斥力移動能力をもつクロムウェル戦車
 */
class Cromwell extends AdvancedRobot with CirclarGunner with Driver with Radarman with RoboUtil with Dancer with GraphicalDebugger{
    override def run = {
    initDriver
    
    addOnPaintEventHandler(g =>{
      enemies.map(e => drawLine(g, Color.red, e.lastPosition, e.lastPosition+e.lastVelocity*10))
      enemies.map(e => drawRect(g, Color.red, e.linerPrediction(getTime,0) - Vector2(16,16), 32, 32))
      //enemies.map(e => drawRect(g, Color.cyan, e.circlarPrediction(getTime,0) - Vector2(16,16), 32, 32))
      drawCircle(g,Color.green,currentPosition,150)
    })
    
    Painter.paintOliveDrab(this)
    if(getRoundNum == 3) Painter.paintStealth(this)
    setAdjustGunForRobotTurn(true)
    
    while(true){
      updateEnemyInfo
      radar
      if(getTime % 5 == 0 ){
        nearestEnemy.map(e => {
          val power = (e.linerPrediction(getTime,0) - Vector2(getX,getY)).magnitude match{
            case d if d<150 => 3.0
            case _ => 2.0
          }
          circlarPrediction(e,power)
        })
      }
      reservedFire
      gravityDrive
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