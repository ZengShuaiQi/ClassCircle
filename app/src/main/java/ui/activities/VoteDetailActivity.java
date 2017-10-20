package ui.activities;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myqq.R;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import utils.threadUtils;

/**
 * Created by clever boy on 2017/10/20.
 */

public class VoteDetailActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private ImageView back;
    private TextView voteTitle;
    private TextView option1;
    private TextView option2;
    private TextView option3;
    private TextView option4;
    private TextView option5;
    private Button postPoll;
    private int count = 2;
    private String mGroupId;
    private String type;
    private List<String> members;
    private String[] options;


    @Override
    protected int getLayouResID() {
        return R.layout.activity_vote_detail;
    }

    @Override
    protected void init() {
        super.init();
        initView();
        uploadVote();
    }

    private void initView() {
        title = (TextView) findViewById(R.id.title);
        back = (ImageView) findViewById(R.id.back);
        voteTitle = (TextView) findViewById(R.id.et_vote);
        option1 = (TextView) findViewById(R.id.option1);
        option2 = (TextView) findViewById(R.id.option2);
        option3 = (TextView) findViewById(R.id.option3);
        option4 = (TextView) findViewById(R.id.option4);
        option5 = (TextView) findViewById(R.id.option5);
        postPoll = (Button) findViewById(R.id.post_poll);

        mGroupId = getIntent().getStringExtra("groupId");
        type = getIntent().getStringExtra("type");
        options = getIntent().getStringArrayExtra("count");
        count = options.length;
        switch (count) {

            case 3:
                option3.setVisibility(View.VISIBLE);

                break;
            case 4:
                option3.setVisibility(View.VISIBLE);
                option4.setVisibility(View.VISIBLE);

                break;
            case 5:
                option3.setVisibility(View.VISIBLE);
                option4.setVisibility(View.VISIBLE);
                option5.setVisibility(View.VISIBLE);

                break;
        }

        back.setVisibility(View.VISIBLE);
        title.setText("投票结果");
        back.setOnClickListener(this);
        postPoll.setOnClickListener(this);
        option1.setOnClickListener(this);
        option2.setOnClickListener(this);
        option3.setOnClickListener(this);
        option4.setOnClickListener(this);
        option5.setOnClickListener(this);
        clickDisable();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.option1:
                vote("option1");
                break;
            case R.id.option2:
                vote("option2");

                break;
            case R.id.option3:
                vote("option3");

                break;
            case R.id.option4:
                vote("option4");

                break;
            case R.id.option5:
                vote("option5");
                break;

        }
    }

    private void uploadVote() {

        threadUtils.runOnBackgroudThread(new Runnable() {
            @Override
            public void run() {


                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(mGroupId);
                List<EMMessage> messages = conversation.loadMoreMsgFromDB(null, conversation.getAllMsgCount());
                for (EMMessage message : messages) {
                    try {
                        if (message.getStringAttribute("type").equals(type)) {
                            EMTextMessageBody body = (EMTextMessageBody) message.getBody();
                            String message1 = body.getMessage();
                             switch (message1){
                                          case "option1":
                                          break;
                                         }
                            if (EMClient.getInstance().getCurrentUser().equals(message.getFrom())){

                            }else{

                            }
                        }

                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                }

            }

        });

    }
    private void vote(final String option){
        threadUtils.runOnBackgroudThread(new Runnable() {
            @Override
            public void run() {
                EMMessage message = EMMessage.createTxtSendMessage(option, mGroupId);
                message.setChatType(EMMessage.ChatType.GroupChat);
                message.setMessageStatusCallback(new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        threadUtils.runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                clickDisable();
                                Toast.makeText(VoteDetailActivity.this, "投票成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onError(int i, String s) {
                        threadUtils.runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(VoteDetailActivity.this, "投票失败", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
            }
        });
    }
    private void clickDisable(){
        option1.setClickable(false);
        option2.setClickable(false);
        option3.setClickable(false);
        option4.setClickable(false);
        option5.setClickable(false);
    }
    private void clickAble(){
        option1.setClickable(true);
        option2.setClickable(true);
        option3.setClickable(true);
        option4.setClickable(true);
        option5.setClickable(true);
    }

}
