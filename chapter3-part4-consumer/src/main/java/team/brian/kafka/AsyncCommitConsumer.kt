package team.brian.kafka

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.consumer.OffsetAndMetadata
import org.apache.kafka.clients.consumer.OffsetCommitCallback
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import java.lang.Exception
import java.time.Duration
import java.util.Properties

class AsyncCommitConsumer {

    private val logger = LoggerFactory.getLogger(this::class.java)

    private val BOOTSTRAP_SERVER = KafkaInfo.BOOTSTRAP_SERVER

    private val TOPIC_NAME = "test"

    private val GROUP_ID = "test-group"

    private val configs = Properties()

    init {
        configs[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = BOOTSTRAP_SERVER
        configs[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java.name
        configs[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java.name
        configs[ConsumerConfig.GROUP_ID_CONFIG] = GROUP_ID
        configs[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = false
    }

    // 비동기적인 커밋을 처리하는 예시
    fun testConsumer1() {
        val consumer = KafkaConsumer<String, String>(configs)
        consumer.subscribe(listOf(TOPIC_NAME))

        while (true) {
            val records = consumer.poll(Duration.ofSeconds(1))

            records.forEach { record ->
                logger.info("$record")
            }

            consumer.commitAsync()
        }
    }

    // callback 함수를 파라미터로 받아서 결과를 얻는 방법
    fun testConsumer2() {
        val consumer = KafkaConsumer<String, String>(configs)
        consumer.subscribe(listOf(TOPIC_NAME))

        while (true) {
            val records = consumer.poll(Duration.ofSeconds(1))

            records.forEach { record ->
                logger.info("$record")
            }

            consumer.commitAsync(object : OffsetCommitCallback {
                override fun onComplete(
                    offsets: MutableMap<TopicPartition, OffsetAndMetadata>?,
                    exception: Exception?
                ) {
                    if (exception != null)
                        println("Commit failed!!")
                    else
                        println("Commit succeeded!!")

                    if (exception != null)
                        logger.error("Commit failed for offsets $offsets")
                }
            })
        }
    }
}