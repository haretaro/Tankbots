package haretaro.tankbots.crusader

import robocode._
import java.awt.Color

/**
 * @author Haretaro
 * 塗装職人
 * ロボットを塗装するメソッドを実装する
 */
trait Painter extends Robot{
  /** 床と同じ色*/
  val stealthColor = new Color(70,77,108)
  
  /** 床と同じ色に塗装する*/
  def paintStealth = setAllColors(stealthColor)
  
  /** タンカラー*/
  val tanColor = new Color(166,123,91)
  
  /** タンカラーに塗装する*/
  def paintTan = {
    setBodyColor(tanColor);
    setGunColor(tanColor);
    setRadarColor(tanColor);
    setScanColor(Color.orange);
    setBulletColor(Color.orange);
  }
}