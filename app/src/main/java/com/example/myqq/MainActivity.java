package com.example.myqq;




import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import factory.fragmentFactory;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;

import butterknife.BindView;
import ui.activities.BaseActivity;

public class MainActivity extends BaseActivity {


    @BindView(R.id.bottomBar)
    BottomBar bottomBar;

    private FragmentManager mFragmentManager;

    @Override
    protected int getLayouResID() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        mFragmentManager = getSupportFragmentManager();
        initBadge();
        bottomBar.setOnTabSelectListener(onTabSelectListener);


    }

    private void initBadge() {
        BottomBarTab barTab = bottomBar.getTabWithId(R.id.tab_conversation);
//        barTab.setBadgeCount(5);
    }
    private OnTabSelectListener onTabSelectListener = new OnTabSelectListener() {
        @Override
        public void onTabSelected(@IdRes int tabId) {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,fragmentFactory.getInstance().getFragment(tabId));
            fragmentTransaction.commit();
        }
    };


}
