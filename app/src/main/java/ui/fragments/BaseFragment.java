package ui.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by 我是小丑逼 on 2016/12/28.
 */

public abstract class BaseFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(getLayoutResID(),null);
        ButterKnife.bind(this,root);
        init();
        return root;
    }

    public  void init() {

    }
    public void goTo(Class activity){
        Intent intent = new Intent(getContext(),activity);
        startActivity(intent);
        getActivity().finish();
    }
    public void goTo(Class activity,boolean flags){
        Intent intent = new Intent(getContext(),activity);
        startActivity(intent);
        if(flags)
        getActivity().finish();
    }

    protected abstract int getLayoutResID();
}
