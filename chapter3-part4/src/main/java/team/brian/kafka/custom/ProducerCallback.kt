package team.brian.kafka.custom

import org.apache.kafka.clients.producer.Callback
import org.apache.kafka.clients.producer.RecordMetadata
import org.slf4j.LoggerFactory
import java.lang.Exception

// 레코드의 비동기 결과를 받기 위해서 사용한다. Kafka는 이를 위해서 Callback interface를 지원해준다
class ProducerCallback: Callback {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun onCompletion(metadata: RecordMetadata?, exception: Exception?) {
        if (exception != null)
            logger.error(exception.message, exception)
        else
            logger.info("$metadata")
    }
}