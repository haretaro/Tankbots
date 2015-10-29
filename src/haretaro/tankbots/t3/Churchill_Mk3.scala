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
class Churchill_Mk3 extends AdvancedRobot with Gunner with Driver with Radarman with RoboUtil with Dancer with GraphicalDebugger{
  
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
    
    //ループ関数
    //再帰呼び出しでループする.ステートを見て処理を分岐し、次のステートを決定して次のループに渡す
    def loop(state:State, numberOfCalls:Int){
      
      val (nextState,nextCall) = state match{
        case State.Searching => search(numberOfCalls)
        case State.Aiming => aim(numberOfCalls)
      }
      
      execute
      loop(nextState,nextCall)
    }
    loop(State.Searching,0)
    
    def search(numberOfCalls:Int):(State,Int) = {
      if(numberOfCalls < 5){
        turnRadarRight(45)
        (State.Searching, numberOfCalls+1)
      }else{
        (State.Aiming, 0)
      }
    }
  
    def aim(numberOfCalls:Int):(State,Int) = {
      
      (State.Aiming, numberOfCalls+1)
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