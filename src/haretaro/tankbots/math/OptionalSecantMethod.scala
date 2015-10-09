package haretaro.tankbots.math

/**
 * @author Haretaro
 */
/**
 * @author Haretaro
 * 割線法で解を求めるクラス.f(x)が値を持たない場合にも対応
 * f 方程式をf(x) = 0 と変形した時の左辺
 * init1 初期値1
 * init2 初期値2
 * epsilon 許容誤差
 * attemptLimits 最大試行回数
 */
case class OptionalSecantMethod(val f:Double => Option[Double], val init1:Double, val init2:Double,
    val epsilon:Double = 1e-4, val attemptLimits:Int = 30) {
  
  /**
   * @return 解を返す
   */
  def answer = {
    def ans(x1:Double, fx1:Double, df1:Double, numberOfCall:Int):Option[Double] = {
      val x2 = x1 - fx1/df1
      f(x2) match{
        case Some(fx2) if fx2 < epsilon => Option(x2)
        case Some(fx2) => ans(x2, fx2, (fx1-fx2)/(x1-x2), numberOfCall+1)
        case _ if numberOfCall >= attemptLimits => None
      }
    }
    (f(init1), f(init2)) match {
      case (Some(fx1),Some(fx2)) => ans(init2, fx2, (fx1-fx2)/(init1-init2), 1)
      case _ => None
    }
  }
}