package haretaro.tankbots.t1

import haretaro.tankbots.visual.Chameleon
import robocode._

/**
 * @author Haretaro
 * 勝利のダンスを集めたトレイト
 */
trait Dancer extends AdvancedRobot {
  
  /**
   * trackerボットにインスパイアされた勝利のダンス
   */
  def trackerDance = {
    setAhead(0)
    while(true){
      setTurnGunRight(300)
      setTurnRadarRight(300)
      turnRight(30)
      
      setTurnGunLeft(300)
      setTurnRadarLeft(300)
      turnLeft(30)
    }
  }
  
  /**
   * 色を変化させる派手なダンスだ 
   */
  def chameleonDance = {
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