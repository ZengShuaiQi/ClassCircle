package ui.activities;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myqq.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import presenter.impl.registerPresenterImpl;
import view.registerView;

/**
 * Created by 我是小丑逼 on 2016/12/29.
 */

public class registerActivity extends BaseActivity implements registerView {
    @BindView(R.id.user_name)
    EditText mUserName;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.confirm_password)
    EditText mConfirmPassword;
    @BindView(R.id.register)
    Button mRegister;

    private registerPresenterImpl registerPresenter;

    @Override
    protected int getLayouResID() {
        return R.layout.activity_register;
    }

    @Override
    protected void init() {
        super.init();
        registerPresenter = new registerPresenterImpl(this);
        mConfirmPassword.setOnEditorActionListener(myOnEditorActionListener);
    }

    private TextView.OnEditorActionListener myOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                register();
                return true;
            }
            return false;
        }
    };

    private void register() {
        String name = mUserName.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        String confirmPassword = mConfirmPassword.getText().toString().trim();

        hideKeyboard();

        registerPresenter.register(name,password,confirmPassword);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.register)
    public void onClick() {
        register();
    }

    @Override
    public void onRegisterSuccess() {
        Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
        goTo(LoginActivity.class);
    }

    @Override
    public void onRegisterFailed() {
        Toast.makeText(this, "注册失败", Toast.LENGTH_SHORT).show();
        goTo(LoginActivity.class);
    }

    @Override
    public void onUserNameError() {
        mUserName.setError(getString(R.string.user_name_error));
    }

    @Override
    public void onConfirmPasswordError() {
        mConfirmPassword.setError(getString(R.string.confirm_password_error));
    }

    @Override
    public void onPasswordError() {
        mPassword.setError(getString(R.string.password_error));
    }

    @Override
    public void onStartRegister() {
        showProgressDialog(getString(R.string.onRegistering));
    }

    @Override
    public void onUserNameExist() {
        Toast.makeText(this, getString(R.string.username_exist), Toast.LENGTH_SHORT).show();
    }
}
