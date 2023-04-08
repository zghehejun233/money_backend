package site.forum.web.teacher.data.enums;

import io.swagger.annotations.ApiModel;

@ApiModel("性别枚举类型")
public enum GenderEnum {
    MALE("男"), FEMALE("女");
    private String gender;

    GenderEnum(String gender) {
    }

    String getGender() {
        return this.gender;
    }
}
