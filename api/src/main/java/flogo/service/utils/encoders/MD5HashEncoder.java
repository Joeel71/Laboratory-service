package flogo.service.utils.encoders;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5HashEncoder implements HashEncoder {

    private final MessageDigest messageDigest;

    public MD5HashEncoder() {
        this.messageDigest = createInstance();
    }

    private static MessageDigest createInstance() {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String encode(String content) {
        byte[] hashBytes = messageDigest.digest(content.getBytes());
        StringBuilder stringBuilder = new StringBuilder();
        for (byte hashByte : hashBytes) {
            String hex = Integer.toHexString(0xff & hashByte);
            if (hex.length() == 1) stringBuilder.append('0');
            stringBuilder.append(hex);
        }
        return stringBuilder.toString();
    }
}
