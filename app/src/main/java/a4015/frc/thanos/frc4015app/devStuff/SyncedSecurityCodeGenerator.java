package a4015.frc.thanos.frc4015app.devStuff;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public abstract class SyncedSecurityCodeGenerator {

    private static SecureRandom RANDOM = new SecureRandom();
    private static String ALGO = "AES";
    private static long linkedID = 98237532057230L;

    public static byte[] getKey() {
        RANDOM = new SecureRandom();
        RANDOM.setSeed(linkedID);
        int steps = Calendar.HOUR_OF_DAY;
        byte[] key = new byte[64];
        for (int conv = 0; conv < steps; conv++) {
            RANDOM.nextBytes(key);
        }
        return key;
    }

    public static byte[] encrypt(byte[] data) {
        Key key = new SecretKeySpec(getKey(), ALGO);
        try {
            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(data);
        } catch (Exception e) {
            // TODO tell user encryption protocol has failed
        }
        return null;
    }

    public static byte[] decrypt(byte[] data) {
        Key key = new SecretKeySpec(getKey(), ALGO);
        try {
            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(data);
        } catch (Exception e) {
            // TODO tell user decryption protocol has failed
        }
        return null;
    }

}
