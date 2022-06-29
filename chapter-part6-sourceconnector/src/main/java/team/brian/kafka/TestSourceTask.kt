package team.brian.kafka

import org.apache.kafka.connect.source.SourceRecord
import org.apache.kafka.connect.source.SourceTask

class TestSourceTask: SourceTask() {
    // 태스크의 버전을 작성한다
    override fun version(): String {
        TODO("Not yet implemented")
    }

    // 태스크가 시작할 때 필요한 로직을 작성한다.
    // 태스크는 실질적으로 데이터를 처리하기 때문에 데이터 처리에 필요한 모든 리소스들을 여기서 정의해주는게 좋다
    // 예를들어, JDBC 커텍터의 태스크인 경우 여기서 커넥션을 맺어주는 것이다.
    override fun start(props: MutableMap<String, String>?) {
        TODO("Not yet implemented")
    }

    // 태스크가 종료될 때의 로직을 작성한다
    override fun stop() {
        TODO("Not yet implemented")
    }

    // 소스 애플리케이션, 혹은 소스 파일로부터 데이터를 읽어오는 로직을 작성한다.
    // MutableList<SourceRecord>로 데이터가 반환이 되면 그대로 토픽에 실려나간다
    override fun poll(): MutableList<SourceRecord> {
        TODO("Not yet implemented")
    }
}