package team.brian.kafka

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.Properties

class SimpleConsumer {

    private val logger = LoggerFactory.getLogger(this::class.java)

    private val TOPIC_NAME = "test"

    private val BOOTSTRAP_SERVER = KafkaInfo.BOOTSTRAP_SERVER

    // 컨슈머 그룹을 통해서 컨슈머의 목적을 구분할 수 있다.
    private val GROUP_ID = "test-group"

    fun testConsumer() {
        val configs = Properties()
        configs[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = BOOTSTRAP_SERVER
        configs[ConsumerConfig.GROUP_ID_CONFIG] = GROUP_ID
        configs[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java.name
        configs[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java.name

        val consumer = KafkaConsumer<String, String>(configs)
        // consumer에게 구독 처리를 하여서 consumer에 대해 토픽을 하나 할당한다
        consumer.subscribe(listOf(TOPIC_NAME))

        // 지속적인 데이터 처리를 위해 무한루프로 일단 구현해둠
        while (true) {
            val records = consumer.poll(Duration.ofSeconds(1))

            records.forEach { record ->
                logger.info("$record")
            }
        }
    }
}