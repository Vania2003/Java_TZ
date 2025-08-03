package com.archiver.library;

import java.io.*;
import java.nio.file.*;
import java.security.*;
import java.util.zip.*;

public class Archiver {
    public static void zipDirectory(Path sourceDir, Path zipFilePath) throws IOException {
        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipFilePath))) {
            Files.walk(sourceDir)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(sourceDir.relativize(path).toString());
                        try {
                            zos.putNextEntry(zipEntry);
                            Files.copy(path, zos);
                            zos.closeEntry();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }
    }

    public static String generateMD5(Path filePath) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        try (InputStream is = Files.newInputStream(filePath);
             DigestInputStream dis = new DigestInputStream(is, md)) {
            while (dis.read() != -1);
        }
        byte[] digest = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static void saveMD5(Path filePath, String hash) throws IOException {
        Path md5File = filePath.resolveSibling(filePath.getFileName() + ".md5");
        Files.writeString(md5File, hash);
    }

    public static boolean verifyMD5(Path filePath) throws IOException, NoSuchAlgorithmException {
        Path md5File = filePath.resolveSibling(filePath.getFileName() + ".md5");
        if (!Files.exists(md5File)) return false;
        String storedMD5 = Files.readString(md5File).trim();
        return storedMD5.equals(generateMD5(filePath));
    }
}
