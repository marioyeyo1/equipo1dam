package com.campusfp.hitogrupal.model;

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
            e.printStackTrace();
        }

    }

    public Cifrado(PublicKey pbk) {
        try {
            c = Cipher.getInstance("RSA");
            this.pbk = pbk;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }

    }

    public byte[] cifrarTxt(String s) {
        try {
            c.init(Cipher.ENCRYPT_MODE, pbk);
            return c.doFinal(s.getBytes());
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
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
            e.printStackTrace();
            return null;
        }
    }

    public byte[] cifrarArchivo(byte[] s) {
        try {
            c.init(Cipher.ENCRYPT_MODE, pbk);
            return c.doFinal(s);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
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
            e.printStackTrace();
            return null;
        }
    }

    public PublicKey getPbk() {
        return pbk;
    }
}
