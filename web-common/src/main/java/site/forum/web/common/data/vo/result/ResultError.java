package site.forum.web.common.data.vo.result;

/**
 * @Author skyyemperor
 * Created by skyyemperor on 2021-01-30
 * Description : 通用异常返回的父接口
 */
public interface ResultError {

    /**
     * 获取错误码
     *
     * @return 错误码
     */
    int getCode();

    /**
     * 获取错误信息
     *
     * @return 错误信息
     */
    String getMessage();

}
