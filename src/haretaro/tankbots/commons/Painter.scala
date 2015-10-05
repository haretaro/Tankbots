package haretaro.tankbots.commons

import java.awt.Color

import robocode.Robot

/**
 * @author Haretaro
 * 塗装職人
 * ロボットを塗装するメソッドを実装する
 */
object Painter{
  /** 床と同じ色*/
  val stealthColor = new Color(70,77,108)
  
  /** 床と同じ色に塗装する*/
  def paintStealth(r:Robot) = r.setAllColors(stealthColor)
  
  /** タンカラー*/
  val tanColor = new Color(166,123,91)
  
  /** タンカラーに塗装する*/
  def paintTan(r:Robot) = {
    r.setBodyColor(tanColor);
    r.setGunColor(tanColor);
    r.setRadarColor(tanColor);
    r.setScanColor(Color.orange);
    r.setBulletColor(Color.orange);
  }
}