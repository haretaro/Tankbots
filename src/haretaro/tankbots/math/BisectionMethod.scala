package haretaro.tankbots.math

/**
 * @author Haretaro
 * f 関数
 * start 解の探索範囲の最小値
 * end 解の探索範囲の最大値
 */
case class BisectionMethod(val f:Double => Double,
    val start:Double, val end:Double, val epsilon:Double = 0.1) {
  def answer = {
    def ans(x1:Double, x2:Double):Option[Double] = {
      val m = (x1 + x2)/2
      
      f(x1) * f(m) match {
        case _ if math.abs(x1-x2) <= epsilon && f(x1)*f(x2)<=0 => Option(m)
        case _ if f(x1)*f(x2) > 0 => None
        case y if y < 0 => ans(x1,m)
        case y if y >= 0 => ans(m,x2)
      }
    }
    ans(start,end)
  }
}