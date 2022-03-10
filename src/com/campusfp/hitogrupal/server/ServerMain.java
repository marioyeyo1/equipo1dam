package com.campusfp.hitogrupal.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import com.campusfp.hitogrupal.model.Client;
import com.campusfp.hitogrupal.model.FileManager;
import com.campusfp.hitogrupal.utils.Colors;
import com.campusfp.hitogrupal.utils.Menu;
import com.campusfp.hitogrupal.utils.Colors.EColors;

public class ServerMain {
    private static List<Client> clients;
    private static Scanner sc;

    public static void main(String[] args) {
        FileManager fm = new FileManager("./enviar");
        Menu menu = new Menu();
        clients = new ArrayList<Client>();
        sc = new Scanner(System.in);

        ServerRoutine sr = new ServerRoutine(clients);
        sr.start();
        
        boolean activo = true;
        while (activo) {
            Menu.clearTerminal();
            menu.printMenu("main");
            System.out.println("Introduce una opción: ");
            String input = sc.nextLine();
            int opcion = 0;
            try {
                opcion = Integer.parseInt(input);
                if (opcion < 0 || opcion > 3)
                    throw new Exception();
            } catch (Exception e) {
                Colors.printlnString(EColors.RED, "Opción inválida");
                Menu.confirmMsg("", sc);
                continue;
            }
            switch (opcion) {
                case 0:
                    System.out.println("Cerrando clientes...");
                    closeConnections();
                    System.out.println("Cerrando receptor de clientes...");
                    sr.setActivo(false);
                    System.out.println("Cerrando servidor...");
                    System.exit(1);
                    break;
                case 1:
                    Colors.printlnString(EColors.BLUE, "Enviando notificación horaria.");
                    String msg = (new Date()).toString();
                    notifyClients(msg);
                    Menu.confirmMsg("Notificación enviada con éxito.", sc);
                    break;
                case 2:
                    System.out.println("Introduce el texto de la notificación: ");
                    String msg2 = sc.nextLine();
                    notifyClients(msg2);
                    Menu.confirmMsg("Notificación enviada con éxito.", sc);
                    break;
                case 3:
                    List<String> fileNames = fm.getFileNames();
                    menu.updateFileList(fileNames);
                    while (true) {
                        Menu.clearTerminal();
                        menu.printMenu("files");
                        System.out.println("Introduce una opción: ");
                        String input2 = sc.nextLine();
                        int opcion2 = 0;
                        try {
                            opcion2 = Integer.parseInt(input2);
                            if (opcion2 < 0 || opcion2 > fileNames.size())
                                throw new Exception();
                        } catch (Exception e) {
                            Colors.printlnString(EColors.RED, "Opción inválida");
                            continue;
                        }
                        if (opcion2 != 0) {
                            sendFileToClients(fileNames.get(opcion2 - 1));
                            System.out.println("Archivo enviado con éxito.");
                        }
                        break;
                    }
                    Menu.confirmMsg("", sc);
                    break;
            }
        }
    }

    public static void notifyClients(String msg) {
        for (Client client : clients) {
           client.sendMsg(msg); 
        }
    }

    public static void sendFileToClients(String fileName) {
        for (Client client : clients) {
            client.sendFile(fileName);
        }
    }

    public static void closeConnections() {
        for (Client client : clients) {
            client.sendCloseWarning();
        }
    }
}