package com.dream.logrocket;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private NestedScrollView scrollView;
    private TextView mTvWsAddress;
    private TextView tv_log;
    private Button mBtnConnect;
    private Button mBtnDisconnect;
    private Button mBtnTestStart;
    private Button mBtnTestStop;

    private ExecutorService executorService;
    private final AtomicBoolean interrupt = new AtomicBoolean(false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();

        String wsAddress = getWsAddress(this);
        if (wsAddress != null) {
            mTvWsAddress.setText(wsAddress);
        }
    }

    private void initView() {
        scrollView = (NestedScrollView) findViewById(R.id.scrollView);
        mTvWsAddress = (TextView) findViewById(R.id.tv_ws_address);
        tv_log = (TextView) findViewById(R.id.tv_log);
        mBtnConnect = (Button) findViewById(R.id.btn_connect);
        mBtnDisconnect = (Button) findViewById(R.id.btn_disconnect);
        mBtnTestStart = (Button) findViewById(R.id.btn_test_start);
        mBtnTestStop = (Button) findViewById(R.id.btn_test_stop);

        executorService = Executors.newCachedThreadPool();

        executorService.execute(() -> {
            CopyOnWriteArrayList<String> contentList = new CopyOnWriteArrayList<>();
            uploadLog(new Callback() {
                @Override
                public void upload(String log) {
                    if (contentList.size() > 30) {
                        contentList.remove(0);
                    }
                    contentList.add(log);
                    String contentStr = "";
                    for (String content : contentList) {
                        contentStr += content + "\n";
                    }
                    String finalContentStr = contentStr;
                    runOnUiThread(() -> {
                        tv_log.setText(finalContentStr);
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    });
                }
            });
        });

    }

    public String getWsAddress(Context context) {
        String result = null; // 默认值，如果反射调用失败则返回此值
        try {
            // 获取MetaUtil类的Class对象
            Class<?> metaUtilClass = Class.forName("cn.net.yto.logrocket.refect.ReflectLogRocket");
            // 获取getPort方法的Method对象
            Method getPortMethod = metaUtilClass.getMethod("wdAddress", Context.class);
            // 调用getPort方法
            // 注意：如果getPort是非公开方法，可能需要设置accessible为true
            getPortMethod.setAccessible(true);
            // 创建MetaUtil类的实例
            Object metaUtilInstance = metaUtilClass.newInstance();
            // 调用invoke方法执行getPort(context)
            result = (String) getPortMethod.invoke(metaUtilInstance, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private void copyText(TextView view) {
        // 创建一个剪贴板管理器
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建一个剪贴数据集
        ClipData clip = ClipData.newPlainText("label", view.getText());
        // 将剪贴数据集放到剪贴板
        clipboard.setPrimaryClip(clip);
        // 显示一个短暂的提示，表明复制成功
        Toast.makeText(getApplicationContext(), "已复制到剪贴板", Toast.LENGTH_SHORT).show();
    }

    private void initListener() {
        mTvWsAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyText(mTvWsAddress);
            }
        });
        mBtnTestStart.setOnClickListener(v -> {
            testLog();
        });
        mBtnTestStop.setOnClickListener(v -> {
            interrupt.set(false);
        });
    }

    private void testLog() {
        interrupt.set(true);
        executorService.execute(() -> {
            try {
                while (interrupt.get()) {
                    try {
                        Thread.sleep(1000);
                        Log.e("ccer", currentTime() + "----请注意，这里是测试日志，产生频率为1秒钟一条");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void uploadLog(Callback callback) {
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

    interface Callback {
        void upload(String log);
    }

    private String currentTime() {
        try {
            // 创建一个Date对象，它将包含当前日期和时间
            Date now = new Date();
            // 创建一个SimpleDateFormat对象，用于指定输出格式
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 使用format方法将Date对象格式化为字符串
            String currentDateTime = dateFormat.format(now);
            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis() + "";
    }

}