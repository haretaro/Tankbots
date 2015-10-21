package haretaro.tankbots.visual

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
    r.setBodyColor(tanColor)
    r.setGunColor(tanColor)
    r.setRadarColor(tanColor)
    r.setBulletColor(Color.orange)
  }
  
  /** オリーブドラブ */
  val oliveDrab = new Color(128,128,64)
  
  def paintOliveDrab(r:Robot) = {
    r.setBodyColor(oliveDrab)
    r.setGunColor(oliveDrab)
    r.setRadarColor(oliveDrab)
    r.setBulletColor(Color.orange)
  }
  
  val armyGreen = new Color(29,33,13)
  
  /**アーミーグリーンに塗装する*/
  def paintGreen(r:Robot) = {
    r.setAllColors(armyGreen)
    r.setBulletColor(Color.orange)
  }
}