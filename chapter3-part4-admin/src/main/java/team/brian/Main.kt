package team.brian

import team.brian.kafka.WatchingBrokersAdmin

fun main(args: Array<String>) {
    val admin = WatchingBrokersAdmin()
    admin.testAdmin()
}