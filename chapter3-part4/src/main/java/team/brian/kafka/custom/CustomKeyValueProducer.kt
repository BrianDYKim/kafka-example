package team.brian.kafka.custom

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.LoggerFactory
import team.brian.kafka.KafkaInfo
import java.util.Properties

class CustomKeyValueProducer {

    private val logger = LoggerFactory.getLogger(this::class.java)

    private val callback = ProducerCallback()

    private val TOPIC_NAME = "test"

    private val BOOTSTRAP_SERVER = KafkaInfo.BOOTSTRAP_SERVER

    fun testProducer(keyValue: String, messageValue: String) {
        val configs = Properties()
        configs[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = BOOTSTRAP_SERVER
        configs[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
        configs[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
        // configs에 Partition을 등록시킨다
        configs[ProducerConfig.PARTITIONER_CLASS_CONFIG] = CustomPartitioner::class.java

        val kafkaProducer = KafkaProducer<String, String>(configs)

        val record = ProducerRecord<String, String>(TOPIC_NAME, keyValue, messageValue)

        // producer.send()의 반환 형태는 RecordMetaData이다.
        kafkaProducer.send(record, callback)

        kafkaProducer.flush()
        kafkaProducer.close()
    }
}