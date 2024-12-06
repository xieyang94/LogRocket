package cn.net.yto.logrocket.refect;

import android.content.Context;

import cn.net.yto.logrocket.LogRocket;
import cn.net.yto.logrocket.utils.NetworkUtils;

/**
 * *****************************************
 *
 * @Author : Davis
 * @Email : 1192171830@qq.com
 * @Create Time : 2024/12/5 20:13
 * @Description :
 * *****************************************
 */
public class ReflectLogRocket {

    /**
     * 反射获取ws地址
     *
     * @param context
     * @return
     */
    public String wdAddress(Context context) {
        return LogRocket.getInstance().getWsAddress(context);
    }


}
