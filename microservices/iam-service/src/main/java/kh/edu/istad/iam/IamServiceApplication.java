package kh.edu.istad.iam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@Controller
public class IamServiceApplication {

    @GetMapping("/login")
    public String login(Model model){
        return "login";
    }

    public static void main(String[] args) {
        SpringApplication.run(IamServiceApplication.class, args);
    }

}
