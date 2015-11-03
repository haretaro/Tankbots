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
      executeFire
      val nextState = state.execute
      execute
      loop(nextState)
    }
    
    abstract class State{
      def execute:State
    }
    
    //索敵状態
    case class SearchingState() extends State{
      var foundEnemies = Set[Enemy]()
      var counter = 0
      val numberOfEnemies = getOthers
      val handler:OnFoundEnemyEvent=>Unit = e => foundEnemies = foundEnemies + e.enemy
      
      addOnFoundEnemyEventHandler(handler)
      
      def execute = {
        gravityDrive
        val nextState = foundEnemies.size match{
          //索敵中に敵の数が変わったら索敵を最初からやり直す
          case _ if getOthers != numberOfEnemies => {
            removeOnFoundEnemyEventHandler(handler)
            SearchingState()
          }
          case size if size < getOthers && counter < 2 => {
            setTurnRadarLeft(100)
            this
          }
          case size if size < getOthers => {
            setTurnRadarRight(100)
            this
          }
          case _ if getOthers < 2 => {
            enemies = foundEnemies
            removeOnFoundEnemyEventHandler(handler)
            nearestEnemy.map(e=>OneOnOne(e)).getOrElse(SearchingState())
          }
          case _ => {
            enemies = foundEnemies
            removeOnFoundEnemyEventHandler(handler)
            nearestEnemy.map(e=>AimingState(e)).getOrElse(SearchingState())
          }
        }
        counter += 1
        nextState
      }
    }
    
    //敵を狙う状態
    case class AimingState(target:Enemy) extends State{
      var counter = 0
      
      override def execute:State ={
        gravityDrive
        lookAt(target.lastPosition)
        circlarPrediction(target,2)
        val nextState = getGunHeat match{
          case heat if heat > 1 => SearchingState()
          case _ if counter > 6 => SearchingState()
          case _ => this
        }
        counter += 1
        nextState
      }
    }
    
    //1 on 1 になった状態
    case class OneOnOne(target:Enemy) extends State{
      
      override def execute:State = {
        lookAt(target.lastPosition)
        simpleAvoid
        if(getEnergy > 3) circlarPrediction(target,2)
        this
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