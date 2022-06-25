package team.brian

import team.brian.kafka.AsyncCommitConsumer
import team.brian.kafka.ConsumerWithExactPartition
import team.brian.kafka.ConsumerWithShutdownHook
import team.brian.kafka.SyncCommitConsumer

fun main(args: Array<String>) {
    val consumerWithShutdownHook = ConsumerWithShutdownHook()
    consumerWithShutdownHook.testConsumer()
}