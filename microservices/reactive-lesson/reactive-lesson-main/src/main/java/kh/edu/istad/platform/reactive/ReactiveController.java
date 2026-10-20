package kh.edu.istad.platform.reactive;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
@RequestMapping
public class ReactiveController {

    @GetMapping
    public Flux<String> flux(){
        return Flux.just("Hello","World","b","g")
                .log()
                .delayElements(Duration.ofSeconds(1));
    }
}
