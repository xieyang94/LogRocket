package cn.net.yto.logrocket;

import android.content.Context;
import android.text.TextUtils;

import org.java_websocket.WebSocket;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.net.yto.logrocket.callback.UploadLogCallback;
import cn.net.yto.logrocket.impl.WebSocketServerImpl;
import cn.net.yto.logrocket.utils.LogCatUtil;
import cn.net.yto.logrocket.utils.NetworkUtils;

/**
 * *****************************************
 *
 * @Author : Davis
 * @Email : 1192171830@qq.com
 * @Create Time : 2024/12/5 15:15
 * @Description :
 * *****************************************
 */
public class LogRocket {

    private WebSocketServerImpl mServer;
    private final ExecutorService executorService;
    private static int PORT = 9527;
    private boolean hasStart = false;
    private boolean uniquePort = false;

    //单例
    private static class SingletonHandler {
        private static final LogRocket instance = new LogRocket();
    }

    private LogRocket() {
        executorService = Executors.newCachedThreadPool();
        mServer = new WebSocketServerImpl(new InetSocketAddress(port()));
    }

    public static LogRocket getInstance() {
        return SingletonHandler.instance;
    }

    private int port() {
        if (uniquePort) {
            return uniquePort();
        }
        return randomPort();
    }

    private int uniquePort() {
        PORT = 9527;
        return PORT;
    }

    private int randomPort() {
        PORT = NetworkUtils.getRandomAvailablePort();
        return PORT;
    }

    private Collection<WebSocket> connections() {
        return mServer.getConnections();
    }

    public String getWsAddress(Context context) {
        String wiFiIPAddress = NetworkUtils.getWiFiIPAddress(context);
        return "ws://" + wiFiIPAddress + ":" + PORT;
    }

    private void sendAll(String msg) {
        Collection<WebSocket> connections = connections();
        if (connections.isEmpty()) return;
        for (WebSocket webSocket : connections) {
            send(webSocket, msg);
        }
    }

    private void send(WebSocket webSocket, String msg) {
        try {
            if (webSocket != null && webSocket.isOpen() && !TextUtils.isEmpty(msg) && !webSocket.isClosed() && !webSocket.isClosing()) {
                webSocket.send(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LogRocket uniquePort(boolean uniquePort) {
        this.uniquePort = uniquePort;
        return this;
    }

    public LogRocket start() {
        if (hasStart) return this;
        try {
            stop();
            mServer = new WebSocketServerImpl(new InetSocketAddress(port()));
            mServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        hasStart = true;
        return this;
    }

    public LogRocket stop() {
        try {
            mServer.stop();
            hasStart = false;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public LogRocket startUploadLog() {
        executorService.execute(() -> {
            LogCatUtil.getInstance().uploadLog(new UploadLogCallback() {
                @Override
                public void upload(String log) {
                    sendAll(log);
                }
            });
        });
        return this;
    }
}
