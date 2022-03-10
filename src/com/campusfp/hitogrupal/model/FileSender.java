package com.campusfp.hitogrupal.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class FileSender extends Thread {
	private final int BUF_SIZE = 16000;
	
	//	Attributes
	private Socket socket;
	private File file;
	private Cifrado cif;
	
	//	Constructors
	public FileSender(Socket socket, File file, Cifrado cif) {
		this.socket = socket;
		this.file = file;
		this.cif = cif;
	}
	
	//	Methods
	@Override
	public void run() {
		try {
			DataInputStream inStream = new DataInputStream(socket.getInputStream());
			DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());
			FileInputStream fis = new FileInputStream(file);
			List<byte[]> encryptedBits = new ArrayList<>();
	
			outStream.writeInt(1);
			int response = inStream.readInt();

			/* if (response == 1) {
				outStream.writeUTF(file.getName());
				byte[] fileBytes = fis.readAllBytes();
				fileBytes = cif.cifrarArchivo(fileBytes);
				if (fileBytes.length > 256) {
					Colors.printlnString(EColors.RED, "Archivo demasiado grande para enviar. Abortando.");
					return;
				}

				outStream.write(fileBytes);
			} */
			
			if (response == 1) {
				outStream.writeUTF(file.getName());
				byte[] fileBytes = fis.readAllBytes();

				int off = 0;
				while (off < fileBytes.length)
				{
					byte[] temp = new byte[245];
					int i = 0;
					for (; i < 245 && (i + off) < fileBytes.length; i++) {
						temp[i] = fileBytes[off + i];
					}
					if (i != 245) {
						byte[] temp2 = temp;
						temp = new byte[i];
						for (int j = 0; j < i; j++) {
							temp[j] = temp2[j];
						}
					}
					temp = cif.cifrarArchivo(temp);
					encryptedBits.add(temp);
					off += i;
				}

				for (byte[] bit : encryptedBits) {
					System.out.println("Sending: " + bit.length);
					outStream.writeInt(1);
					outStream.write(bit);
				}
				Thread.sleep(100);
				outStream.writeInt(0);

				/* int offset = 0;
				boolean finished = false;
				while (!finished) {
					byte[] sendBytes = new byte[BUF_SIZE];
					int readBytes;
					for (readBytes = 0; readBytes < BUF_SIZE && (readBytes + offset) < fileBytes.length; readBytes++) {
						sendBytes[readBytes] = fileBytes[readBytes + offset];
					}
					offset += readBytes;
					
					if (readBytes < BUF_SIZE) {
						byte[] tempBytes = sendBytes;
						sendBytes = new byte[readBytes];
						for (int i = 0; i < readBytes; i++) {
							sendBytes[i] = tempBytes[i];
						}
						finished = true;
					}

					outStream.writeInt(1);
					outStream.write(sendBytes);
				}
				Thread.sleep(100);
				outStream.writeInt(0); */
			}
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	//	Getters & Setters
	
	
}
