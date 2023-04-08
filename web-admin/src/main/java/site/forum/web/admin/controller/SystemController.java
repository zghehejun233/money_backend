package site.forum.web.admin.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.forum.web.admin.data.vo.InstanceVo;
import site.forum.web.admin.service.SystemInfoService;
import site.forum.web.common.annotation.AccessLimit;
import site.forum.web.common.data.vo.result.Result;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/system")
@Api("获取系统运行状态")
@RefreshScope
public class SystemController {
    @Resource
    private DiscoveryClient discoveryClient;
    @Resource
    private SystemInfoService systemInfoService;



    @ApiOperation("获取服务实例")
    @RequestMapping(value = "/service/instances", method = RequestMethod.GET)
    public Result<?> getServiceInstances() {
        List<String> serviceNames = discoveryClient.getServices();
        Map<String, List<InstanceVo>> result = new HashMap<>();
        serviceNames.forEach(name -> {
            List<ServiceInstance> instanceList = discoveryClient.getInstances(name);
            List<InstanceVo> instances = new ArrayList<>();
            instanceList.forEach(instance -> {
                InstanceVo instanceVo = new InstanceVo();
                instanceVo.setInstanceId(instance.getInstanceId());
                instanceVo.setIp(instance.getHost());
                instanceVo.setPort(instance.getPort());
                instanceVo.setStatus(instance.getMetadata().get("nacos.healthy"));
                instanceVo.setWeight(instance.getMetadata().get("nacos.weight"));

                instances.add(instanceVo);
            });
            result.put(name, instances);
        });

        return new Result<>().success(result);
    }

    int s = 10;
    @ApiOperation("获取系统运行规模信息")
    @RequestMapping(value = "/scale", method = RequestMethod.GET)
    @AccessLimit(maxCount = 10)
    public Result<?> getSystemScale(@RequestParam(value = "length",required = false) Integer length) {
        return systemInfoService.getSystemScale(length);
    }


    @ApiOperation("删除系统运行规模信息")
    @RequestMapping(value = "/scale", method = RequestMethod.DELETE)
    public Result<?> deleteSystemScale() {
        return systemInfoService.forceClearCache();
    }
}
