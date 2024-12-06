package cn.net.yto.logrocket.utils;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.util.Collections;

/**
 * *****************************************
 *
 * @Author : Davis
 * @Email : 1192171830@qq.com
 * @Create Time : 2024/12/5 17:05
 * @Description :
 * *****************************************
 */
public class NetworkUtils {

    public static String getWiFiIPAddress(Context context) {
        WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiMgr != null && wifiMgr.getConnectionInfo() != null) {
            int ipAddress = wifiMgr.getConnectionInfo().getIpAddress();
            return Formatter.formatIpAddress(ipAddress);
        }
        return null;
    }

    public static String getMobileIPAddress() {
        try {
            String ipAddress = "";
            for (java.net.NetworkInterface intf : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                for (InetAddress addr : Collections.list(intf.getInetAddresses())) {
                    if (!addr.isLoopbackAddress() && !(addr instanceof Inet6Address)) {
                        ipAddress = addr.getHostAddress();
                    }
                }
            }
            return ipAddress;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static int getRandomAvailablePort() {
        ServerSocket serverSocket = null; // 0 表示让系统自动分配一个空闲端口
        try {
            serverSocket = new ServerSocket(0);
            int port = serverSocket.getLocalPort();
            serverSocket.close();
            return port;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 9527;
    }


}
