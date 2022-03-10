package com.campusfp.hitogrupal.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;

import com.campusfp.hitogrupal.model.Client;
import com.campusfp.hitogrupal.utils.Colors;
import com.campusfp.hitogrupal.utils.Colors.EColors;

public class ServerRoutine extends Thread {
    private List<Client> clients;
    private boolean activo;

    public ServerRoutine(List<Client> clients) {
        this.clients = clients;
        activo = true;
    }

    @Override
    public void run() {
        while (activo) {
            try {
                int clientId = 1;
                ServerSocket serverSocket = new ServerSocket(1710);

                while (true) {
                    Socket socket = serverSocket.accept();
                    
                    PublicKey pbk;
                    try {
                        DataInputStream is = new DataInputStream(socket.getInputStream());
                        byte[] keyBytes = new byte[2048];
                        is.read(keyBytes, 0, 2048);
                        //is.close();
                        X509EncodedKeySpec ks = new X509EncodedKeySpec(keyBytes);
                        KeyFactory kf = KeyFactory.getInstance("RSA");
                        pbk = kf.generatePublic(ks);
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }
                    
                    Client c = new Client(clientId, socket, pbk);
                    clients.add(c);
                    Colors.printString(EColors.CYAN, "Client number " + clientId + " connected.");
                    clientId++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

}
