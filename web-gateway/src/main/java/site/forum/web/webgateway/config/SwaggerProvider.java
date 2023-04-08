package site.forum.web.webgateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Primary
@Component
public class SwaggerProvider implements SwaggerResourcesProvider {

    /**
     * 网关路由
     */
    @Resource
    private RouteLocator routeLocator;

    @Value("${spring.application.name}")
    private String applicationName;


    /**
     * 聚合其他服务接口
     */
    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        List<String> routes = new ArrayList<>();
        // 获取所有可用的应用名称
        routeLocator.getRoutes()
                .filter(route -> route.getUri().getHost() != null)
                .filter(route -> !applicationName.equals(route.getUri().getHost()))
                .subscribe(route -> routes.add(route.getUri().getHost()));

        // 去重，多负载服务只添加一次
        Set<String> existsServer = new HashSet<>();
        routes.forEach(host -> {
            // 拼接url
            String url = "/" + host + "/v2/api-docs";
            //不存在则添加
            if (!existsServer.contains(url)) {
                existsServer.add(url);
                SwaggerResource swaggerResource = new SwaggerResource();
                swaggerResource.setUrl(url);
                swaggerResource.setName(host);
                swaggerResource.setSwaggerVersion("2.0");
                resources.add(swaggerResource);
            }
        });
        return resources;
    }
}
