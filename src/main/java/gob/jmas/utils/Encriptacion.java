package gob.jmas.utils;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Encriptacion {
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    @Value("${crypto.key}")
    private String keyString;

    @Value("${crypto.iv}")
    private String ivString;

    public String encriptar(String input) throws Exception {
        Key key = new SecretKeySpec(keyString.getBytes(), "AES");
        AlgorithmParameterSpec iv = new IvParameterSpec(ivString.getBytes());
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] encrypted = cipher.doFinal(input.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public String desencriptar(String input) throws Exception {
        Key key = new SecretKeySpec(keyString.getBytes(), "AES");
        AlgorithmParameterSpec iv = new IvParameterSpec(ivString.getBytes());
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(input));
        return new String(decrypted);
    }
}