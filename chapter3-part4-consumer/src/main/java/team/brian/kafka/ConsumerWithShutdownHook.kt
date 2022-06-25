package team.brian.kafka

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.consumer.OffsetAndMetadata
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.errors.WakeupException
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.Properties

// test topic의 0번 partition을 명시적으로 구독하는 consumer의 구현
// 안전한 종료를 위해서 shutdown hook을 적용하는 클래스 예제이다
class ConsumerWithShutdownHook {

    private val logger = LoggerFactory.getLogger(this::class.java)

    private val BOOTSTRAP_SERVER = KafkaInfo.BOOTSTRAP_SERVER

    private val GROUP_ID = KafkaInfo.GROUP_ID

    private val TOPIC_NAME = "test"

    private val PARTITION_NUMBER = 0

    private val configs = Properties()

    init {
        configs[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = BOOTSTRAP_SERVER
        configs[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java.name
        configs[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java.name
        configs[ConsumerConfig.GROUP_ID_CONFIG] = GROUP_ID
        configs[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = false
    }

    fun testConsumer() {
        val consumer = KafkaConsumer<String, String>(configs)
        // main process에 shutdown hook을 추가시켜서 shutdown시 취할 행동을 정의한다.
        // kill -term [PID] 를 통해서 shutdown hook을 발생시켜 안전한 종료가 가능하다. 혹은 정지 버튼을 눌러도 동작한다.
        Runtime.getRuntime().addShutdownHook(CustomShutdownThread(consumer))
        consumer.assign(listOf(TopicPartition(TOPIC_NAME, PARTITION_NUMBER)))

        try {
            while (true) {
                val records = consumer.poll(Duration.ofSeconds(1))
                val currentOffsets = mutableMapOf<TopicPartition, OffsetAndMetadata>()

                records.forEach { record ->
                    logger.info("$record")

                    currentOffsets[TopicPartition(record.topic(), record.partition())] =
                        OffsetAndMetadata(record.offset() + 1, null)

                    consumer.commitSync(currentOffsets)
                }
            }
        } catch (e: WakeupException) {
            logger.warn("Wakeup consumer!!")
        } finally {
            consumer.close() // 리소스 종료처리
        }
    }

    // shutdown hook이 발생시 취할 행동을 정의하는 스레드이다
    inner class CustomShutdownThread(
        private val consumer: KafkaConsumer<String, String>
    ) : Thread() {
        override fun run() {
            logger.info("shutdown hook!!")
            consumer.wakeup()
        }
    }
}