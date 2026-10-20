package kh.edu.istad.platform.reactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReactiveLessonApplicationService {

    public static void main(String[] args) {
        SpringApplication.run(ReactiveController.class, args);
    }
}
