package team.brian.kafka

import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.common.config.ConfigResource
import org.slf4j.LoggerFactory
import java.util.Properties

class WatchingBrokersAdmin {

    private val logger = LoggerFactory.getLogger(this::class.java)

    private val configs = Properties()

    init {
        configs[AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG] = KafkaInfo.BOOTSTRAP_SERVER
    }

    fun testAdmin() {
        val admin = AdminClient.create(configs)

        logger.info("==========[GET BROKER INFORMATION]==========")

        admin.describeCluster().nodes().get().forEach { node ->
            logger.info("node: $node")

            val configResource = ConfigResource(ConfigResource.Type.BROKER, node.idString())
            val describeConfigs = admin.describeConfigs(listOf(configResource))

            // broker 정보 조회
            describeConfigs.all().get().forEach { broker, config ->
                config.entries().forEach { configEntry ->
                    logger.info(configEntry.name() + "= " + configEntry.value())
                }
            }
        }
    }
}