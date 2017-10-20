package ui.activities;

import android.os.Handler;

import com.example.myqq.MainActivity;
import com.example.myqq.R;

import presenter.impl.splashPresenterImpl;
import presenter.splashPresenter;
import view.splashView;

/**
 * Created by 我是小丑逼 on 2016/12/28.
 */

public class splashActivity extends BaseActivity implements splashView {
    private splashPresenter mSplashPresenter;
    private Handler mHandler = new Handler();
    private static final int DELAY = 1000;

    @Override
    protected int getLayouResID() {
        return R.layout.activity_splash;
    }

    @Override
    protected void init() {
        super.init();
        mSplashPresenter = new splashPresenterImpl(this);
        mSplashPresenter.checkLoginStatus();
    }

    @Override
    public void onLoggedIn() {
        navigateToMain();
    }

    private void navigateToMain() {
      /*  Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);*/
        goTo(MainActivity.class);

    }

    @Override
    public void onNotLogin() {
        navigateToLogin();
    }

    private void navigateToLogin() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
              /*  Intent intent = new Intent(splashActivity.this, LoginActivity.class);
                startActivity(intent);*/
                goTo(LoginActivity.class);

            }
        }, DELAY);
    }
}
