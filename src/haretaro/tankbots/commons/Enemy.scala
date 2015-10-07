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
  
  /**
   * @return 速度ベクトルの角度の変化
   */
  def deltaRotation = {
    val latest :: prev :: tail = history.reverse
    (latest.velocity.angle - prev.velocity.angle) / (latest.time - prev.time)
  }
  
  /**
   * 円形予測を行う
   */
  def circlarPrediction(now:Long, t:Int) = {
    history match{
      case his if his.length < 2 => None
      case _ =>{
        var position = history.last.position
        var velocity = history.last.velocity
        for (i <- 0 to t + (now - timeLastUpdated).asInstanceOf[Int]){
          position += velocity
          velocity = velocity.rotate(deltaRotation)
        }
        Option(position)
      }
    }
  }
  
  def historySize = history.length
}