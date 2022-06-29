package team.brian.kafka

import org.apache.kafka.common.config.ConfigDef
import org.apache.kafka.connect.connector.Task
import org.apache.kafka.connect.source.SourceConnector

// 사용자가 지정한 커넥터 클래스의 이름은 최종적으로 커넥트에서 호출할 때 사용되기 때문에 명화가게 이름을 지어주는것이 포인트이다.
// 예를 들어, DynamoDB에서 사용하는 커넥터일 경우 DynamoDbSourceConnector로 이름을 지어주면된다
class TestSourceConnector: SourceConnector() {
    // 커넥터의 버전을 리턴하는 메소드이다
    override fun version(): String {
        TODO("Not yet implemented")
    }

    // 사용자가 Json으로 혹은 config로 입력한 설정값을 초기화해주는 메소드이다.
    // 설정값이 올바르지 않은 경우에는 ConnectionException을 발생시켜서 커넥터를 종료할 수 있다.
    override fun start(props: MutableMap<String, String>?) {
        TODO("Not yet implemented")
    }

    // 이 커넥터가 사용할 태스크 클래스를 지정한다
    override fun taskClass(): Class<out Task> {
        TODO("Not yet implemented")
    }

    // 만약에 태스크의 개수가 2개 이상인 경우에는 태스크마다 각기 다른 옵션을 설정해야할 때가 있다.
    // 이 때 태스크마다 옵션을 따로 설정할 때 사용하는 클래스이다
    override fun taskConfigs(maxTasks: Int): MutableList<MutableMap<String, String>> {
        TODO("Not yet implemented")
    }

    // 커넥터가 종료될 때 필요한 로직을 작성한다
    override fun stop() {
        TODO("Not yet implemented")
    }

    // 커넥터가 사용할 설정값에 대한 정보를 받는다. 커넥터의 설정값은 ConfigDef 클래스를 통해서
    // 각 설정의 이름, 기본값, 중요도, 설명을 정의할 수 있다.
    override fun config(): ConfigDef {
        TODO("Not yet implemented")
    }
}