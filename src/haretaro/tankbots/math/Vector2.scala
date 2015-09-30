package haretaro.tankbots.math

/**
 * @author Haretaro
 * robocode用のベクトルクラス
 */
case class Vector2 (val x:Double, val y:Double){
  
  /** @return 角度(ラジアン) */
  def angle = math.atan2(-y,x) + math.Pi/2
  
  /** @return 角度(度) */
  def angleDegrees = math.toDegrees(angle)
  
  /** @return ベクトルの絶対値 */
  def magnitude = math.sqrt(x*x+y*y)
  
  /** @return 正規化したベクトル */
  def normalized = this/magnitude
  
  def +(that:Vector2) = Vector2(x+that.x, y+that.y)
  def -(that:Vector2) = Vector2(x-that.x, y-that.y)
  def *(that:Int) = Vector2(x*that, y*that)
  def *(that:Double) = Vector2(x*that, y*that)
  def /(that:Int) = Vector2(x/that, y/that)
  def /(that:Double) = Vector2(x/that, y/that)
}

object Vector2{
  /**
   * 極座標からVector2インスタンスを生成する.
   * @param r ベクトルの大きさ
   * @param theta ラジアン
   */
  def fromTheta(r:Double, theta:Double) = {
    val ang = theta - math.Pi/2
    val x = r * math.cos(ang)
    val y = -r * math.sin(ang)
    Vector2(x,y)
  }
  
  /**
   * 極座標からVector2インスタンスを生成する
   * @param r ベクトルの大きさ
   * @param angle 角度(度)
   */
  def fromDegrees(r:Double, angle:Double) = fromTheta(r, math.toRadians(angle))
}