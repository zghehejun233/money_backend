package site.forum.web.teacher.data.enums;

import io.swagger.annotations.ApiModel;

/**
 * 职称枚举类
 * <p>
 * 参考资料：<a href="https://lib.xust.edu.cn/info/5880/3652.htm">https://lib.xust.edu.cn/info/5880/3652.htm</a>
 */
@ApiModel("职称枚举类")
public enum TitleEnum {
    TA("助教"), LECTURER("讲师"), ASSOCIATE_PROFESSOR("副教授"), PROFESSOR("教授");

    private final String level;

    TitleEnum(String level) {
        this.level = level;
    }

    public String getLevel() {
        return this.level;
    }
}
