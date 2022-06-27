package team.brian.kafka

import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import java.util.*

// 문자열 길이가 5 이상인 문자열들만 골라서 싱크시키는 토폴로지를 구현하는 예제 클래스
class FilteringKafkaStreams {

    // src topic = stream_log
    private val STREAM_LOG = "stream_log"

    // dest topic - stream_log_filter
    private val STREAM_LOG_FILTER = "stream_log_filter"

    private val APPLICATION_ID = "streams-filter-application"

    private val properties = Properties()

    init {
        properties[StreamsConfig.BOOTSTRAP_SERVERS_CONFIG] = KafkaStreamsInfo.BOOTSTRAP_SERVER
        properties[StreamsConfig.APPLICATION_ID_CONFIG] = APPLICATION_ID
        properties[StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG] = Serdes.String()::class.java
        properties[StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG] = Serdes.String()::class.java
    }

    // source processor에서는 stream_log에서 데이터를 복사하고, stream processor에서는 데이터 프로세싱을 진행하고,
    // to 메소드를 사용해서 stream_log_filter 토픽에다가 싱크를 맞춘다
    fun testStreams() {
        // ====================[Topology 정의 부분]==============================
        val streamsBuilder = StreamsBuilder()
        val streamLog = streamsBuilder.stream<String, String>(STREAM_LOG)
        val filterStream = streamLog.filter { key, value ->
            value.length > 5
        }
        filterStream.to(STREAM_LOG_FILTER)

        // ====================[토폴로지를 이용해서 스트리밍 실행]==============================
        val streams = KafkaStreams(streamsBuilder.build(), properties)
        streams.start()
    }
}