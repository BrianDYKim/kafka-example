package team.brian.kafka.singleConsumerThread

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import team.brian.kafka.KafkaInfo
import java.time.Duration
import java.util.Properties
import java.util.concurrent.Executors

class ConsumerWithWorkerThread {

    private val TOPIC_NAME = "test"

    private val GROUP_ID = "test-group"

    private val configs = Properties()

    init {
        configs[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = KafkaInfo.BOOTSTRAP_SERVER
        configs[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java.name
        configs[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java.name
        configs[ConsumerConfig.GROUP_ID_CONFIG] = GROUP_ID
    }

    fun testConsumer() {
        val consumer = KafkaConsumer<String, String>(configs)
        consumer.subscribe(listOf(TOPIC_NAME)) // test topic을 구독
        val executorService =
            Executors.newCachedThreadPool() // cacheThreadPool을 이용해서 workerThread가 일을 마치면 바로 종료될 수 있도록 설정

        while (true) {
            val records = consumer.poll(Duration.ofSeconds(10))

            records.forEach { record ->
                val worker = ConsumerWorker(record.value())
                executorService.execute(worker)
            }
        }
    }
}