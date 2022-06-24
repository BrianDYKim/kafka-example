package team.brian.kafka

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.LoggerFactory
import java.util.Properties

class SimpleKeyValueProducer {

    private val logger = LoggerFactory.getLogger(this::class.java)

    // 저장하고자하는 토픽의 이름을 지정
    private val TOPIC_NAME = "test"

    // 카프카 브로커가 올라가있는 host의 이름과 port 번호를 기입
    private val BOOTSTRAP_SERVER = KafkaInfo.BOOTSTRAP_SERVER

    fun testProducer(keyValue: String, messageValue: String) {
        val configs = Properties()
        configs[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = BOOTSTRAP_SERVER
        configs[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
        configs[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name

        val producer = KafkaProducer<String, String>(configs)

        val record = ProducerRecord<String, String>(TOPIC_NAME, keyValue, messageValue)

        producer.send(record)

        logger.info("$record")

        producer.flush()
        producer.close()
    }
}