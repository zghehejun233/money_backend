package site.surui.web.auth.util;

import cn.hutool.core.date.DateUtil;
import com.alibaba.nacos.common.codec.Base64;
import com.squareup.okhttp.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;


@Slf4j
public class FileUtil {
    final String cloudUserName;
    final String cloudPassword;

    final OkHttpClient client;

    public FileUtil(String username, String passwd) {
        client = new OkHttpClient();
        client.setReadTimeout(10, TimeUnit.SECONDS);
        client.setWriteTimeout(10, TimeUnit.SECONDS);
        client.setConnectTimeout(10, TimeUnit.SECONDS);
        cloudUserName = username;
        cloudPassword = passwd;
    }

    public String uploadCode(byte[] file, String uploadUrl, String directUrl, String userId) {

        RequestBody requestBody;
        assert file != null;
        requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);

        String fileName = userId + "-code" + DateUtil.now();
        fileName = new String(Base64.encodeBase64(fileName.getBytes(StandardCharsets.UTF_8))) + ".jpg";
        uploadUrl = uploadUrl + fileName;
        directUrl = directUrl + fileName;

        Request request = new Request.Builder()
                .url(uploadUrl)
                .addHeader("Authorization", buildAuthHeader())
                .put(requestBody)
                .build();

        Response response;
        try {
            response = client.newCall(request).execute();
            if (response.code() >= 300) {
                log.warn("上传文件失败，状态码：{}", response.code());
                directUrl = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return directUrl;
    }

    private String buildAuthHeader() {
        String auth = cloudUserName + ":" + cloudPassword;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.US_ASCII));
        return "Basic " + new String(encodedAuth);
    }
}
