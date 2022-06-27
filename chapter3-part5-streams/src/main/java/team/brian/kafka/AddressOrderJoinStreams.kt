package team.brian.kafka

import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import java.util.Properties

class AddressOrderJoinStreams {

    private val APPLICATION_NAME = "order-join-application"

    private val ADDRESS_TABLE = "address"

    private val ORDER_STREAM = "order"

    private val ORDER_JOIN_STREAM = "order_join"

    private val properties = Properties()

    init {
        properties[StreamsConfig.BOOTSTRAP_SERVERS_CONFIG] = KafkaStreamsInfo.BOOTSTRAP_SERVER
        properties[StreamsConfig.APPLICATION_ID_CONFIG] = APPLICATION_NAME
        properties[StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG] = Serdes.String()::class.java
        properties[StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG] = Serdes.String()::class.java
    }

    fun testJoin() {
        val streamsBuilder = StreamsBuilder()

        // ==============================[Topology 작성]==============================
        // 소스 프로세서 작성
        val addressKTable = streamsBuilder.table<String, String>(ADDRESS_TABLE)
        val orderKStream = streamsBuilder.stream<String, String>(ORDER_STREAM)

        // Join을 수행하는 스트림 프로세서 작성 이후에 join stream으로 싱크 맞추기
        // address 토픽, order 토픽에서 동일한 메시지 키 값을 가지는 데이터를 발견시 아래와 같은 조합으로 메시지 값을 생성해서 join으로 보낸다
        // ex) key1:iMac, key1:Newyork -> iMac send to Newyork
        orderKStream.join(addressKTable) { order, address ->
            "$order send to $address"
        }.to(ORDER_JOIN_STREAM)

        // ==============================[Stream 실행]==============================
        val streams = KafkaStreams(streamsBuilder.build(), properties)
        streams.start()
    }
}