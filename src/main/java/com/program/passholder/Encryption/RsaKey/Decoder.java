package com.program.passholder.Encryption.RsaKey;

import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@Component
public class Decoder {

    public String decrypt(String passwordEncrypted, String privateKeyBase64) {
        try{
            //Dekodowanie prywatnego klucza z Base64
            byte[] privBytes = Base64.getDecoder().decode(privateKeyBase64);    //Zamiana tekstu na bajty DER
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privBytes);   //Zamiana na format PKCD#8 DER
            KeyFactory kf = KeyFactory.getInstance("RSA");  //Pobierz fabrykę kluczy dla RSA
            PrivateKey privateKey = kf.generatePrivate(keySpec);    //Utwórz objekt PrivateKey dla JDK

            //Konfiguracja RSA, Dopasowany padding
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            //Dekodowanie
            byte[] encryptedBytes = Base64.getDecoder().decode(passwordEncrypted);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
