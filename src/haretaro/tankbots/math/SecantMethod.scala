package haretaro.tankbots.math

/**
 * @author Haretaro
 * 割線法で解を求めるクラス
 * f 方程式をf(x) = 0 と変形した時の左辺
 * init1 初期値1
 * init2 初期値2
 * epsilon 許容誤差
 * attemptLimits 最大試行回数
 */
case class SecantMethod(val f:Double => Double, val init1:Double, val init2:Double,
    val epsilon:Double = 1e-4, val attemptLimits:Int = 30) {
  
  /**
   * @return 解を返す
   */
  def answer = {
    def ans(x1:Double, df1:Double, numberOfCall:Int):Option[Double] = {
      val x2 = x1 - f(x1)/df1
      val df2 = (f(x1)-f(x2))/(x1-x2)
      math.abs(f(x2)) match{
        case y if y < epsilon => Option(x2)
        case _ if numberOfCall >= attemptLimits => None
        case _ => ans(x2,df2,numberOfCall+1)
      }
    }
    val df = (f(init1)-f(init2))/(init1-init2)
    ans(init2,df,0)
  }
}