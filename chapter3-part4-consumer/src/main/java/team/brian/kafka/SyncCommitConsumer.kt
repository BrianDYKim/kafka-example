package team.brian.kafka

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.consumer.OffsetAndMetadata
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.Properties

/**
 * poll() 메소드가 호출된 이후에 동기적으로 커밋을 수행하는 예제이다.
 * poll() 이후에 동기적으로 커밋이 완료되기까지 실행 흐름이 block 되기 때문에 동일 시간 대비해서 데이터의 처리량이 낮다는 것이 단점이다.
 */
class SyncCommitConsumer {

    private val logger = LoggerFactory.getLogger(this::class.java)

    private val TOPIC_NAME = "test"

    private val BOOTSTRAP_SERVER = KafkaInfo.BOOTSTRAP_SERVER

    private val GROUP_ID = "test-group"

    private var configs: Properties = Properties()

    // secondary constructor
    init {
        configs[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = BOOTSTRAP_SERVER
        configs[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java.name
        configs[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java.name
        configs[ConsumerConfig.GROUP_ID_CONFIG] = GROUP_ID
        configs[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = false
    }

    // 배치 단위로 수행되는 poll에 대해서 가장 마지막 오프셋에 대해서만 커밋을 수행하는 예제
    fun testComsumer1() {
        val consumer = KafkaConsumer<String, String>(configs)
        consumer.subscribe(listOf(TOPIC_NAME))

        while (true) {
            val records = consumer.poll(Duration.ofSeconds(1))

            records.forEach { record ->
                logger.info("$record")
            }
            consumer.commitSync() // 동기적으로 커밋을 수행한다.
        }
    }

    // 개별 레코드 단위로 커밋을 수행하고 싶으면 commitSync() 메소드의 파라미터로 오프셋 정보를 넘겨버리면된다.
    fun testConsumer2() {
        val consumer = KafkaConsumer<String, String>(configs)
        consumer.subscribe(listOf(TOPIC_NAME))

        while (true) {
            val records = consumer.poll(Duration.ofSeconds(1))
            val currentOffset = mutableMapOf<TopicPartition, OffsetAndMetadata>()

            records.forEach { record ->
                logger.info("$record")

                currentOffset[TopicPartition(record.topic(), record.partition())] = OffsetAndMetadata(record.offset() + 1, null)

                consumer.commitSync(currentOffset)
            }
        }
    }

}