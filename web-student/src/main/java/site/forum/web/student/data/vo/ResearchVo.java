package site.forum.web.student.data.vo;

import lombok.Data;

@Data
public class ResearchVo {
    private Long id;
    private String name;
    private String time;
    private Float score;
    // 详细描述
    private String detail;
    private Integer status;
    // 文章标题
    private String articleTitle;
    // 发表刊物
    private String publication;
    // meta
    private String meta;
    // url
    private String url;
}
