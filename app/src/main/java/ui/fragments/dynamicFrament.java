package ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myqq.R;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ui.activities.LoginActivity;
import utils.threadUtils;

/**
 * Created by 我是小丑逼 on 2017/1/1.
 */

public class dynamicFrament extends BaseFragment {
    @BindView(R.id.logout)
    Button mLogout;
    @BindView(R.id.title)
    TextView mTitle;

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_dynamic;
    }

    @Override
    public void init() {
        super.init();
        mTitle.setText(R.string.dynamic);
        String logout = String .format(getString(R.string.logout), EMClient.getInstance().getCurrentUser());
        mLogout.setText(logout);
    }

    @OnClick(R.id.logout)
    public void onClick() {

        EMClient.getInstance().logout(true, new EMCallBack() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                threadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), getString(R.string.logout_success), Toast.LENGTH_SHORT).show();
                        goTo(LoginActivity.class);
                    }
                });


            }

            @Override
            public void onProgress(int progress, String status) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onError(int code, String message) {
                // TODO Auto-generated method stub
                threadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), getString(R.string.logout_failed), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
