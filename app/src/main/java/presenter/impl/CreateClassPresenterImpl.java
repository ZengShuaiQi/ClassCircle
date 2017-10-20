package presenter.impl;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroupInfo;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMGroupOptions;
import com.hyphenate.exceptions.HyphenateException;
import com.superrtc.call.ThreadUtils;

import java.util.ArrayList;
import java.util.List;

import presenter.CreateClassPresenter;
import utils.threadUtils;
import view.CreateClassView;

/**
 * Created by clever boy on 2017/9/23.
 */

public class CreateClassPresenterImpl implements CreateClassPresenter {

    private CreateClassView createClassView;
    private List<EMGroupInfo> mGroupList;
    private List<String> mGroupNameList;


    public CreateClassPresenterImpl(CreateClassView view) {
        createClassView = view;
    }

    @Override
    public void createClass(final String groupName, final String desc, final int count) {
        if (count > 200) {
            createClassView.classOutnumber();
            return;
        } else if (count > 0 && count < 200) {
            threadUtils.runOnBackgroudThread(new Runnable() {
                @Override
                public void run() {
                    String cursor = null;
                    int pageSize = 20;
                    try {
                        EMCursorResult<EMGroupInfo> result = EMClient.getInstance().groupManager().getPublicGroupsFromServer(pageSize, cursor);//需异步处理
                        mGroupList = result.getData();
                        mGroupNameList = new ArrayList<String>();
                        if (mGroupList.size() > 0) {
                            for (EMGroupInfo emGroupInfo : mGroupList) {
                                mGroupNameList.add(emGroupInfo.getGroupName());
                            }
                        }
                        if (mGroupNameList.contains(groupName)) {
                            threadUtils.runOnMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    createClassView.classExited();
                                }
                            });
                            return;
                        } else {
                            EMGroupOptions option = new EMGroupOptions();
                            option.maxUsers = count;
                            option.style = EMGroupManager.EMGroupStyle.EMGroupStylePublicOpenJoin;
                            EMClient.getInstance().groupManager().createGroup(groupName, desc, new String[]{}, null, option);
                            threadUtils.runOnMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    createClassView.CreateClassSuccess();
                                }
                            });

                        }
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        threadUtils.runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                createClassView.CreateClassfailed();
                            }
                        });
                    }
                }
            });

        }


    }
}
