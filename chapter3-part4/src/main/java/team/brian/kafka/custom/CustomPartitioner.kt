package team.brian.kafka.custom

import org.apache.kafka.clients.producer.Partitioner
import org.apache.kafka.common.Cluster
import org.apache.kafka.common.InvalidRecordException
import org.apache.kafka.common.utils.Utils

class CustomPartitioner: Partitioner {
    override fun configure(configs: MutableMap<String, *>?) {

    }

    override fun close() {

    }

    /**
     * 레코드를 기반으로 파티션을 정하는 로직을 정하는 메소드
     * @return 주어진 레코드가 들어가는 파티션 번호
     */
    override fun partition(
        topic: String?,
        key: Any?,
        keyBytes: ByteArray?,
        value: Any?,
        valueBytes: ByteArray?,
        cluster: Cluster?
    ): Int {
        // 키 값이 존재하지 않는 레코드는 비 정상 레코드로 간주하고 에러를 뱉어낸다
        requireNotNull(keyBytes) {
            InvalidRecordException("Need message key!!")
        }

        // 메시지 키가 Pangyo인 경우 0을 반환시킨다. -> 파티션 0으로 보낸다
        check((key as String).equals("Pangyo")) {
            return 0
        }

        val partitions = cluster!!.partitionsForTopic(topic)
        val numPartitions = partitions.size

        // 그 외의 메시지 키는 메시지 키의 해쉬값을 기반으로 특정 파티션에 꽂히도록 설정한다
        return Utils.toPositive(Utils.murmur2(keyBytes)) % numPartitions
    }
}