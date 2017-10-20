package ui.activities;

import android.content.Intent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myqq.R;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

import adapter.InformListAdapter;
import adapter.PhotoListAdapter;
import utils.PreUtils;

/**
 * Created by clever boy on 2017/10/7.
 */

public class ClassPhotosActivity extends BaseActivity implements View.OnClickListener {

    private ImageView back;
    private ImageView add;
    private TextView title;
    private String mGroupId;
    private List<EMMessage> msgList = new ArrayList<EMMessage>();
    private boolean isfrist;
    private PhotoListAdapter adapter;
    private GridView gridView;


    @Override
    protected int getLayouResID() {
        return R.layout.activity_class_photos;
    }

    @Override
    protected void init() {
        super.init();
        back = (ImageView) findViewById(R.id.back);
        add = (ImageView) findViewById(R.id.add);
        title = (TextView) findViewById(R.id.title);
        gridView = (GridView) findViewById(R.id.gdv_class_photo);
        title.setText("班级相册");
        back.setVisibility(View.VISIBLE);
        boolean isGroupManager = getIntent().getBooleanExtra("isGroupManager", false);
        if (isGroupManager) {
            add.setVisibility(View.VISIBLE);
        } else {
            add.setVisibility(View.INVISIBLE);
        }
        mGroupId = getIntent().getStringExtra("groupId");
        back.setOnClickListener(this);
        add.setOnClickListener(this);
        isfrist = PreUtils.getBoolean(mGroupId, false, getApplicationContext());
        if (isfrist) {
            uploadPhoto();
        }
        adapter = new PhotoListAdapter(this, msgList);
        gridView.setAdapter(adapter);
        EMClient.getInstance().chatManager().addMessageListener(emMessageListener);
    }

    private void uploadPhoto() {

                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(mGroupId);

                List<EMMessage> list = conversation.loadMoreMsgFromDB(null, conversation.getAllMsgCount());

                if (list != null) {
                    for (EMMessage emMessage : list) {
                        try {
                            String type = emMessage.getStringAttribute("type");
                            if (type.equals("photo")) {

                                    msgList.add(emMessage);

                            }
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                        }
                    }
                }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.add:
                Intent intent = new Intent(this, SendPhotoActivity.class);
                intent.putExtra("groupId", mGroupId);
                startActivity(intent);
                break;
        }
    }

    private EMMessageListener emMessageListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> list) {
            if(!isfrist){
                if(list.get(0).conversationId().equals(mGroupId)){
                    PreUtils.putBoolean(mGroupId, true, getApplicationContext());
                }
            }
            if (list != null) {
                for (EMMessage emMessage : list) {
                    try {
                        if (emMessage.getStringAttribute("type").equals("photo")) {
                                msgList.add(emMessage);
                                adapter.notifyDataSetChanged();
                        }
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageRead(List<EMMessage> list) {

        }

        @Override
        public void onMessageDelivered(List<EMMessage> list) {

        }

        @Override
        public void onMessageRecalled(List<EMMessage> list) {

        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {

        }
    };
    @Override
    protected void onRestart() {
        super.onResume();
        if (isfrist) {
            msgList.clear();
            uploadPhoto();
            adapter.notifyDataSetChanged();
        }
    }
}
