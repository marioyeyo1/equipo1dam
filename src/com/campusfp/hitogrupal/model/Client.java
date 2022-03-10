package com.campusfp.hitogrupal.model;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.security.PublicKey;

public class Client {
    private int id;
    private Socket socket;
    private Cifrado cif;
    private FileManager fm;

    public Client(int id, Socket socket, PublicKey pkey) {
        this.id = id;
        this.socket = socket;
        cif = new Cifrado(pkey);
        this.fm = new FileManager("./enviar");
    }

    public int sendMsg(String msg) {
        byte[] msgBytes;
        try {
            DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());

            msgBytes = cif.cifrarTxt(msg);
            outStream.writeUTF("notification");
            outStream.writeInt(msgBytes.length);
            outStream.write(msgBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    public int sendFile(String fileName) {
        try {
            File file = fm.getFileByName(fileName);
            FileSender fs = new FileSender(socket, file, cif);
            DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());

            outStream.writeUTF("file");
            fs.start();
            fs.join();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    public void sendCloseWarning() {
        try {
            DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());

            outStream.writeUTF("exit");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
