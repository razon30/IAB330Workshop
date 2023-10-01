package com.example.fleetmanagement.BLE;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class DataEnCryptDecrypt {

    public static UUID encryptToUUID(String customInfo) {
        // Convert the customInfo to bytes
        byte[] bytes = customInfo.getBytes(StandardCharsets.UTF_8);

        // Wrap the bytes in a ByteBuffer and create a UUID
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        long mostSigBits = byteBuffer.getLong();
        long leastSigBits = byteBuffer.getLong();
        return new UUID(mostSigBits, leastSigBits);
    }

    public static String decryptFromUUID(UUID uuid) {
        // Extract most significant bits and least significant bits from the UUID
        long mostSigBits = uuid.getMostSignificantBits();
        long leastSigBits = uuid.getLeastSignificantBits();

        // Put the bits into a ByteBuffer and get the bytes
        ByteBuffer byteBuffer = ByteBuffer.allocate(16);
        byteBuffer.putLong(mostSigBits);
        byteBuffer.putLong(leastSigBits);

        // Get the bytes as a string
        byte[] bytes = byteBuffer.array();
        return new String(bytes, StandardCharsets.UTF_8);
    }

}
