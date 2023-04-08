package site.surui.web.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Guo Surui
 */
@SpringBootApplication
@MapperScan("site.surui.web.auth.mapper")
public class WebAuthApplication {

    public static void main(String[] args) {SpringApplication.run(WebAuthApplication.class, args);}

}
