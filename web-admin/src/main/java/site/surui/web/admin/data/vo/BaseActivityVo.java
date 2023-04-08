package site.surui.web.admin.data.vo;

import lombok.Data;

@Data
public class BaseActivityVo {
    private Long id;
    private String name;
    private Integer type;
    private String typeName;
    private String time;
    private String beginTime;
    private String endTime;
    private String detail;
    private Integer status;
    private Long maintainer;
    private Float score;
    private String socialType;
    private String userName;

    // StudentWork
    private String position;
    private String department;

    // VoluntaryService
    private String serviceAgency;
    private String organizer;
    private String duration;

    // Research
    private String articleTitle;
    private String publication;
    private String meta;
    private String url;

    // Competition
    private String awardLevel;
    private String awardUnit;
    private String awardCertificate;
}