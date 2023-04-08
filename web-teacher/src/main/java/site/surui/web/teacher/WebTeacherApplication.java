package site.surui.web.teacher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class WebTeacherApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebTeacherApplication.class, args);
    }

}
