package presenter.impl;

import com.hyphenate.chat.EMClient;

import presenter.splashPresenter;
import view.splashView;

/**
 * Created by 我是小丑逼 on 2016/12/28.
 */

public class splashPresenterImpl implements splashPresenter {
    private splashView mSplashView;

    public splashPresenterImpl(splashView view){
        mSplashView = view;
    }

    @Override
    public void checkLoginStatus() {
        if (isLoggedIn()){
            mSplashView.onLoggedIn();
        }else{
            mSplashView.onNotLogin();
        }

    }
    public boolean isLoggedIn(){

        return EMClient.getInstance().isLoggedInBefore() && EMClient.getInstance().isConnected();
    }
}
