package haretaro.tankbots.commons

import haretaro.tankbots.math.Vector2

/**
 * @author Haretaro
 */
case class Enemy(name:String){
  
  private var history = Seq[EnemyHistory]()
  
  /**
   * @return 速度ベクトルが一定の場合の現在時刻からt時間後の未来位置.t=0で現在位置
   */
  def linerPrediction(now:Long, t:Int) = {
    val position = history.last.position
    val velocity = history.last.velocity
    position + velocity * (now -timeLastUpdated + t)
  }
  
  /**
   * 情報をリストに追加する
   */
  def push(time:Long, position:Vector2, velocity:Vector2) =
    history = history :+ EnemyHistory(time, position, velocity)
  
  /** 最後に観測された時刻 */
  def timeLastUpdated = history.last.time
  
  /** 最後に観測された時の位置 */
  def lastPosition = history.last.position
  
  /** 最後に観測された時の速度 */
  def lastVelocity = history.last.velocity
}