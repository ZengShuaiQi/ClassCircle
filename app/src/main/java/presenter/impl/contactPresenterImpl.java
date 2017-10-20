package presenter.impl;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import model.contactListItem;
import presenter.contactPresenter;
import utils.threadUtils;
import view.contactView;

/**
 * Created by 我是小丑逼 on 2017/1/2.
 */

public class contactPresenterImpl implements contactPresenter {
    private contactView mContactView;
    private List<contactListItem> mContactListItems;
    private boolean isCompleted = false;

    public contactPresenterImpl(contactView contactView){
        mContactView = contactView;
        mContactListItems = new ArrayList<contactListItem>();
    }

    @Override
    public void loadContacts() {
        threadUtils.runOnBackgroudThread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<EMGroup> grouplist = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();//需异步处理
                    String currentUser = EMClient.getInstance().getCurrentUser();
                    for (int i = 0;i < grouplist.size();i++){
                        contactListItem item = new contactListItem();
                        item.contact = grouplist.get(i).getGroupName();
                        item.groupId = grouplist.get(i).getGroupId();
                        String owner = grouplist.get(i).getOwner();
                        List<String> adminList = grouplist.get(i).getAdminList();
                        if(currentUser.equals(owner) || adminList.contains(currentUser)){
                            item.isGroupManager = true;
                        }else{
                            item.isGroupManager = false;
                        }
                        mContactListItems.add(item);
                    }
                    Collections.sort(mContactListItems, new Comparator<contactListItem>() {
                        @Override
                        public int compare(contactListItem o1, contactListItem o2) {
                            return o1.contact.charAt(0) - o2.contact.charAt(0);
                        }
                    });

                    for (int i = 0; i < mContactListItems.size(); i++) {
                        contactListItem item = mContactListItems.get(i);
                        if(i>0&&item.getFirstLetter().equals(mContactListItems.get(i-1).getFirstLetter())){
                            item.showFirstLetter = false;
                        }
                    }

                    threadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mContactView.onLoadContactsSuccess();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    threadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mContactView.onLoadContactsFailed();
                        }
                    });
                }
            }
        });


    }

    @Override
    public List<contactListItem> getDataList() {
        return mContactListItems;
    }

    @Override
    public void refresh() {
        mContactListItems.clear();
        loadContacts();
    }
}
