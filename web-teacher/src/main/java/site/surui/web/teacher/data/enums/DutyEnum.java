package site.surui.web.teacher.data.enums;

import io.swagger.annotations.ApiModel;

@ApiModel("职务枚举类")
public enum DutyEnum {
    PRESIDENT("校长"), VICE_PRESIDENT("副校长"), DEAN("院长"), FACULTY("教职员工"), STAFF("行政员工"), RESEARCH_FELLOW("研究员");

    private final String duty;

    DutyEnum(String duty) {
        this.duty = duty;
    }

    public String getDuty() {
        return duty;
    }
}
