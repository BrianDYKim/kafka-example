package team.brian

import team.brian.kafka.singleConsumerThread.ConsumerWithWorkerThread

fun main(args: Array<String>) {
    val consumer = ConsumerWithWorkerThread()
    consumer.testConsumer()
}