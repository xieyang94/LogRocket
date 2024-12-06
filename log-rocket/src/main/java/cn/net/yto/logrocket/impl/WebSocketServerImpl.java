package cn.net.yto.logrocket.impl;

/**
 * *****************************************
 *
 * @Author : Davis
 * @Email : 1192171830@qq.com
 * @Create Time : 2024/12/5 10:12
 * @Description :
 * *****************************************
 */

import android.util.Log;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class WebSocketServerImpl extends WebSocketServer {


    public WebSocketServerImpl(InetSocketAddress address) {
        super(address);
    }


    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        //log("New connection: " + conn.getRemoteSocketAddress());
        log("Hello , New connector [" + conn.getRemoteSocketAddress().toString().replace("/", "") + "] , Welcome to the Log Rocket , Provides technical services by Davis !");
        //conn.send("Welcome to the Log Rocket , Provides technical services by Davis !");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        //conn.send("Log Rocket closed !");
        log("Connection closed: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        log("Message received: " + message);
        // Echo the message back to the client
        //conn.send("Echo: " + message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        log("Error occurred: " + ex.getMessage());

        ex.printStackTrace();
    }

    @Override
    public void onStart() {
        log("WebSocket Server started successfully!");
    }


    private void log(String message) {
        Log.e("ccer", message);
    }
}
