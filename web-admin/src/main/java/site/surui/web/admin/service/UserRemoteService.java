package site.surui.web.admin.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import site.surui.web.common.data.po.User;
import site.surui.web.common.data.vo.result.Result;

import java.util.List;

@FeignClient("web-auth")
public interface UserRemoteService {
    @PostMapping("/user/reset")
    Result<?> resetUserPassword(@RequestParam("id") String id,
                                @RequestParam("password") String password);

    @PostMapping("/user/add")
    Result<?> addUser(@RequestBody List<User> users);
}
