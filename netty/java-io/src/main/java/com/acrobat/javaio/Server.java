package com.acrobat.javaio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 最简单的java socket阻塞式服务器，只能处理一个客户端连接
 *
 * @author xutao
 * @date 2020-11-24 10:40
 */
public class Server {

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(SocketConfig.port);

        // 阻塞等待直到收到客户端连接
        Socket clientSocket = serverSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        // 以下代码只能处理第一个连接上的客户端
        String request;
        while ((request = in.readLine()) != null) {
            if ("done".equals(request)) {
                // 关闭与客户端的连接
                out.println(SocketConfig.DISCONNECT);
                clientSocket.close();

                break;
            }

            System.out.println("message from client: " + request);
            out.println("message received: " + request);
        }
    }

}
