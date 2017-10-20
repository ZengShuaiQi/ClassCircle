package ui.activities;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myqq.R;
import com.hyphenate.chat.EMGroup;

import java.util.List;

import adapter.addClassListAdapter;
import butterknife.BindView;
import butterknife.OnClick;
import presenter.addFriendPresenter;
import presenter.impl.addFriendPresenterImpl;
import view.addFriendView;
import widgets.DividerItemDecoration;

/**
 * Created by 我是小丑逼 on 2017/1/3.
 */

public class addFriendActivity extends BaseActivity implements addFriendView {
    @BindView(R.id.keyword)
    EditText mKeyword;
    @BindView(R.id.search_friend)
    Button mSearchFriend;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.back)
    ImageView btn_back;
    @BindView(R.id.title)
    TextView mTitle;
    private List<EMGroup> data;
    private addFriendPresenter mAddFriendPresenter;
    private adapter.addClassListAdapter addClassListAdapter;
    private List<EMGroup> searchGroup;

    @Override
    protected int getLayouResID() {
        return R.layout.activity_add_class;
    }

    @Override
    protected void init() {
        super.init();
        mTitle.setText(getString(R.string.add_class));
        btn_back.setVisibility(View.VISIBLE);
        mAddFriendPresenter = new addFriendPresenterImpl(this);
        mKeyword.setOnEditorActionListener(mOnEditorActionListener);


        addClassListAdapter = new addClassListAdapter(this);
        addClassListAdapter.setOnItemClickListener(new addClassListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                showItemDialog(position);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(addClassListAdapter);

    }

    private void showItemDialog(final int position) {
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(this);
        normalDialog.setTitle("添加班级");
        normalDialog.setMessage("你确定要加入"+searchGroup.get(position).getGroupName()+"吗？");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAddFriendPresenter.addClass(position);
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        // 显示
        normalDialog.show();

    }


    @OnClick({R.id.search_friend,R.id.back})
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.search_friend:
                searchFriend();
                break;
            case R.id.back :
                finish();
                break;
        }
    }

    private void searchFriend() {
        String keyWord = mKeyword.getText().toString().trim();
        mAddFriendPresenter.searchfriend(keyWord);

    }
    private TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                searchFriend();
            }
            return true;
        }
    };

    @Override
    public void updateSearchList() {
        searchGroup = mAddFriendPresenter.getSearchGroup();
        addClassListAdapter.setSearchGroup(searchGroup);
        addClassListAdapter.notifyDataSetChanged();
    }

    @Override
    public void addClassSucess() {
        Toast.makeText(this, getString(R.string.add_class_sucess), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addClassFailed() {
        Toast.makeText(this, getString(R.string.add_class_failed), Toast.LENGTH_SHORT).show();
    }
}
