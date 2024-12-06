package cn.net.yto.logrocket.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import cn.net.yto.logrocket.callback.UploadLogCallback;

/**
 * *****************************************
 *
 * @Author : Davis
 * @Email : 1192171830@qq.com
 * @Create Time : 2024/12/5 15:17
 * @Description :
 * *****************************************
 */
public class LogCatUtil {

    //单例
    private static class SingletonHandler {
        private static LogCatUtil instance = new LogCatUtil();
    }

    private LogCatUtil() {
    }

    public static LogCatUtil getInstance() {
        return SingletonHandler.instance;
    }

    public void uploadLog(UploadLogCallback callback) {
        try {
            // 执行logcat命令
            Process process = Runtime.getRuntime().exec("logcat");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                if (callback != null) {
                    callback.upload(line);
                }
            }
            // 关闭流
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
