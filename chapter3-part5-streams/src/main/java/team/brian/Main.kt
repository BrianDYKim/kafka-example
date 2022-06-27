package team.brian

import team.brian.kafka.AddressOrderJoinStreams
import team.brian.kafka.FilteringKafkaStreams
import team.brian.kafka.SimpleKafkaStreams

fun main(args: Array<String>) {
    val joinStreams = AddressOrderJoinStreams()
    joinStreams.testJoin()
}