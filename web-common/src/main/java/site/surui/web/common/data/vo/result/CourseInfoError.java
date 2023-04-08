package site.surui.web.common.data.vo.result;

public enum CourseInfoError implements ResultError {

    INFO_NOT_FOUND(40300, "未查询到数据"),
    FAILED_TO_EDIT(40301, "数据修改失败"),
    ACCESS_ILLEGAL(40302, "Illegal access"),
    FAILED_TO_DELETE(40303, "删除失败"),
    ALREADY_SELECTED(40304, "已选择课程"),
    FAILED_TO_SELECT(40305, "选课失败");


    private final int code;
    private final String message;

    CourseInfoError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
