package cn.net.yto.logrocket.provider;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.net.yto.logcat.meta.MetaParseUtil;
import cn.net.yto.logrocket.LogRocket;
import cn.net.yto.logrocket.lifecycle.ActivityLifecycleCallbackImpl;

/**
 * *****************************************
 *
 * @Author : Davis
 * @Email : 1192171830@qq.com
 * @Create Time : 2024/12/5 20:04
 * @Description :
 * *****************************************
 */
public class LogRocketProvider extends ContentProviderImpl {

    private int mCount = 0;

    @Override
    public void doWork(@NonNull Context context) {
        registerLifecycle(context);
    }

    private void registerLifecycle(Context context) {
        if (context instanceof Application) {
            ((Application) context).registerActivityLifecycleCallbacks(new ActivityLifecycleCallbackImpl() {
                @Override
                public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                    super.onActivityCreated(activity, savedInstanceState);
                    if (mCount <= 0) {
                        boolean uniquePort = MetaParseUtil.parseMeta(context);
                        LogRocket.getInstance()
                                .uniquePort(uniquePort)
                                .startUploadLog()
                                .start();
                    }
                    mCount++;
                }

                @Override
                public void onActivityDestroyed(@NonNull Activity activity) {
                    super.onActivityDestroyed(activity);
                    mCount--;
                    if (mCount <= 0) {
                        Log.e("ccer","===========stop====================>");
                        LogRocket.getInstance().stop();
                    }
                }

                @Override
                public void onActivityStopped(@NonNull Activity activity) {
                    super.onActivityStopped(activity);
                }
            });
        }
    }


}
