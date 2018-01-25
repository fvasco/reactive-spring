package web

import dao.IotClient
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan("dao", "example", "web")
class Application {

    private val log = LoggerFactory.getLogger(Application::class.java)

    @Bean
    fun init(iotClient: IotClient) = CommandLineRunner {
        log.info("Application initialized")
    }

}
