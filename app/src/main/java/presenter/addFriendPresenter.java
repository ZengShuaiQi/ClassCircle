package presenter;

import com.hyphenate.chat.EMGroup;

import java.util.List;

/**
 * Created by 我是小丑逼 on 2017/1/5.
 */

public interface addFriendPresenter {
    //搜索班级
    void searchfriend(String keyword);
    //获取公开的班级列表
    List<EMGroup> getSearchGroup();
    //加入班级
    void addClass(int position);

}
