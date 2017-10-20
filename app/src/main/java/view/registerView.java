package view;

/**
 * Created by 我是小丑逼 on 2016/12/29.
 */

public interface registerView {
    void onRegisterSuccess();

    void onRegisterFailed();

    void onUserNameError();

    void onConfirmPasswordError();

    void onPasswordError();

    void onStartRegister();

    void onUserNameExist();
}
