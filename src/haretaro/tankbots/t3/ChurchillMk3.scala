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
class ChurchillMk3 extends AdvancedRobot with Gunner with Driver with Radarman with RoboUtil with Dancer with GraphicalDebugger{
  
  override def run = {
    initDriver
    
    addOnPaintEventHandler(g =>{
      enemies.map(e => drawRect(g, Color.red, e.linerPrediction(getTime,0) - Vector2(16,16), 32, 32))
      for(i <- 0 to 5){
        enemies.map(e => e.circlarPrediction(getTime,i*5).foreach(pos =>
          drawRect(g, Color.cyan, pos - Vector2(16,16), 32, 32)))
      }
    })
    
    
    Painter.paintGreen(this)
    if(getRoundNum == 4) Painter.paintStealth(this)
    setAdjustGunForRobotTurn(true)
    
    loop(SearchingState())
    
    def loop(state:State):Unit = {
      if(getOthers > 1) gravityDrive else simpleAvoid
      executeFire
      val nextState = state.execute
      execute
      loop(nextState)
    }
    
    abstract class State{
      def execute:State
    }
    
    //周りを見る状態
    case class SearchingState() extends State{
      println("searching")
      var foundEnemies = Set[Enemy]()
      val handler:OnFoundEnemyEvent=>Unit = e => {
        foundEnemies = foundEnemies + e.enemy
      }
      addOnFoundEnemyEventHandler(handler)
      def execute = {
        if(foundEnemies.size < getOthers){
          setTurnRadarRight(100)
          println("size=" + foundEnemies.size)
          this
        }else{
          enemies = foundEnemies
          removeOnFoundEnemyEventHandler(handler)
          nearestEnemy.map(e=>AimingState(e)).getOrElse(SearchingState())
        }
      }
    }
    
    //敵を狙う状態
    case class AimingState(target:Enemy) extends State{
      println("aiming")
      var counter = 0
      
      override def execute:State = {
        lookAt(target.lastPosition)
        if(counter < 3){
          counter+=1
          this
        }else if(counter < 8){
          circlarPrediction(target,3)
          counter+=1
          this
        }else{
          circlarPrediction(target,3)
          SearchingState()
        }
      }
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