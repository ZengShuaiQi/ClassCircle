package ui.activities;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myqq.R;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMGroupOptions;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import utils.PreUtils;
import utils.threadUtils;

/**
 * Created by clever boy on 2017/10/18.
 */

public class PostPollActivity extends BaseActivity implements View.OnClickListener {

    private TextView title;
    private ImageView back;
    private EditText voteTitle;
    private EditText option1;
    private EditText option2;
    private EditText option3;
    private EditText option4;
    private EditText option5;
    private Button postPoll;
    private TextView addOption;
    private int count = 1;
    private String mGroupId;
    private List<String> members;
    private String id1;
    private boolean isfrist;
    private int add = 1;


    @Override
    protected int getLayouResID() {
        return R.layout.activity_post_poll;
    }

    @Override
    protected void init() {
        super.init();
        initView();


    }

    private void initView() {
        title = (TextView) findViewById(R.id.title);
        back = (ImageView) findViewById(R.id.back);
        voteTitle = (EditText) findViewById(R.id.et_vote);
        option1 = (EditText) findViewById(R.id.option1);
        option2 = (EditText) findViewById(R.id.option2);
        option3 = (EditText) findViewById(R.id.option3);
        option4 = (EditText) findViewById(R.id.option4);
        option5 = (EditText) findViewById(R.id.option5);
        addOption = (TextView) findViewById(R.id.add_option);
        postPoll = (Button) findViewById(R.id.post_poll);

        back.setVisibility(View.VISIBLE);
        title.setText("发布投票");
        addOption.setOnClickListener(this);
        back.setOnClickListener(this);
        postPoll.setOnClickListener(this);
        mGroupId = getIntent().getStringExtra("groupId");
        isfrist = PreUtils.getBoolean(mGroupId, false, getApplicationContext());


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.add_option:
                if (add % 3 == 1) {
                    option3.setVisibility(View.VISIBLE);
                    add++;
                } else if (add % 3 == 2) {
                    option4.setVisibility(View.VISIBLE);
                    add++;
                } else if (add % 3 == 0) {
                    option5.setVisibility(View.VISIBLE);
                    add++;
                }
                break;
            case R.id.post_poll:

                uploadVote();
                break;

        }
    }

    private void uploadVote() {
        final String subject = voteTitle.getText().toString();
        final String EtOption1 = option1.getText().toString();
        final String EtOption2 = option2.getText().toString();
        final String EtOption3 = option3.getText().toString();
        final String EtOption4 = option4.getText().toString();
        final String EtOption5 = option5.getText().toString();

        threadUtils.runOnBackgroudThread(new Runnable() {
            @Override
            public void run() {

                if(isfrist){
                    EMConversation conversation = EMClient.getInstance().chatManager().getConversation(mGroupId);
                    final int allMsgCount = conversation.getAllMsgCount();
                    threadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PostPollActivity.this, allMsgCount+"", Toast.LENGTH_SHORT).show();
                        }
                    });
                    List<EMMessage> messages = conversation.loadMoreMsgFromDB(null, conversation.getAllMsgCount());
                    for (EMMessage message: messages) {
                        try {
                            if((message.getStringAttribute("type").substring(0,4)).equals("vote")){
                                if(message.getStringAttribute("flag").equals("count")){
                                    count++;
                                }
                            }

                        } catch (HyphenateException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (!TextUtils.isEmpty(subject)) {
                    EMMessage message = EMMessage.createTxtSendMessage(subject, mGroupId);
                    message.setAttribute("flag","count");
                    message.setAttribute("type", "vote"+count);
                    if (!TextUtils.isEmpty(EtOption1)) {
                        message.setAttribute("option1", EtOption1);
                    } else {
                        threadUtils.runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(PostPollActivity.this, "必须有两个选项", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        });
                    }
                    if (!TextUtils.isEmpty(EtOption2)) {
                        message.setAttribute("option2", EtOption2);
                    } else {
                        threadUtils.runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(PostPollActivity.this, "必须有两个选项", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        });
                    } if (!TextUtils.isEmpty(EtOption3)) {
                        message.setAttribute("option3", EtOption3);
                    }
                    if (!TextUtils.isEmpty(EtOption4)) {
                        message.setAttribute("option4", EtOption4);
                    }
                    if (!TextUtils.isEmpty(EtOption5)) {
                        message.setAttribute("option5", EtOption5);

                    }
                    message.setChatType(EMMessage.ChatType.GroupChat);
                    message.setMessageStatusCallback(new EMCallBack() {
                        @Override
                        public void onSuccess() {
                            threadUtils.runOnMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    PreUtils.putBoolean(mGroupId,true,getApplicationContext());
                                    Toast.makeText(PostPollActivity.this, "发布成功", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }

                        @Override
                        public void onError(int i, String s) {
                            threadUtils.runOnMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(PostPollActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            });
                        }

                        @Override
                        public void onProgress(int i, String s) {

                        }
                    });
                    EMClient.getInstance().chatManager().sendMessage(message);
                } else {
                    threadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PostPollActivity.this, "标题不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    });
                }

            }

        });


    }


}
