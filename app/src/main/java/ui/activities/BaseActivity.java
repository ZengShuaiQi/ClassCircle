package ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;

import butterknife.ButterKnife;

/**
 * Created by 我是小丑逼 on 2016/12/28.
 */

public abstract class BaseActivity extends AppCompatActivity{
    private ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayouResID());
        ButterKnife.bind(this);
        init();
    }

    protected void init() {

    }
    public void goTo(Class activity){
        Intent intent = new Intent(this,activity);
        startActivity(intent);
        finish();
    }
    public void goTo(Class activity,boolean flags){
        Intent intent = new Intent(this,activity);
        startActivity(intent);
        if(flags) {
            finish();
        }
    }

    public void showProgressDialog(String name){
        if(mProgressDialog == null){
            mProgressDialog = new ProgressDialog(this);
        }
        mProgressDialog.setMessage(name);
        mProgressDialog.show();
    }
    public void hideProgressDialog(){
        mProgressDialog.hide();
    }
    public void hideKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }



    protected abstract int getLayouResID();
}
