package presenter;

import com.hyphenate.chat.EMMessage;

import java.util.List;

/**
 * Created by 我是小丑逼 on 2017/1/2.
 */

public interface chatPresenter {
    void sendMessage(String content,String toChatUsername);

    List<EMMessage> getMessageList();
}
