package utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by 我是小丑逼 on 2016/12/30.
 */

public class threadUtils {

    private static Handler mHandler = new Handler(Looper.getMainLooper());
    private static Executor executor = Executors.newSingleThreadExecutor();

    public static void runOnBackgroudThread(Runnable runnable) {
        executor.execute(runnable);
    }

    public static void runOnMainThread(Runnable runnable) {
        mHandler.post(runnable);
    }
}


