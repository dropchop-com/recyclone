package com.dropchop.recyclone.base.api.jwt;

import com.dropchop.recyclone.base.api.model.utils.Iso8601;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.ZonedDateTime;
import java.util.Base64;

@SuppressWarnings("unused")
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
  private final ZonedDateTime created;

  public AccessKey(String clientId, ZonedDateTime created, String userName, String password) {
    this.clientId = clientId;
    this.created = created;
    this.token = null;
    this.type = Type.user_password;
    this.userName = userName;
    this.password = password;
  }

  public AccessKey(String clientId, ZonedDateTime created, String token) {
    this.clientId = clientId;
    this.created = created;
    this.token = token;
    this.type = Type.user_token;
    this.userName = null;
    this.password = null;
  }

  public static String encrypt(AccessKey accessKey) {
    String date = Iso8601.DATE_TIME_MS_TZ_FORMATTER.get().format(accessKey.created.toInstant());
    String plainText = accessKey.type.name() + SEPARATOR + date + SEPARATOR + accessKey.clientId + SEPARATOR;
    if (accessKey.type == Type.user_password) {
      plainText += accessKey.userName + SEPARATOR + accessKey.password;
    } else {
      plainText += accessKey.token;
    }

    byte[] iv = new byte[GCM_IV_LENGTH];
    new SecureRandom().nextBytes(iv);

    byte[] encrypted;
    try {
      Cipher cipher = Cipher.getInstance(ALGO);
      cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(KEY, "AES"), new GCMParameterSpec(128, iv));
      encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
    } catch (Exception e) {
      throw new RuntimeException("Failed to encrypt the access key!", e);
    }

    byte[] result = new byte[iv.length + encrypted.length];
    System.arraycopy(iv, 0, result, 0, iv.length);
    System.arraycopy(encrypted, 0, result, iv.length, encrypted.length);

    return Base64.getEncoder().encodeToString(result);
  }

  public static String encrypt(String clientId, String userName, String password) {
    return encrypt(new AccessKey(clientId, ZonedDateTime.now(), userName, password));
  }

  public static String encrypt(String clientId, String token) {
    return encrypt(new AccessKey(clientId, ZonedDateTime.now(), token));
  }

  public static AccessKey decrypt(String base64CipherText) {
    byte[] input = Base64.getDecoder().decode(base64CipherText);

    byte[] iv = new byte[GCM_IV_LENGTH];
    System.arraycopy(input, 0, iv, 0, iv.length);

    String plainText;
    try {
      Cipher cipher = Cipher.getInstance(ALGO);
      cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(KEY, "AES"), new GCMParameterSpec(128, iv));
      byte[] decrypted = cipher.doFinal(input, GCM_IV_LENGTH, input.length - GCM_IV_LENGTH);
      plainText = new String(decrypted, StandardCharsets.UTF_8);
    } catch (Exception e) {
      throw new RuntimeException("Failed to encrypt the access key!", e);
    }

    String[] parts = plainText.split(SEPARATOR);
    if (parts.length < 4) {
      throw new RuntimeException("Invalid access key format! Expected at least 4 parts but got " + parts.length);
    }

    Type type;
    try {
      type = Type.valueOf(parts[0]);
    } catch (IllegalArgumentException ex) {
      throw new RuntimeException("Unknown access key type: " + parts[0], ex);
    }

    ZonedDateTime created;
    try {
      if (parts[1].equalsIgnoreCase("null")) {
        created = null;
      } else {
        created = ZonedDateTime.parse(parts[1], Iso8601.DATE_TIME_MS_TZ_FORMATTER.get());
      }
    } catch (Exception e) {
      throw new RuntimeException("Invalid date format for access key: " + parts[1], e);
    }

    String clientId = parts[2];
    if (clientId.equalsIgnoreCase("null")) {
      clientId = null;
    }
    if (type == Type.user_password) {
      if (parts.length != 5) {
        throw new RuntimeException(
            "Invalid access key format for user_password type! Expected 5 parts but got " + parts.length
        );
      }
      String userName = parts[3];
      String password = parts[4];
      return new AccessKey(clientId, created, userName, password);
    } else if (type == Type.user_token) {
      if (parts.length != 4) {
        throw new RuntimeException(
            "Invalid access key format for user_token type! Expected 4 parts but got " + parts.length
        );
      }
      String token = parts[3];
      return new AccessKey(clientId, created, token);
    } else {
      throw new RuntimeException("Unhandled access key type: " + type);
    }
  }
}
