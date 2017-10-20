package presenter.impl;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import presenter.registerPresenter;
import utils.StringUtils;
import utils.threadUtils;
import view.registerView;

/**
 * Created by 我是小丑逼 on 2016/12/29.
 */

public class registerPresenterImpl implements registerPresenter {

    private registerView mRegisterView;

    public registerPresenterImpl(registerView mRegisterView){
        this.mRegisterView = mRegisterView;
    }


    @Override
    public void register(String username, String password, String confirmPassword) {
        if(StringUtils.isValidUserName(username)){
            if(StringUtils.isValidPassword(password)){

                if(password.equals(confirmPassword)){
                    mRegisterView.onStartRegister();

                    registerBmob(username,password);
                }else{
                    mRegisterView.onConfirmPasswordError();
                }
            }else{
                mRegisterView.onPasswordError();
            }
        }else{
            mRegisterView.onUserNameError();
        }

    }

    private void registerBmob(final String username, final String password) {
        BmobUser bmobUser = new BmobUser();
        bmobUser.setUsername(username);
        bmobUser.setPassword(password);
        bmobUser.signUp(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                if (e == null) {
                    registerEaseMob(username,password);

                } else {
                    if(e.getErrorCode()==202){
                        mRegisterView.onUserNameExist();
                    }else {
                        mRegisterView.onRegisterFailed();
                    }
                }
            }

            private void registerEaseMob(final String username,final String password) {
               /* new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EMClient.getInstance().createAccount(username, password);
                            mRegisterView.onRegisterSuccess();
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();*/
                threadUtils.runOnBackgroudThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EMClient.getInstance().createAccount(username, password);
                            threadUtils.runOnMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    mRegisterView.onRegisterSuccess();
                                }
                            });
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            threadUtils.runOnMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    mRegisterView.onRegisterFailed();
                                }
                            });
                        }
                    }
                });
            }
        });
    }
}
