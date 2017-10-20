package ui.activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myqq.R;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import java.util.List;

import adapter.messageListAdapter;
import butterknife.BindView;
import butterknife.OnClick;
import presenter.chatPresenter;
import presenter.impl.chatPresenterImpl;
import utils.threadUtils;
import view.chatView;

/**
 * Created by 我是小丑逼 on 2017/1/2.
 */

public class chatActivity extends BaseActivity implements chatView,View.OnClickListener {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.message)
    EditText message;
    @BindView(R.id.send)
    Button send;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.back)
    ImageView mBack;

    private String mContact;
    private chatPresenter mChatPresenter;
    private messageListAdapter mMessageListAdapter;
    private ImageView add;
    private String mGroupId;
    private boolean isGroupManager;
    private PopupWindow popupWindow;

    @Override
    protected int getLayouResID() {
        return R.layout.activity_chat;
    }

    @Override
    protected void init() {
        super.init();
        add = (ImageView) findViewById(R.id.add);
        add.setOnClickListener(this);
        mBack.setVisibility(View.VISIBLE);
        add.setVisibility(View.VISIBLE);
        mContact = getIntent().getStringExtra("contact");
        mGroupId = getIntent().getStringExtra("groupId");
        isGroupManager = getIntent().getBooleanExtra("isGroupManager", false);
        mTitle.setText(mContact);
        message.addTextChangedListener(mTextWatcher);
        message.setOnEditorActionListener(moOnEditorActionListener);
        mChatPresenter = new chatPresenterImpl(this);
        initRecyclerView();
        EMClient.getInstance().chatManager().addMessageListener(emMessageListener);
    }

    private void initRecyclerView() {
        mMessageListAdapter = new messageListAdapter(this,mChatPresenter.getMessageList());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mMessageListAdapter);


    }

    @OnClick({R.id.send, R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send:
                sendMessage();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.add:
                showPopupWindow();
                break;
            case R.id.announcement:
                Intent intent = new Intent(this,AnnouncementActivity.class);
                intent.putExtra("groupId",mGroupId);
                intent.putExtra("isGroupManager",isGroupManager);
                startActivity(intent);
                popupWindow.dismiss();
                break;
            case R.id.class_file:
                Intent fileIntent = new Intent(this,ClassFileActivity.class);
                fileIntent.putExtra("groupId",mGroupId);
                fileIntent.putExtra("isGroupManager",isGroupManager);
                startActivity(fileIntent);
                popupWindow.dismiss();
                break;
            case R.id.class_photo:
                Intent photoIntent = new Intent(this,ClassPhotosActivity.class);
                photoIntent.putExtra("groupId",mGroupId);
                photoIntent.putExtra("isGroupManager",isGroupManager);
                startActivity(photoIntent);
                popupWindow.dismiss();
                break;
            case R.id.class_vote:
                Intent voteIntent = new Intent(this,VoteLListActivity.class);
                voteIntent.putExtra("groupId",mGroupId);
                voteIntent.putExtra("isGroupManager",isGroupManager);
                startActivity(voteIntent);
                popupWindow.dismiss();
                break;
        }
    }

    /**
     * 创建一个PopupWindow
     */
    private void showPopupWindow() {
        popupWindow = new PopupWindow(this);
        popupWindow.setWidth(getWindowManager().getDefaultDisplay().getWidth()/4);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        View contentView = View.inflate(this,R.layout.popup_window,null);
        contentView.findViewById(R.id.announcement).setOnClickListener(this);
        contentView.findViewById(R.id.class_file).setOnClickListener(this);
        contentView.findViewById(R.id.class_photo).setOnClickListener(this);
        contentView.findViewById(R.id.class_vote).setOnClickListener(this);
        popupWindow.setContentView(contentView);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.showAsDropDown(add,-50,getWindowManager().getDefaultDisplay().getHeight()/40);
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            send.setEnabled(s.toString().length() > 0);
        }
    };
    private TextView.OnEditorActionListener moOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if(actionId == EditorInfo.IME_ACTION_SEND){
                sendMessage();
            }
            return true;
        }


    };

    private void sendMessage() {
        String content = message.getText().toString().trim();
        mChatPresenter.sendMessage(content,mContact);
    }

    @Override
    public void onSendMessageSuccess() {

        Toast.makeText(this, getString(R.string.send_message_success), Toast.LENGTH_SHORT).show();
        mMessageListAdapter.notifyDataSetChanged();
        message.getEditableText().clear();

    }

    @Override
    public void onSendMessageFailed() {
        Toast.makeText(this, getString(R.string.send_message_failed), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStartSendMessage() {
        mMessageListAdapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(mChatPresenter.getMessageList().size()-1);
    }
    private EMMessageListener emMessageListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(final List<EMMessage> list) {
            threadUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    EMMessage emMessage = list.get(0);
                    if(emMessage.getFrom().equals(mContact)){
                        mMessageListAdapter.addMessage(emMessage);
                        recyclerView.smoothScrollToPosition(mChatPresenter.getMessageList().size()-1);
                    }
                }
            });
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
}
