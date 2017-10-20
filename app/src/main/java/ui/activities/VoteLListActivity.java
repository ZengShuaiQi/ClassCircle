package ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myqq.R;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMucSharedFile;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.FileUtils;
import com.hyphenate.util.PathUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import adapter.FileListAdapter;
import adapter.OptionListAdapter;
import utils.PreUtils;
import utils.threadUtils;

/**
 * Created by clever boy on 2017/10/17.
 */

public class VoteLListActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private ImageView add;
    private TextView title;
    private String mGroupId;
    private boolean isGroupManager;
    private ListView listView;
    private OptionListAdapter adapter;
    private List<EMMessage> list = new ArrayList<>();

    @Override
    protected int getLayouResID() {
        return R.layout.activity_vote_list;
    }

    @Override
    protected void init() {
        super.init();
        back = (ImageView) findViewById(R.id.back);
        add = (ImageView) findViewById(R.id.add);
        title = (TextView) findViewById(R.id.title);
        listView = (ListView) findViewById(R.id.vote_list);
        title.setText("投票");
        isGroupManager = getIntent().getBooleanExtra("isGroupManager", false);
        if (isGroupManager) {
            add.setVisibility(View.VISIBLE);
        } else {
            add.setVisibility(View.INVISIBLE);
        }
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(this);
        add.setOnClickListener(this);
        mGroupId = getIntent().getStringExtra("groupId");


        adapter = new OptionListAdapter(this);
        adapter.setList(list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] count = adapter.getCount(position);
                String type = adapter.getType(position);
                Intent intent = new Intent(getApplicationContext(),VoteDetailActivity.class);
                intent.putExtra("count",count);
                intent.putExtra("groupId",mGroupId);
                intent.putExtra("type",type);
                startActivity(intent);
            }
        });
        boolean isfrist = PreUtils.getBoolean(mGroupId, false, getApplicationContext());
        if(isfrist){
            uploadFileList();
        }

//        title.setText(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath().toString());
    }

    private void uploadFileList() {

                    EMConversation conversation = EMClient.getInstance().chatManager().getConversation(mGroupId);
                    List<EMMessage> messages = conversation.loadMoreMsgFromDB(null, conversation.getAllMsgCount());
                    for (EMMessage emMessage: messages) {
                        try {
                        String flag = emMessage.getStringAttribute("flag");
                        if(flag.equals("count")){
                            list.add(emMessage);
                        }
                    } catch (HyphenateException e) {
                        e.printStackTrace();

                    }
                    }

                            adapter.setList(list);
                            adapter.notifyDataSetChanged();



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.add:
                Intent intent = new Intent(this,PostPollActivity.class);
                intent.putExtra("groupId",mGroupId);
                startActivity(intent);
                break;
        }
    }

    public boolean getIsGroupManager(){
        return isGroupManager;
    }
}
