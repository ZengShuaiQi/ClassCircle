package app;

import android.app.Application;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BuildConfig;

/**
 * Created by 我是小丑逼 on 2016/12/29.
 */

public class ClassCircleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(getApplicationContext(),"df5a02bd4c7875549784ceb033407cc4");

        initEaseMob();
    }

    private void initEaseMob() {
        EMOptions options = new EMOptions();
// 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(true);


//初始化
        EMClient.getInstance().init(getApplicationContext(), options);
//在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        if(BuildConfig.DEBUG){

            EMClient.getInstance().setDebugMode(true);
        }
    }
}
