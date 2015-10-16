package haretaro.tankbots.commons

import haretaro.tankbots.math.Vector2

/**
 * @author Haretaro
 * ある時刻における敵の情報
 */
case class EnemyInfo(time:Long, position:Vector2, velocity:Vector2) {
}