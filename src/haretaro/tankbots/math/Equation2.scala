package haretaro.tankbots.math

/**
 * @author Haretaro
 * 2次方程式のクラス
 */
case class Equation2(a:Double, b:Double, c:Double) {
  /** 判別式の値 */
  def discriminant = b*b - 4*a*c
  
  /** 実数解 */
  def solution = {
    discriminant match{
      case d if d<0 => (None,None)
      case d if d == 0 => (Option(-b / 2*a),None)
      case d => (Option((-b - math.sqrt(d))/2*a),Option((-b + math.sqrt(d))/2*a))
    }
  }
}