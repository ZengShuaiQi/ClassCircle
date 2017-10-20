package ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myqq.R;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMMucSharedFile;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import adapter.InformListAdapter;
import utils.PreUtils;
import utils.threadUtils;

/**
 * Created by clever boy on 2017/9/26.
 */

public class AnnouncementActivity extends BaseActivity implements View.OnClickListener {

    private String mGroupId;
    private ImageView back;
    private ImageView add;
    private TextView title;
    private ListView listView;
    private List<EMMessage> msgList = new ArrayList<EMMessage>();
    private InformListAdapter listAdapter;
    private boolean isfrist;


    @Override
    protected int getLayouResID() {
        return R.layout.announcement_activity;
    }

    @Override
    protected void init() {
        super.init();
        back = (ImageView) findViewById(R.id.back);
        add = (ImageView) findViewById(R.id.add);
        title = (TextView) findViewById(R.id.title);
        listView = (ListView) findViewById(R.id.inform_list);

        title.setText("通知");
        mGroupId = getIntent().getStringExtra("groupId");
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(this);
        add.setOnClickListener(this);
        boolean isGroupManager = getIntent().getBooleanExtra("isGroupManager", false);
        if (isGroupManager) {
            add.setVisibility(View.VISIBLE);
        } else {
            add.setVisibility(View.INVISIBLE);
        }
        EMClient.getInstance().chatManager().addMessageListener(emMessageListener);
        isfrist = PreUtils.getBoolean(mGroupId, false, getApplicationContext());
        if (isfrist) {
            uploadInform();
        }

        listAdapter = new InformListAdapter(this, msgList);
        listView.setAdapter(listAdapter);
    }


    private void uploadInform() {

        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(mGroupId);

        List<EMMessage> list = conversation.loadMoreMsgFromDB(null, conversation.getAllMsgCount());


        if (list != null) {
            for (EMMessage emMessage : list) {
                try {
                    String type = emMessage.getStringAttribute("type");
                    if (type.equals("announcement")) {
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
                Intent intent = new Intent(this, SendInformActivity.class);
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
                        if (emMessage.getStringAttribute("type").equals("announcement")) {
                            msgList.add(emMessage);
                            listAdapter.notifyDataSetChanged();
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
            uploadInform();
            listAdapter.notifyDataSetChanged();
        }
    }
}
