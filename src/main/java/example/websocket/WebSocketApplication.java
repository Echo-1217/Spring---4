package example.websocket;

import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class WebSocketApplication {

	public static void main(String[] args) throws IOException {
		Runtime.getRuntime().exec("cmd.exe /c  docker-compose -f broker-compose.yml up mq" );
		SpringApplication.run(WebSocketApplication.class, args);
	}

}
