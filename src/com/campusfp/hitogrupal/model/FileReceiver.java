package com.campusfp.hitogrupal.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.campusfp.hitogrupal.utils.Colors;
import com.campusfp.hitogrupal.utils.Colors.EColors;


public class FileReceiver extends Thread {
	//	Attributes
	private Socket socket;
	private FileManager fileManager;
	private List<byte[]> fileContent;
	private Cifrado cif;
	
	//	Constructors
	public FileReceiver(Socket socket, String destinationFolder, Cifrado cif) {
		this.socket = socket;
		this.fileManager = new FileManager(destinationFolder);
		this.fileContent = new ArrayList<>();
		this.cif = cif;
	}
	
	//	Methods
	@Override
	public void run() {
		try {
			DataInputStream inStream = new DataInputStream(socket.getInputStream());
			DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());
	
			int serverMsg = inStream.readInt();
			if (serverMsg == 1) {
				outStream.writeInt(1);
				String fileName = inStream.readUTF();

				/* byte[] receivedBytes = new byte[256];
				inStream.read(receivedBytes, 0, 256); */

				while (inStream.readInt() == 1) {
					byte[] receivedBytes = new byte[256];
					int readBytes = inStream.read(receivedBytes, 0, 256);
					//System.out.println("Receiving: " + readBytes);
					
					if (readBytes < 256) {
						byte[] temp = receivedBytes;
						receivedBytes = new byte[readBytes];
						for (int i = 0; i < readBytes; i++) {
							receivedBytes[i] = temp[i];
						}
					}
					receivedBytes = cif.descifrarArchivo(receivedBytes);
					fileContent.add(receivedBytes);
				}

				byte[] joinedFile = new byte[0];
				for (byte[] received : fileContent) {
					byte[] temp = joinedFile;
					joinedFile = new byte[temp.length + received.length];
					int offset = 0;
					for (; offset < temp.length; offset++) {
						joinedFile[offset] = temp[offset];
					}
					for (int j = 0; j < received.length; j++) {
						joinedFile[j + offset] = received[j];
					}
				}

				if (fileManager.createFile(fileName, joinedFile)) {
					Colors.printlnString(EColors.GREEN, "Fichero recibido con éxito!");
				}
				else {
					Colors.printlnString(EColors.RED, "Ha habido algún error al crear el archivo.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	//	Getters & Setters
	
	
}
