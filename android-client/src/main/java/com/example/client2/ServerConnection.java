package com.example.client2;

import java.io.*;
import java.net.*;
public class ServerConnection {
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Socket socket;

    public boolean connectToServer(String ip, int port) {
        try {
            InetAddress addr = InetAddress.getByName(ip);
            socket = new Socket(addr, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

    public ObjectOutputStream getOutputStream() {
        return out;
    }

    public ObjectInputStream getInputStream() {
        return in;
    }

    public void close() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}