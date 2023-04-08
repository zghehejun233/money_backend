package site.surui.web.student.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import site.surui.web.common.data.vo.result.Result;

@FeignClient(value = "web-auth")
public interface AuthService {
    @GetMapping("/user/{token}")
    Result<?> getUserInfo(@PathVariable("token") String token);
}
