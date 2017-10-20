package view;

/**
 * Created by clever boy on 2017/9/23.
 */

public interface CreateClassView {
    /**
     *创建班级成功
     */
    void CreateClassSuccess();

    /**
     *创建班级失败
     */
    void CreateClassfailed();

    /**
     * 班级已存在
     */
    void classExited();

    /**
     * 班级数量太大
     */
    void classOutnumber();
}
