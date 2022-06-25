package team.brian.kafka

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.consumer.OffsetAndMetadata
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.Properties

// 명시적으로 파티션을 지정하여 consume을 하는 consumer를 구현하는 클래스
// 실습 환경에서는 test 토픽의 파티션이 2개이므로 round-robin 정책에 의해서 producer가 partition을 명시하지 않는 이상
// 2번 단위로 번갈아가며 log가 찍힌다
class ConsumerWithExactPartition {

    private val logger = LoggerFactory.getLogger(this::class.java)

    private val BOOTSTRAP_SERVER = KafkaInfo.BOOTSTRAP_SERVER

    private val TOPIC_NAME = "test"

    private val GROUP_ID = KafkaInfo.GROUP_ID

    private val PARTITION_NUMBER = 0

    private val configs = Properties()

    init {
        configs[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = BOOTSTRAP_SERVER
        configs[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java.name
        configs[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java.name
        configs[ConsumerConfig.GROUP_ID_CONFIG] = GROUP_ID
        configs[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = false // 비명시적 자동 커밋 설정을 false로 설정한다
    }

    fun testConsumer() {
        val consumer = KafkaConsumer<String, String>(configs)
        // 명시적으로 파티션을 컨슈머에 할당한다
        // 명시적으로 파티션을 컨슈머에 직접 할당하는 경우 rebalance 과정이 존재하지 않는다.
        consumer.assign(listOf(TopicPartition(TOPIC_NAME, PARTITION_NUMBER)))

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
    }
}