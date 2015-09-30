package haretaro.tankbots.commons

import robocode._
import java.awt.Graphics2D
import java.awt.Color
import haretaro.tankbots.math.Vector2

/**
 * @author Haretaro
 */
trait GraphicalDebugger extends Robot{
  var lines = List[(Color,Vector2,Vector2)]()
  
  var delegates:List[()=>Unit] = List.empty
  
  //Double から Int への暗黙の型変換
  implicit def convert(value:Double) = value.asInstanceOf[Int]
  
  override def onPaint(g:Graphics2D) = {
    lines = List.empty
    delegates.foreach(f => f())
    
    lines.foreach(line =>{
      g.setColor(line._1)
      g.drawLine(line._2.x,line._2.y,line._3.x,line._3.y)
    })
  }
}