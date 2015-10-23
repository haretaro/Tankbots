package haretaro.tankbots.crew

import java.awt.Color
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
  
  /**
   * 色を変化させながら小刻みに振動するうざいダンス
   */
  def chameleonShake = {
    val chameleon = Chameleon(this)
    
    var count = 1
    while(true){
      setBulletColor(Color.yellow)
      count = count * -1
      chameleon.update
      setAhead(2*count)
      setTurnRight(10*count)
      setTurnGunLeft(20*count)
      setTurnRadarRight(30*count)
      setFire(3)
      execute
    }
  }
  
  /**
   * 祝砲
   */
  def salute = {
    setAhead(0)
    setTurnRight(0)
    setTurnGunRight(0)
    setTurnRadarRight(0)
    setBulletColor(Color.white)
    for(i <- 0 to 10){
      execute
    }
    fire(3)
    while(true){
      execute
    }
  }
}