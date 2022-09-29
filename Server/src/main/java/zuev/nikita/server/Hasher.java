package zuev.nikita.server;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class that hashes a string. Result is a HEX-string
 *
 */

public class Hasher {
    public static String getHashSHA384(String text){

        try{
            final MessageDigest digest = MessageDigest.getInstance("SHA-384");
            final byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
            final StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                final String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
}
