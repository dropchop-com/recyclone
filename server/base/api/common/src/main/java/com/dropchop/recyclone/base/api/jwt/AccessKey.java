package com.dropchop.recyclone.base.api.jwt;

import com.dropchop.recyclone.base.api.model.utils.Iso8601;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.ZonedDateTime;
import java.util.Base64;

public class AccessKey {
  private static final String SEPARATOR = "::";
  private static final String ALGO = "AES/GCM/NoPadding";
  private static final int GCM_IV_LENGTH = 12;
  private static final byte[] KEY = "your-32-byte-long-secret-key-!!!".getBytes(StandardCharsets.UTF_8);

  public enum Type {
    user_password,
    user_token,
  }

  private final String clientId;
  private final String token;
  private final Type type;
  private final String userName;
  private final String password;

  public AccessKey(String clientId, String token, Type type, String userName, String password) {
    this.clientId = clientId;
    this.token = token;
    this.type = type;
    this.userName = userName;
    this.password = password;
  }

  public static String encrypt(Type type, String clientId, String plainText) {
    String date = Iso8601.DATE_TIME_MS_TZ_FORMATTER.get().format(ZonedDateTime.now());
    String clearText = type.name() + SEPARATOR + clientId + SEPARATOR + date + SEPARATOR + plainText;

    byte[] iv = new byte[GCM_IV_LENGTH];
    new SecureRandom().nextBytes(iv);

    byte[] encrypted;
    try {
      Cipher cipher = Cipher.getInstance(ALGO);
      cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(KEY, "AES"), new GCMParameterSpec(128, iv));
      encrypted = cipher.doFinal(clearText.getBytes(StandardCharsets.UTF_8));
    } catch (Exception e) {
      throw new RuntimeException("Failed to encrypt the access key!", e);
    }

    byte[] result = new byte[iv.length + encrypted.length];
    System.arraycopy(iv, 0, result, 0, iv.length);
    System.arraycopy(encrypted, 0, result, iv.length, encrypted.length);

    return Base64.getEncoder().encodeToString(result);
  }

  public static String encrypt(String clientId, String userName, String password) {
    return password;
  }

  public static String encrypt(String clientId,         String token) {
    return token;
  }

  public static AccessKey decrypt(String cipherText) {
    return new AccessKey(null, null, Type.user_password, null, null);
  }
}
