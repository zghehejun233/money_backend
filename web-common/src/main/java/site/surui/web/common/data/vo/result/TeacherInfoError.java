package site.surui.web.common.data.vo.result;

public enum TeacherInfoError implements ResultError {
    FAIL_TO_EDIT(40300, "编辑失败"),
    INFO_NOT_FOUND(40301,"未查询到相关信息"),
    RPC_CALLING_FAILED(40302, "远程调用失败");

    private int code;

    private String message;


    TeacherInfoError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
