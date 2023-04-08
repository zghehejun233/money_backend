package site.forum.web.common.util;

import cn.hutool.core.date.DateUtil;
import com.alibaba.nacos.common.codec.Base64;
import com.squareup.okhttp.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;


@Slf4j
public class FileUtil {
    final String cloudUserName;
    final String cloudPassword;

    OkHttpClient client;

    public FileUtil(String username, String passwd) {
        client = new OkHttpClient();
        client.setReadTimeout(10, TimeUnit.SECONDS);
        client.setWriteTimeout(10, TimeUnit.SECONDS);
        client.setConnectTimeout(10, TimeUnit.SECONDS);
        cloudUserName = username;
        cloudPassword = passwd;
    }

    public String uploadAvatar(MultipartFile file, String uploadUrl, String directUrl, String userId) {
        byte[] bytes = null;
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            assert inputStream != null;
            bytes = inputStream.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        RequestBody requestBody;
        assert bytes != null;
        requestBody = RequestBody.create(MediaType.parse(file.getContentType()), bytes);

        String fileName = userId + "-" + file.getName() + DateUtil.now();
        fileName = new String(Base64.encodeBase64(fileName.getBytes(StandardCharsets.UTF_8))) + ContentTypeUtil.getType(file.getContentType());
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
