package com.dropchop.recyclone.base.api.model.security;

import com.dropchop.recyclone.base.api.model.utils.Iso8601;
import lombok.Getter;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Objects;

@Getter
@SuppressWarnings("unused")
public class AccessKey {
  private static final String SEPARATOR = "::";
  private static final String ALGO = "AES/GCM/NoPadding";
  private static final int GCM_IV_LENGTH = 12;

  public enum Type {
    user_password,
    user_token,
  }

  private final String clientId;
  private final String userId;
  private final String token;
  private final Type type;
  private final String userName;
  private final char[] password;
  private final ZonedDateTime created;

  public AccessKey(String clientId, ZonedDateTime created, String userId, String userName, char[] password) {
    this.clientId = clientId;
    this.userId = userId;
    this.created = created;
    this.token = null;
    this.type = Type.user_password;
    this.userName = userName;
    this.password = password;
  }

  public AccessKey(String clientId, ZonedDateTime created, String userId, String token) {
    this.clientId = clientId;
    this.userId = userId;
    this.created = created;
    this.token = token;
    this.type = Type.user_token;
    this.userName = null;
    this.password = null;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof AccessKey accessKey)) {
      return false;
    }
    return Objects.equals(getClientId(), accessKey.getClientId())
        && Objects.equals(getUserId(), accessKey.getUserId())
        && getType() == accessKey.getType();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getClientId(), getUserId(), getType());
  }

  @Override
  public String toString() {
    return "access-key." + clientId + "." + type.name();
  }

  private static SecretKey getKeyFromPassword(String password, String salt)
      throws NoSuchAlgorithmException, InvalidKeySpecException {

    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
    return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
  }

  public static String encrypt(ClientKeyConfig config, AccessKey accessKey) {
    String date = null;
    if (accessKey.created != null) {
      date = Iso8601.DATE_TIME_MS_TZ_FORMATTER.get().format(accessKey.created);
    }
    StringBuilder plainTextBuilder = new StringBuilder(256);
    plainTextBuilder.append(accessKey.type.name())
        .append(SEPARATOR)
        .append(date)
        .append(SEPARATOR);
    if (accessKey.type == Type.user_password) {
      plainTextBuilder.append(accessKey.userId)
          .append(SEPARATOR)
          .append(accessKey.userName)
          .append(SEPARATOR)
          .append(accessKey.password);
    } else {
      plainTextBuilder.append(accessKey.userId)
          .append(SEPARATOR)
          .append(accessKey.token);
    }
    String plainText = plainTextBuilder.toString();

    byte[] iv = new byte[GCM_IV_LENGTH];
    new SecureRandom().nextBytes(iv);

    byte[] encrypted;
    try {
      Cipher cipher = Cipher.getInstance(ALGO);
      cipher.init(
          Cipher.ENCRYPT_MODE,
          getKeyFromPassword(config.getSecret(), config.getSalt()),
          new GCMParameterSpec(128, iv)
      );
      encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
    } catch (Exception e) {
      throw new RuntimeException("Failed to encrypt the access key!", e);
    }

    byte[] prefix = (accessKey.clientId + SEPARATOR).getBytes(StandardCharsets.UTF_8);

    byte[] result = new byte[prefix.length + iv.length + encrypted.length];
    System.arraycopy(prefix, 0, result, 0, prefix.length);
    System.arraycopy(iv, 0, result, prefix.length, iv.length);
    System.arraycopy(encrypted, 0, result, prefix.length + iv.length, encrypted.length);

    return Base64.getEncoder().encodeToString(result);
  }

  public static String encrypt(ClientKeyConfig config, String userId, String userName, String password) {
    return encrypt(
        config, new AccessKey(
            config.getClientId(), ZonedDateTime.now(), userId, userName, password.toCharArray()
        )
    );
  }

  public static String encrypt(ClientKeyConfig config, String userId, String token) {
    return encrypt(config, new AccessKey(config.getClientId(), ZonedDateTime.now(), userId, token));
  }

  public static AccessKey decrypt(ClientKeyConfig config, String base64CipherText) {
    byte[] input = Base64.getDecoder().decode(base64CipherText);

    // 1. Extract the clientId (plaintext), up to the first occurrence of "::"
    int sepIdx = -1;
    for (int i = 0; i < input.length - 1; i++) {
      if (input[i] == ':' && input[i + 1] == ':') {
        sepIdx = i;
        break;
      }
    }
    if (sepIdx < 0) {
      throw new RuntimeException("ClientId separator not found in access key!");
    }
    String clientId = new String(input, 0, sepIdx, StandardCharsets.UTF_8);

    // IV and ciphertext follow after the separator (which is 2 bytes)
    int ivStart = sepIdx + 2;
    if (ivStart + GCM_IV_LENGTH > input.length) {
      throw new RuntimeException("Invalid access key: IV section missing or too short.");
    }
    byte[] iv = new byte[GCM_IV_LENGTH];
    System.arraycopy(input, ivStart, iv, 0, GCM_IV_LENGTH);

    // The rest is ciphertext
    int cipherTextStart = ivStart + GCM_IV_LENGTH;
    if (cipherTextStart > input.length) {
      throw new RuntimeException("No ciphertext found in access key!");
    }
    byte[] cipherText = new byte[input.length - cipherTextStart];
    System.arraycopy(input, cipherTextStart, cipherText, 0, cipherText.length);

    // Decrypt ciphertext
    String plainText;
    try {
      Cipher cipher = Cipher.getInstance(ALGO);
      cipher.init(
          Cipher.DECRYPT_MODE,
          getKeyFromPassword(config.getSecret(), config.getSalt()),
          new GCMParameterSpec(128, iv)
      );
      byte[] decrypted = cipher.doFinal(cipherText);
      plainText = new String(decrypted, StandardCharsets.UTF_8);
    } catch (Exception e) {
      throw new RuntimeException("Failed to decrypt the access key!", e);
    }

    // 5. Parse plaintext fields as before
    String[] parts = plainText.split(SEPARATOR, -1);
    if (parts.length < 4) {
      throw new RuntimeException("Invalid access key format! Expected at least 3 parts but got " + parts.length);
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

    if (type == Type.user_password) {
      if (parts.length != 5) {
        throw new RuntimeException(
            "Invalid access key format for user_password type! Expected 4 parts but got " + parts.length
        );
      }
      String userId = parts[2];
      String userName = parts[3];
      String password = parts[4];
      return new AccessKey(clientId, created, userId, userName, password.toCharArray());
    } else if (type == Type.user_token) {
      if (parts.length != 4) {
        throw new RuntimeException(
            "Invalid access key format for user_token type! Expected 3 parts but got " + parts.length
        );
      }
      String userId = parts[2];
      String token = parts[3];
      return new AccessKey(clientId, created, userId, token);
    } else {
      throw new RuntimeException("Unhandled access key type: " + type);
    }
  }
}
