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

// 리밸런스 리스너를 가진 consumer에 대한 예제이다
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

        // 리밸런스 발생시 리밸런스가 되기 전에 poll()을 통해 불러온 모든 레코드에 대해서 commit해야하기 때문에 rebalance listener를 준비해야한다
        consumer.subscribe(listOf(TOPIC_NAME), object : ConsumerRebalanceListener {
            // consumer group에서 rebalance가 일어나기 직전에 호출되는 callback 메소드이다
            override fun onPartitionsRevoked(partitions: MutableCollection<TopicPartition>?) {
                logger.warn("Partitions are revoked!!")
                consumer.commitSync(currentOffsets)
            }

            // consumer group에서 rebalance가 끝난 직후에 파티션이 할당 완료되면 호출되는 메소드이다
            override fun onPartitionsAssigned(partitions: MutableCollection<TopicPartition>?) {
                logger.warn("Partitions are assigned!!")
            }
        })

        while (true) {
            val records = consumer.poll(Duration.ofSeconds(1))

            records.forEach { record ->
                logger.info("$record")

                currentOffsets[TopicPartition(record.topic(), record.partition())] =
                    OffsetAndMetadata(record.offset() + 1, null)

                consumer.commitSync(currentOffsets)
            }
        }
    }
}