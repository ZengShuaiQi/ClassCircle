package presenter.impl;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupInfo;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

import presenter.addFriendPresenter;
import utils.threadUtils;
import view.addFriendView;

/**
 * Created by 我是小丑逼 on 2017/1/5.
 */

public class addFriendPresenterImpl implements addFriendPresenter {
    private addFriendView mAddClassView;

    private List<EMGroup> searchGroup;

    public List<EMGroup> getSearchGroup() {
        return searchGroup;
    }

    @Override
    public void addClass(final int position) {
        threadUtils.runOnBackgroudThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().groupManager().joinGroup(searchGroup.get(position).getGroupId());
                    threadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mAddClassView.addClassSucess();
                        }
                    });
                } catch (HyphenateException e) {
                    threadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mAddClassView.addClassFailed();
                        }
                    });
                    e.printStackTrace();

                }
            }
        });
    }

    public addFriendPresenterImpl(addFriendView mAddClassView){
        this.mAddClassView = mAddClassView;

    }

    @Override
    public void searchfriend(final String keyword) {
        threadUtils.runOnBackgroudThread(new Runnable() {
            @Override
            public void run() {
                try {
                    //获取群组集合
                    EMCursorResult<EMGroupInfo> result = EMClient.getInstance().groupManager().getPublicGroupsFromServer(20, null);//需异步处理
                    List<EMGroupInfo> data = result.getData();
                    searchGroup = new ArrayList<EMGroup>();
                    for (EMGroupInfo emGroupInfo:data) {
                        if(emGroupInfo.getGroupName().contains(keyword)){
                            EMGroup group = EMClient.getInstance().groupManager().getGroupFromServer(emGroupInfo.getGroupId());
                            searchGroup.add(group);
                        }
                    }
                    threadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mAddClassView.updateSearchList();
                        }
                    });

                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });


    }

}
