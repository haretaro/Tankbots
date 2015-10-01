package haretaro.tankbots.commons

import robocode._
import java.awt.Graphics2D
import java.awt.Color
import haretaro.tankbots.math.Vector2

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
  
  /** 描画処理 */
  override def onPaint(g:Graphics2D) =
    onPaintEventHandlers.foreach(f=>f(g))
}
  