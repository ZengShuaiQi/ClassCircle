package presenter;

import com.hyphenate.chat.EMClient;

/**
 * Created by clever boy on 2017/9/23.
 */

public interface CreateClassPresenter {
    /**
     *创建班级
     * @param groupName
     * @param desc
     */
    void createClass(String groupName,String desc,int count);

}
