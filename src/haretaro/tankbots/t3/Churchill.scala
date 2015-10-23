package haretaro.tankbots.t3

import java.awt.Color
import haretaro.tankbots.commons._
import haretaro.tankbots.crew._
import haretaro.tankbots.math.Vector2
import haretaro.tankbots.t1._
import haretaro.tankbots.visual.{Chameleon, Painter}
import robocode._

/**
 * @author Haretaro
 * 1 on 1 時に攻撃を避けるチャーチル戦車
 */
class Churchill extends AdvancedRobot with Gunner with Driver with Radarman with RoboUtil with Dancer with GraphicalDebugger{
  
  override def run = {
    initDriver
    
    addOnPaintEventHandler(g =>{
      enemies.map(e => drawRect(g, Color.red, e.linerPrediction(getTime,0) - Vector2(16,16), 32, 32))
      for(i <- 0 to 5){
        enemies.map(e => e.circlarPrediction2(getTime,i*5).foreach(pos =>
          drawRect(g, Color.cyan, pos - Vector2(16,16), 32, 32)))
      }
    })
    
    
    Painter.paintGreen(this)
    if(getRoundNum == 4) Painter.paintStealth(this)
    setAdjustGunForRobotTurn(true)
    while(true){
      
      updateEnemyInfo
      
      getEnergy match{
        case life if life > 20 => executeFire
        case life if life > 3 && getOthers > 1 => executeFire
        case _ if nearestEnemy.map(e=>e.lastEnergy == 0).getOrElse(false) => executeFire
        case _ => ()
      }
      
      getTime match{
        case t if getOthers > 1 && (t/10)%4==0 || enemies.size == 0 => setTurnRadarRight(45)
        case _ => nearestEnemy.map(_.lastPosition).orElse(Option(Vector2(0,0))).foreach(p => lookAt(p))
      }
      
      if(getOthers==1){
        simpleAvoid
      }else{
        gravityDrive
      }
      
      //敵が瀕死ならば,ちょうど一発で殺せるパワーで撃つ
      nearestEnemy.foreach(e=>{
        e match{
          //damage = 4 * power where power <= 1
          case e if e.lastEnergy <= 4 => circlarPrediction(e,e.lastEnergy/4,nextPosition)
          //damage = 4 * power + 2 * (power - 1) where power > 1
          case e if e.lastEnergy <= 16 => circlarPrediction(e,(e.lastEnergy+2)/6,nextPosition)
          case e if (e.lastPosition - currentPosition).magnitude < 200 => circlarPrediction(e,3,nextPosition)
          case _ => circlarPrediction(e,2,nextPosition)
        }
      })
      
      execute
    }
  }
  
  /** 勝利のダンスを踊る */
  override def onWin(e:WinEvent) = {
    setBulletColor(Color.red)
    
    getRoundNum match{
      case 1 => chameleonDance
      case 10 => salute
      case r if r%2==0 => chameleonShake
      case _ => trackerDance
    }
  }
}