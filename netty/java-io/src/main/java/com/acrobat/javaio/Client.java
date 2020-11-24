package com.acrobat.javaio;

import java.io.*;
import java.net.Socket;

/**
 * @author xutao
 * @date 2020-11-24 10:47
 */
public class Client {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(SocketConfig.host, SocketConfig.port);
        if (socket.isConnected()) System.out.println("connected to server!");

        // 读取并发送控制台输入，并发送给服务器
        new SysScanner(socket.getOutputStream()).start();

        // 读取服务器返回
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String response;
        while ((response = in.readLine()) != null) {
            System.out.println(response);

            if (SocketConfig.DISCONNECT.equals(response)) {
                break;
            }
        }

        // 客户端主动关闭连接
        if (socket.isConnected()) socket.close();
    }

    static class SysScanner extends Thread {

        private OutputStream out;

        public SysScanner(OutputStream out) {
            this.out = out;
        }

        public void run() {
            PrintWriter writer = new PrintWriter(out, true);

            BufferedReader sysInput = new BufferedReader(new InputStreamReader(System.in));
            String request;
            while (true) {
                try {
                    request = sysInput.readLine();
                    if ("done".equals(request)
                            || request == null) {
                        break;
                    }

                    writer.println(request);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
