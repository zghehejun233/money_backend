package site.surui.web.common.data.vo.result;

public enum StudentInfoError implements ResultError {

    STUDENT_NOT_FOUND(400200, "未找到学生信息，请检查学号或是否同步"),
    STUDENT_SYNC_FAIL(40201, "学生信息同步失败"),
    STUDENT_NOT_INITIALIZED(40202, "学生账号未初始化"),
    INFO_NOT_FOUND(40203, "未查询到相关信息"),
    FAIL_TO_DELETE(40204, "删除数据失败"),
    FAIL_TO_EDIT_INFO(40205, "为对数据进行修改");


    private final int code;
    private final String message;

    StudentInfoError(int code, String message) {
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
