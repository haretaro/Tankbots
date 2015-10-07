package haretaro.tankbots.commons

import java.awt.{Color, Graphics2D}

import haretaro.tankbots.math.Vector2
import robocode.Robot

/**
 * @author Haretaro
 */
trait GraphicalDebugger extends Robot{
  
  /** onPaintイベントハンドラーのリスト */
  private var onPaintEventHandlers = List[Graphics2D=>Unit]()
  
  /** onPaintイベントで呼び出されるハンドラーを追加する */
  def addOnPaintEventHandler(handler:Graphics2D=>Unit) =
    onPaintEventHandlers = onPaintEventHandlers :+ handler
  
  //Double から Int への暗黙の型変換
  implicit def convert(value:Double) = value.asInstanceOf[Int]
  
  /** 線分を描画する */
  def drawLine(g:Graphics2D, color:Color, start:Vector2, end:Vector2) = {
    g.setColor(color)
    g.drawLine(start.x,start.y,end.x,end.y)
  }
  
  /** 矩形を描画する */
  def drawRect(g:Graphics2D, color:Color, position:Vector2, width:Int, height:Int) = {
    g.setColor(color)
    g.drawRect(position.x,position.y,width,height)
  }
  
  def drawCircle(g:Graphics2D, color:Color, center:Vector2, radius:Double) = {
    g.setColor(color)
    g.drawArc(center.x-radius, center.y-radius,
        radius*2, radius*2,
        0,360)
  }
  
  /** 描画処理 */
  override def onPaint(g:Graphics2D) =
    onPaintEventHandlers.foreach(f=>f(g))
}
  