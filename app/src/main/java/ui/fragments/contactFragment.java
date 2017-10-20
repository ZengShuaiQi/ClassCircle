package ui.fragments;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import adapter.contactListAdapter;

import com.example.myqq.R;


import butterknife.BindView;
import butterknife.OnClick;
import presenter.impl.contactPresenterImpl;
import ui.activities.CreateClassActivity;
import ui.activities.addFriendActivity;
import view.contactView;
import presenter.contactPresenter;

/**
 * Created by 我是小丑逼 on 2017/1/1.
 */

public class contactFragment extends BaseFragment implements contactView, View.OnClickListener {
    private contactListAdapter mContactListAdapter;
    private contactPresenter mContactPresenter;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.add)
    ImageView mAdd;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private PopupWindow popupWindow;


    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_contact;
    }

    @Override
    public void init() {
        super.init();
        mTitle.setText(getString(R.string.contacts));
        mAdd.setVisibility(View.VISIBLE);
        mContactPresenter = new contactPresenterImpl(this);
        mContactPresenter.loadContacts();
        initRecyclerView();
        mSwipeRefreshLayout.setColorSchemeResources(R.color.qq_blue, R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);

    }

    private void initRecyclerView() {
        mContactListAdapter = new contactListAdapter(getContext(), mContactPresenter.getDataList());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mContactListAdapter);

    }

    @Override
    public void onLoadContactsSuccess() {
        mContactListAdapter.notifyDataSetChanged();
        Toast.makeText(getContext(), getString(R.string.load_contacts_sucess), Toast.LENGTH_SHORT).show();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @OnClick(R.id.add)
    public void onClick() {
        showPopupWindow();
    }


    /**
     * 创建一个PopupWindow
     */
    private void showPopupWindow() {
        popupWindow = new PopupWindow(getActivity());
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        View contentView = View.inflate(getActivity(), R.layout.add_create, null);
        //给弹窗中的按钮添加点击事件
        contentView.findViewById(R.id.add_class).setOnClickListener(this);
        contentView.findViewById(R.id.create_class).setOnClickListener(this);

        popupWindow.setContentView(contentView);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_contact,null);
        popupWindow.showAtLocation(rootView, Gravity.BOTTOM,0,0);
    }

    @Override
    public void onLoadContactsFailed() {
        Toast.makeText(getContext(), getString(R.string.load_contacts_failed), Toast.LENGTH_SHORT).show();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mContactPresenter.refresh();
        }


    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_class:
                goTo(addFriendActivity.class,false);
                popupWindow.dismiss();
                break;
            case R.id.create_class:
                goTo(CreateClassActivity.class,false);
                popupWindow.dismiss();
                break;
        }
    }
}
