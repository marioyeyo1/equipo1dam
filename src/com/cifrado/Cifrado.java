package com.cifrado;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Cifrado {
    private Cipher c;
    private PrivateKey prk;
    private PublicKey pbk;

    public Cifrado(String clave) {
        try {
            c = Cipher.getInstance("RSA");
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            KeyPair kp = kpg.generateKeyPair();
            prk = kp.getPrivate();
            pbk = kp.getPublic();
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public byte[] cifrarTxt(String s) {
        try {
            c.init(Cipher.ENCRYPT_MODE, pbk);
            return c.doFinal(s.getBytes());
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public String descifrarTxt(byte[] s) {
        try {
            c.init(Cipher.DECRYPT_MODE, prk);
            byte[] descifrado = c.doFinal(s);
            return new String(descifrado);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public byte[] cifrarArchivo(byte[] s) {
        try {
            c.init(Cipher.ENCRYPT_MODE, pbk);
            return c.doFinal(s);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public byte[] descifrarArchivo(byte[] s) {
        try {
            c.init(Cipher.DECRYPT_MODE, prk);
            byte[] descifrado = c.doFinal(s);
            return descifrado;
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        // instanciar sistema de cifrado
        Cifrado c = new Cifrado("clave");
        // cifrar texto
        String txt = "hola";
        byte[] txtCifrado = c.cifrarTxt(txt);
        System.out.print("texto cifrado: ");
        System.out.println(new String(txtCifrado));

        // descifrar texto
        String txtDescifrado = new String(c.descifrarTxt(txtCifrado));
        System.out.print("text descifrado: ");
        System.out.println(txtDescifrado);

        // cifrar archivos
        File f = new File("test.txt");
        try {
            FileInputStream fis = new FileInputStream(f);
            byte[] fCifrado = c.cifrarArchivo(fis.readAllBytes());
            System.out.print("archivo de texto cifrado: ");
            System.out.println(new String(fCifrado));

            // descifrar archivos
            byte[] fDescifrado = c.descifrarArchivo(fCifrado);
            System.out.print("archivo de texto descifrado: ");
            System.out.println(new String(fDescifrado));
            FileOutputStream fos = new FileOutputStream(new File("descifrado.txt"));
            fos.write(fDescifrado);
            fos.flush();
            fos.close();
            fis.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
