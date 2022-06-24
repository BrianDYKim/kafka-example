package team.brian

import team.brian.kafka.AsyncCommitConsumer
import team.brian.kafka.SyncCommitConsumer

fun main(args: Array<String>) {
    val asyncCommitConsumer = AsyncCommitConsumer()
    asyncCommitConsumer.testConsumer2()
}