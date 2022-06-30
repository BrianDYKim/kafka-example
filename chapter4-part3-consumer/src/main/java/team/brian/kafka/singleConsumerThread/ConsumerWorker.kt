package team.brian.kafka.singleConsumerThread

import org.slf4j.LoggerFactory

// Consumer의 데이터 처리를 담당하는 worker thread class
class ConsumerWorker(
    private val recordValue: String
): Runnable {

    private val logger = LoggerFactory.getLogger(this::class.java)

    // 데이터를 처리하는 로직을 여기다가 구현한다
    override fun run() {
        logger.info("thread: ${Thread.currentThread()}\trecord: $recordValue")
    }
}