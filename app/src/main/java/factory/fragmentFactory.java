package factory;

import android.support.v4.app.Fragment;

import ui.fragments.contactFragment;
import ui.fragments.conversationFrament;
import ui.fragments.dynamicFrament;

import com.example.myqq.R;


/**
 * Created by 我是小丑逼 on 2017/1/1.
 */

public class fragmentFactory {
    private static fragmentFactory sFragmentFactory;
    private Fragment mConversationFragment;
    private Fragment contactFragment;
    private Fragment dynamicFragment;
    private fragmentFactory(){}

    public static fragmentFactory getInstance() {
        if (sFragmentFactory == null) {
            synchronized (fragmentFactory.class) {
                if (sFragmentFactory == null) {
                    sFragmentFactory = new fragmentFactory();
                }
            }
        }
        return sFragmentFactory;

    }

    public Fragment getFragment(int tabId) {
        switch (tabId) {
            case R.id.tab_conversation:
                return getConversationFragment();
            case R.id.tab_contacts:
                return getContactFragment();
            case R.id.tab_dynamic:
                return getDynamicFragment();
        }
        return null;
    }

    public Fragment getConversationFragment() {
        if (mConversationFragment == null) {
            mConversationFragment = new conversationFrament();
        }
        return mConversationFragment;
    }

    public Fragment getContactFragment() {
        if (contactFragment == null) {
            contactFragment = new contactFragment();
        }
        return contactFragment;
    }

    public Fragment getDynamicFragment() {
        if(dynamicFragment ==null){
            dynamicFragment = new dynamicFrament();
        }
        return dynamicFragment;
    }
}
