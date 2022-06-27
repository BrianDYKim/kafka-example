package team.brian.kafka

import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import java.util.Properties

// stream_log 토픽에서 stream_log_copy 토픽으로 로그를 복사하는 간단한 토폴로지 구현
class SimpleKafkaStreams {

    // src topic
    private val STREAM_LOG = "stream_log"

    // dest topic
    private val STREAM_LOG_COPY = "stream_log_copy"

    private val properties = Properties()

    init {
        properties[StreamsConfig.BOOTSTRAP_SERVERS_CONFIG] = KafkaStreamsInfo.BOOTSTRAP_SERVER
        // Stremas Application은 역할별로 어플리케이션 ID를 구분지어야한다
        properties[StreamsConfig.APPLICATION_ID_CONFIG] = KafkaStreamsInfo.APPLICATION_NAMR
        // 스트림 처리를 위해서 (역)직렬화 방식을 정해야한다. 여기서는 한번에 역직렬화/직렬화 방식을 할당해서 넣어준다
        properties[StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG] = Serdes.String()::class.java
        properties[StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG] = Serdes.String()::class.java
    }

    fun testStreams() {
        val streamBuilder = StreamsBuilder() // streamBuilder는 스트림 토폴로지를 정하기 위해서 사용해야만한다
        // stream_log 토픽으로부터 KStream 객체를 생성한다.
        // 그 외에도 builder의 table(), globalTable() 메소드는 소스 프로세스의 역할을 수행한다
        val streamLog = streamBuilder.stream<String, String>(STREAM_LOG)
        // 소스 프로세서로부터 다른 토픽으로 싱크를 맞춰주기 위해서 to 메소드를 사용해서 KStream의 로그들을 모두 복사한다
        streamLog.to(STREAM_LOG_COPY)

        // 이전에 정의한 스트림을 실행하기 위해서 KafkaStreams 클래스를 이용해서 스트림을 정의한다
        val streams = KafkaStreams(streamBuilder.build(), properties)
        streams.start()
    }
}