package ui.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myqq.MainActivity;
import com.example.myqq.R;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import utils.threadUtils;

/**
 * Created by 我是小丑逼 on 2016/12/28.
 */
public class LoginActivity extends BaseActivity {
    @BindView(R.id.user_name)
    EditText mUserName;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.login)
    Button mLogin;
    @BindView(R.id.new_user)
    TextView mNewUser;

    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 0;
    @Override
    protected int getLayouResID() {
        return R.layout.activity_login;
    }

    @Override
    protected void init() {
        super.init();
        mPassword.setOnEditorActionListener(myOnEditorActionListener);
    }

    private TextView.OnEditorActionListener myOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                login();
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.login, R.id.new_user})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                if(hasWriteExternalStoragePermission()){
                    login();
                }else{
                    applyWriteExternalStoragePermission();
                }

                break;
            case R.id.new_user:
                goTo(registerActivity.class);
                break;
        }
    }

    private void applyWriteExternalStoragePermission() {
        String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(this,permission,REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);

    }

    private boolean hasWriteExternalStoragePermission() {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void login() {
        showProgressDialog(getString(R.string.logining));
        hideKeyboard();
        String userName = mUserName.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        EMClient.getInstance().login(userName,password,new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.d("main", "登录聊天服务器成功！");
                threadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        goTo(MainActivity.class);
                        hideProgressDialog();
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Log.d("main", "登录聊天服务器失败！");
                threadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressDialog();
                    }
                });
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    login();
                }else{
                    Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
                }

        }
    }
}
