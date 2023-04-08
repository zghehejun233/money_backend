package site.forum.web.webcourse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class WebCourseApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebCourseApplication.class, args);
    }

}
