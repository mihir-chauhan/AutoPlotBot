package com.example.odometryapp_v10.RobotSimulation.Structure;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;

public class AutoClient {

    public static final String COMMAND_GET_FILE_NAMES = "COMMAND_GET_FILE_NAMES";

    private DatagramSocket socket;

    private String address;
    private int port;

    public AutoClient(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public void connectToServer() {
        try {
            DatagramSocket sock = new DatagramSocket();
            sock.connect(new InetSocketAddress(address, port));
            this.socket = sock;
        } catch (IOException ex) {
            System.out.println("IOException: " + ex.getMessage());
        }
    }

    public void disconnectFromServer() {
        socket.disconnect();
        socket.close();
    }

    public String requestFileNames() {
        String data = "none";
        try {
            DatagramPacket request = new DatagramPacket(COMMAND_GET_FILE_NAMES.getBytes(), COMMAND_GET_FILE_NAMES.length());
            this.socket.send(request);

            byte[] buffer = new byte[10000];
            DatagramPacket response = new DatagramPacket(buffer, buffer.length);
            socket.receive(response);
            return new String(buffer, 0, response.getLength());
        } catch (SocketTimeoutException ex) {
            System.out.println("SocketTimeoutException: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("IOException: " + ex.getMessage());
        }
        return data;
//        } catch (InterruptedException ex) {
//            System.out.println("InterruptedException: " + ex.getMessage());
//        }
    }

    public void sendData(String data) {
        try {
            DatagramPacket request = new DatagramPacket(data.getBytes(), data.length());
            this.socket.send(request);
        } catch (SocketTimeoutException ex) {
            System.out.println("SocketTimeoutException: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("IOException: " + ex.getMessage());
        }
//        } catch (InterruptedException ex) {
//            System.out.println("InterruptedException: " + ex.getMessage());
//        }
    }

    public String requestFile(String filename) {
        String data = "none";
        try {
            DatagramPacket request = new DatagramPacket(filename.getBytes(), filename.length());
            this.socket.send(request);

            byte[] buffer = new byte[10000];
            DatagramPacket response = new DatagramPacket(buffer, buffer.length);
            socket.receive(response);
            return new String(buffer, 0, response.getLength());
        } catch (SocketTimeoutException ex) {
            System.out.println("SocketTimeoutException: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("IOException: " + ex.getMessage());
        }
        return data;
//        } catch (InterruptedException ex) {
//            System.out.println("InterruptedException: " + ex.getMessage());
//        }
    }


}
