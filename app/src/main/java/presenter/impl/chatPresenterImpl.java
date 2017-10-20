package presenter.impl;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.List;

import presenter.chatPresenter;
import utils.threadUtils;
import view.chatView;

/**
 * Created by 我是小丑逼 on 2017/1/2.
 */

public class chatPresenterImpl implements chatPresenter {

    private chatView mChatView;
    private List<EMMessage> emMessages;


    public chatPresenterImpl(chatView chatView){
        emMessages = new ArrayList<EMMessage>();
        mChatView = chatView;

    }
    @Override
    public void sendMessage(final String content, final String toChatUsername) {
        threadUtils.runOnBackgroudThread(new Runnable() {
            @Override
            public void run() {
                EMMessage emMessage = EMMessage.createTxtSendMessage(content,toChatUsername);
                emMessage.setMessageStatusCallback(emCallBack);
                emMessages.add(emMessage);
                threadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        mChatView.onStartSendMessage();
                    }
                });
                EMClient.getInstance().chatManager().sendMessage(emMessage);
            }
        });
    }

    @Override
    public List<EMMessage> getMessageList() {
        return emMessages;
    }

    private EMCallBack emCallBack = new EMCallBack() {
        @Override
        public void onSuccess() {
            threadUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    mChatView.onSendMessageSuccess();
                }
            });

        }

        @Override
        public void onError(int i, String s) {
            threadUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    mChatView.onSendMessageFailed();
                }
            });

        }

        @Override
        public void onProgress(int i, String s) {

        }
    };
}
