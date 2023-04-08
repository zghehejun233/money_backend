package site.forum.web.auth.data.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

/**
 * tos: 对方的邮箱地址，可以是单个，也可以是多个（Collection表示）
 * subject：标题
 * content：邮件正文，可以是文本，也可以是HTML内容
 * isHtml： 是否为HTML，如果是，那参数3识别为HTML内容
 * files： 可选：附件，可以为多个或没有，将File对象加在最后一个可变参数中即可
 */
@Setter
@Getter
@AllArgsConstructor
public class MailDto {
    private String to;
    private String subject;
    private String content;
    private boolean isHtml;
    private File files;

    public MailDto(String to) {
        this.to = to;
        this.subject = "SDU WEB Design";
        this.content = "Hello world!";
        this.isHtml = false;
        this.files = null;
    }
}
