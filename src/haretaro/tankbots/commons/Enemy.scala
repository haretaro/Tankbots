package haretaro.tankbots.commons

import haretaro.tankbots.math.Vector2

/**
 * @author Haretaro
 */
case class Enemy(name:String){
  
  private var history = Seq[EnemyInfo]()
  
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
  def push(time:Long, position:Vector2, velocity:Vector2, energy:Double) =
    history = history :+ EnemyInfo(time, position, velocity, energy)
  
  /** 最後に観測された時刻 */
  def timeLastUpdated = history.last.time
  
  /** 最後に観測された時の位置 */
  def lastPosition = history.last.position
  
  /** 最後に観測された時の速度 */
  def lastVelocity = history.last.velocity
  
  /** 最後に観測された時のenergy */
  def lastEnergy = history.last.energy
  
  /**
   * @return 速度ベクトルの角度の変化
   */
  def deltaRotation = {
    val latest :: prev :: tail = history.reverse
    (latest.velocity.angle - prev.velocity.angle) / (latest.time - prev.time)
  }
  
  /**
   * 速度の大きさと向きの変化が一定の時のt時間後の位置
   * 計算できるだけの情報がない場合はNoneが返る
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
  
  def circlarPrediction2(time:Long, t:Int) = {
    def next(position:Vector2, velocity:Vector2, delta:Double, t:Int):Vector2 = {
      if(t<=0){
        position
      }else{
        val v = velocity.rotate(delta)
        next(position + v, v, delta, t-1)
      }
    }
    val origin1 = history.find(_.time == time-1)
    val origin2 = history.find(_.time == time)
    (origin1,origin2) match{
      case (Some(o1),Some(o2)) => {
        val delta = o2.velocity.angle - o1.velocity.angle
        Option(next(o2.position, o2.velocity, delta, t))
      }
      case _ => None
    }
  }
  
  /**
   * 連続して情報を取得しているターン数
   */
  def stroak = {
    def calc(list:Seq[EnemyInfo]):Int = {
      val enemy :: tail = list
      tail match{
        case prev :: li if prev.time == enemy.time-1 => calc(tail)+1
        case _ => 0
      }
    }
    calc(history.reverse)
  }
  
  def circlarPredictionError(now:Long) = {
    circlarPrediction2(now-10,10).map(p => (history.last.position - p).magnitude)
  }
  
  def update = history = history.takeRight(20)
  
  def didShoot = {
    if(history.length > 1){
      val his = history.takeRight(2)
      val delta = his(1).energy - his(0).energy
      -3 <= delta && delta < 0
    }else{
      false
    }
  }
}