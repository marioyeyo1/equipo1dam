package com.campusfp.hitogrupal.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

import com.campusfp.hitogrupal.model.Cifrado;
import com.campusfp.hitogrupal.model.FileReceiver;
import com.campusfp.hitogrupal.utils.Colors;
import com.campusfp.hitogrupal.utils.Colors.EColors;

public class ClientMain {
    public static void main(String[] args) {
        Socket socket;
		Scanner sc = new Scanner(System.in);
		DataInputStream inStream;
		DataOutputStream outStream;
        Cifrado cif;
		String ip;
		int port;
        String clave;

		Colors.printlnString(EColors.PURPLE, "Bienvenido al servicio de notificaciones seguro de CampusFP");
		Colors.printlnString(EColors.YELLOW, "Introduce la ip del servidor (default 127.0.0.1):");
		ip = sc.nextLine();
		if (ip.equals(""))
			ip = "127.0.0.1";
			while (true) {
			Colors.printlnString(EColors.YELLOW, "Introduce el puerto (default 1710):");
			String portInput = sc.nextLine();
			try {
				if (portInput.equals(""))
					port = 1710;
				else
					port = Integer.parseInt(portInput);
				if (port < 1024 || port > 65535) {
					throw new Exception();
				}
			} catch (Exception e) {
				Colors.printlnString(EColors.RED, "El puerto introducido es inválido. Rango válido [1024 - 65535]");
				continue;
			}
			break;
		}
        System.out.println("Introduce una clave de cifrado: ");
        clave = sc.nextLine();

        System.out.print("CLIENTE [ ");
        Colors.printString(EColors.GREEN, "CONECTADO");
        System.out.println(" ]");

		try {
			socket = new Socket(ip, port);

            inStream = new DataInputStream(socket.getInputStream());
			outStream = new DataOutputStream(socket.getOutputStream());

            cif = new Cifrado(clave);
            byte[] pbk = cif.getPbk().getEncoded();
            outStream.write(pbk);

            boolean activo = true;
			while (activo) {
                String category = inStream.readUTF();

                switch (category) {
                    case "exit":
                        System.out.println("Cerrando cliente por petición del servidor...");
                        activo = false;
                        break;

                    case "notification":
                        int bufferSize = inStream.readInt();
                        byte[] bytesRecibidos = new byte[bufferSize];

                        inStream.read(bytesRecibidos, 0, bufferSize);

                        String msg = cif.descifrarTxt(bytesRecibidos);
                        System.out.print("[ ");
                        Colors.printString(EColors.RED, "SERVER");
                        System.out.println(" ] " + msg);
                        break;

                    case "file":
                        Colors.printlnString(EColors.YELLOW, "Recibiendo archivo del servidor...");
                        FileReceiver fr = new FileReceiver(socket, "./recibir", cif);
                        fr.start();
                        fr.join();
                        Colors.printlnString(EColors.GREEN, "Archivo recibido.");
                        break;
                
                    default:
                        Colors.printlnString(EColors.RED, "Opción inválida recibida: \"" + category + "\"");
                        break;
                }
            }
		} catch (Exception e) {
			e.printStackTrace();
		} 

		sc.close();
	}
}
