package team.brian

import team.brian.kafka.SimpleKeyValueProducer
import team.brian.kafka.SimpleProducer
import team.brian.kafka.custom.CustomKeyValueProducer

fun main(args: Array<String>) {
    val customKeyValueProducer = CustomKeyValueProducer()
    customKeyValueProducer.testProducer("Pangyo", "26")
}