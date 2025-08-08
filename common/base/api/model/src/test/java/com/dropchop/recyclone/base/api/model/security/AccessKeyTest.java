package com.dropchop.recyclone.base.api.model.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.time.ZonedDateTime;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 08. 08. 2025
 */
class AccessKeyTest {
  /**
   * Tests the encryption of an AccessKey with user_password type.
   */
  @Test
  public void testEncryptUserPasswordType() {
    ClientKeyConfig clientKeyConfig = new ClientKeyConfig(
        "testClient", URI.create("example.com"), "Gs*t4USB29uAMVex!vmRJ#8g6jt^cq3n", "QqvBbrGSFVAVAd9*"
    );
    ZonedDateTime created = ZonedDateTime.now();
    AccessKey accessKey = new AccessKey(
        "testClient", created, "cac6d703-a941-4fc0-bb23-1201a5976718", "nikola", "dev1234".toCharArray()
    );

    String encryptedKey = AccessKey.encrypt(clientKeyConfig, accessKey);

    Assertions.assertNotNull(encryptedKey, "Encrypted key should not be null");
    Assertions.assertFalse(encryptedKey.isEmpty(), "Encrypted key should not be empty");
  }

  /**
   * Tests the encryption of an AccessKey with user_token type.
   */
  @Test
  public void testEncryptUserTokenType() {
    ClientKeyConfig clientKeyConfig = new ClientKeyConfig(
        "testClient", URI.create("example.com"), "testSecret123!", "testSalt123!"
    );
    ZonedDateTime created = ZonedDateTime.now();
    AccessKey accessKey = new AccessKey("testClient", created, "testUserId", "testToken");

    String encryptedKey = AccessKey.encrypt(clientKeyConfig, accessKey);

    Assertions.assertNotNull(encryptedKey, "Encrypted key should not be null");
    Assertions.assertFalse(encryptedKey.isEmpty(), "Encrypted key should not be empty");
  }

  /**
   * Tests the encryption of user credentials (userId, userName, password) directly.
   */
  @Test
  public void testEncryptDirectUserCredentials() {
    ClientKeyConfig clientKeyConfig = new ClientKeyConfig(
        "testClient", URI.create("example.com"), "testSecret123!", "testSalt123!"
    );

    String encryptedKey = AccessKey.encrypt(clientKeyConfig, "testUserId", "testUserName", "testPassword");

    Assertions.assertNotNull(encryptedKey, "Encrypted key should not be null");
    Assertions.assertFalse(encryptedKey.isEmpty(), "Encrypted key should not be empty");
  }

  /**
   * Tests the encryption of user credentials (userId, token) directly.
   */
  @Test
  public void testEncryptDirectUserToken() {
    ClientKeyConfig clientKeyConfig = new ClientKeyConfig(
        "testClient", URI.create("example.com"), "testSecret123!", "testSalt123!"
    );

    String encryptedKey = AccessKey.encrypt(clientKeyConfig, "testUserId", "testToken");

    Assertions.assertNotNull(encryptedKey, "Encrypted key should not be null");
    Assertions.assertFalse(encryptedKey.isEmpty(), "Encrypted key should not be empty");
  }

  /**
   * Tests the encryption and decryption workflow for user_password type.
   */
  @Test
  public void testEncryptAndDecryptUserPasswordType() {
    ClientKeyConfig clientKeyConfig = new ClientKeyConfig(
        "testClient", URI.create("example.com"), "testSecret123!", "testSalt123!"
    );
    ZonedDateTime created = ZonedDateTime.now();
    AccessKey originalKey = new AccessKey(
        "testClient", created, "testUserId", "testUserName", "testPassword".toCharArray()
    );

    String encryptedKey = AccessKey.encrypt(clientKeyConfig, originalKey);
    AccessKey decryptedKey = AccessKey.decrypt(clientKeyConfig, encryptedKey);

    Assertions.assertEquals(originalKey.getClientId(), decryptedKey.getClientId(), "Client IDs should match");
    Assertions.assertEquals(originalKey.getUserId(), decryptedKey.getUserId(), "User IDs should match");
    Assertions.assertEquals(originalKey.getUserName(), decryptedKey.getUserName(), "Usernames should match");
    Assertions.assertArrayEquals(originalKey.getPassword(), decryptedKey.getPassword(), "Passwords should match");
    Assertions.assertEquals(originalKey.getType(), decryptedKey.getType(), "Types should match");
    Assertions.assertEquals(
        originalKey.getCreated().toEpochSecond(),
        decryptedKey.getCreated().toEpochSecond(),
        "Created timestamps should match"
    );
  }

  /**
   * Tests the encryption and decryption workflow for user_token type.
   */
  @Test
  public void testEncryptAndDecryptUserTokenType() {
    ClientKeyConfig clientKeyConfig = new ClientKeyConfig(
        "testClient", URI.create("example.com"), "testSecret123!", "testSalt123!"
    );
    ZonedDateTime created = ZonedDateTime.now();
    AccessKey originalKey = new AccessKey("testClient", created, "testUserId", "testToken");

    String encryptedKey = AccessKey.encrypt(clientKeyConfig, originalKey);
    AccessKey decryptedKey = AccessKey.decrypt(clientKeyConfig, encryptedKey);

    Assertions.assertEquals(originalKey.getClientId(), decryptedKey.getClientId(), "Client IDs should match");
    Assertions.assertEquals(originalKey.getUserId(), decryptedKey.getUserId(), "User IDs should match");
    Assertions.assertEquals(originalKey.getToken(), decryptedKey.getToken(), "Tokens should match");
    Assertions.assertEquals(originalKey.getType(), decryptedKey.getType(), "Types should match");
    Assertions.assertEquals(
        originalKey.getCreated().toEpochSecond(),
        decryptedKey.getCreated().toEpochSecond(),
        "Created timestamps should match"
    );
  }
}