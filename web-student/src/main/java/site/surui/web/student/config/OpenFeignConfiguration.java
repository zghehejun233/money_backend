package site.surui.web.student.config;


import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import lombok.extern.slf4j.Slf4j;
import site.surui.web.common.data.vo.result.Result;

import java.io.IOException;
import java.lang.reflect.Type;


@Slf4j
public class OpenFeignConfiguration implements Decoder {
    @Override
    public Object decode(Response response, Type type) throws IOException, FeignException {
        log.debug("Customized decoder working...");
        if (response.body() == null) {
            log.warn("Body is empty");
            throw new DecodeException(response.status(), "没有返回有效的数据", response.request());
        }
        String bodyStr = Util.toString(response.body().asReader(Util.UTF_8));
        log.debug("bodyStr: {}", bodyStr);
        //对结果进行转换
        Result<?> result =  json2obj(bodyStr, type);
        log.debug("result: {}", result);
        //如果返回错误，且为内部错误，则直接抛出异常
        if (result.getCode() != 0) {
            log.warn("Wrong status code");
            throw new DecodeException(response.status(), "接口返回错误：" + result.getMessage(), response.request());
        }
        return result;
    }

    public static <T> T json2obj(String jsonStr, Type targetType) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JavaType javaType = TypeFactory.defaultInstance().constructType(targetType);
            return mapper.readValue(jsonStr, javaType);
        } catch (IOException e) {
            throw new IllegalArgumentException("将JSON转换为对象时发生错误:" + jsonStr, e);
        }
    }
}
