package haretaro.tankbots.commons

import java.awt.Color

import robocode.Robot

/**
 * @author Haretaro
 * カメレオンの様に虹色に塗装するクラス
 */
case class Chameleon(val robot:Robot) {
  
  var hue = 0
  
  /** 色相の調整量/update */
  var delta = 90
  
  /** 色を更新する */
  def update = {
    robot.setRadarColor(new Color(red(hue),green(hue),blue(hue)))
    robot.setGunColor(new Color(red(hue+10),green(hue+10),blue(hue+10)))
    robot.setBodyColor(new Color(red(hue+20),green(hue+20),blue(hue+20)))
    hue += delta
  }
  
  /** 色相に対応する赤の輝度値
   *  @param hue 色相(360度形式)
   *  @return 0 to 255
   */
  def red(hue:Int) = {
    val r = hue%360 match {
      case h if 0 <= h && h < 60 => 1
      case h if 60 <= h && h < 120 => -2.0 * h / 120 + 2
      case h if 120 <= h && h < 240 => 0
      case h if 240 <= h && h < 300 => 2.0 * h / 120 - 4
      case h if 300 <= h && h <= 360 => 1
    }
    (r * 255).asInstanceOf[Int]
  }
  
  def green(hue:Int) = red(hue+120)
  
  def blue(hue:Int) = red(hue+240)
}