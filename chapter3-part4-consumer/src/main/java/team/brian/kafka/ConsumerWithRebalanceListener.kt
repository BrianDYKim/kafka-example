package team.brian.kafka

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.consumer.OffsetAndMetadata
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.Properties

class ConsumerWithRebalanceListener {

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

    fun testConsumer() {
        val consumer = KafkaConsumer<String, String>(configs)
        val currentOffsets = mutableMapOf<TopicPartition, OffsetAndMetadata>()
        consumer.subscribe(listOf(TOPIC_NAME), object : ConsumerRebalanceListener {
            // onPartitionRevoked callback method의 경우 consumer가 rebalance되기 직전에 호출되는 함수이다.
            // rebalance되어 consumer가 consumer group에서 제외되기 전에 모든 오프셋을 커밋해야한다
            override fun onPartitionsRevoked(partitions: MutableCollection<TopicPartition>?) {
                logger.warn("Partitions are revoked!!")
                consumer.commitSync(currentOffsets)
            }

            // rebalance 직후에 호출되는 메소드이다.
            override fun onPartitionsAssigned(partitions: MutableCollection<TopicPartition>?) {
                logger.warn("Partitions are assigned!!")
            }
        })

        while (true) {
            val records = consumer.poll(Duration.ofSeconds(1))

            records.forEach { record ->
                logger.info("$record")

                // kafka의 consumer는 마지막으로 커밋한 오프셋부터 읽어내기 시작하기 때문에 commit은 무조건 offset + 1로 적용해야한다
                currentOffsets[TopicPartition(record.topic(), record.partition())] =
                    OffsetAndMetadata(record.offset() + 1, null)

                // 동기적으로 commit을 시도한다
                consumer.commitSync(currentOffsets)
            }
        }
    }
}