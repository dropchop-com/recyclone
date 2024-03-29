package com.dropchop.recyclone.model.api.utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 17. 12. 21.
 */
@SuppressWarnings("unused")
public interface Uuid {
  /**
   * Namespace used when name is a DNS name.
   */
  UUID NAMESPACE_DNS = UUID.fromString("6ba7b810-9dad-11d1-80b4-00c04fd430c8");

  /**
   * Namespace used when name is a URL.
   */
  UUID NAMESPACE_URL = UUID.fromString("6ba7b811-9dad-11d1-80b4-00c04fd430c8");
  byte[] NAMESPACE_URL_BYTES = toByteArray(NAMESPACE_URL);

  /**
   * Namespace used when name is an OID.
   */
  UUID NAMESPACE_OID = UUID.fromString("6ba7b812-9dad-11d1-80b4-00c04fd430c8");
  /**
   * Namespace used when name is an X500 identifier
   */
  UUID NAMESPACE_X500 = UUID.fromString("6ba7b814-9dad-11d1-80b4-00c04fd430c8");

  String RUNTIME_RANDOM = getRandom();

  private static long makeEpoch() {
    // UUID v1 timestamps must be in 100-nanoseconds interval since 00:00:00.000 15 Oct 1582.
    Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT-0"));
    c.set(Calendar.YEAR, 1582);
    c.set(Calendar.MONTH, Calendar.OCTOBER);
    c.set(Calendar.DAY_OF_MONTH, 15);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    c.set(Calendar.MILLISECOND, 0);
    return c.getTimeInMillis();
  }

  long START_EPOCH = makeEpoch();
  long TICK = 100; // 1 tick = 100ns
  long TICKS_PER_SECOND = 10_000_000L;

  // The min and max possible lsb for a UUID.
  //
  // This is not 0 and all 1's because Cassandra's TimeUUIDType compares the lsb parts as signed
  // byte arrays. So the min value is 8 times -128 and the max is 8 times +127.
  //
  // We ignore the UUID variant (namely, MIN_CLOCK_SEQ_AND_NODE has variant 2 as it should, but
  // MAX_CLOCK_SEQ_AND_NODE has variant 0) because I don't trust all UUID implementations to have
  // correctly set those (pycassa doesn't always for instance).
  long MIN_CLOCK_SEQ_AND_NODE = 0x8080808080808080L;
  long MAX_CLOCK_SEQ_AND_NODE = 0x7f7f7f7f7f7f7f7fL;


  private static long makeNodeFromString(String name) {
    try {
      MessageDigest digest = MessageDigest.getInstance("MD5");
      digest.update(name.getBytes(StandardCharsets.UTF_8));

      byte[] hash = digest.digest();

      long node = 0;
      for (int i = 0; i < 6; i++) {
        node |= (0x00000000000000ffL & (long) hash[i]) << (i * 8);
      }
      // Since we don't use the MAC address, the spec says that the multicast bit (the least significant
      // bit of the first byte of the node ID) must be 1.
      return node | 0x0000010000000000L;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static long makeFromName(String name) {
    long node = makeNodeFromString(name);
    long lsb = 0;
    lsb |= 0x0000000000003FFFL << 48;
    lsb |= 0x8000000000000000L;
    lsb |= node;
    return lsb;
  }

  private static long fromUnixTimestamp(Instant instant) {
    return (instant.toEpochMilli() - START_EPOCH) * 10000 + (instant.getNano() / TICK);
  }

  private static long fromUnixTimestamp(long tstamp) {
    return (tstamp - START_EPOCH) * 10000;
  }

  private static long makeMsb(long timestamp) {
    long msb = 0L;
    msb |= (0x00000000ffffffffL & timestamp) << 32;
    msb |= (0x0000ffff00000000L & timestamp) >>> 16;
    msb |= (0x0fff000000000000L & timestamp) >>> 48;
    msb |= 0x0000000000001000L; // sets the version to 1.
    return msb;
  }

  static long toUnixTimestamp(UUID uuid) {
    if (uuid.version() != 1) {
      throw new IllegalArgumentException(
        String.format(
          "Can only retrieve the unix timestamp for version 1 uuid (provided version %d)",
          uuid.version()));
    }
    long timestamp = uuid.timestamp();
    return (timestamp / 10000) + START_EPOCH;
  }

  static Instant toInstant(UUID uuid) {
    long timestamp = uuid.timestamp();
    long secs = (timestamp / TICKS_PER_SECOND) + START_EPOCH;
    long nano = (timestamp % TICKS_PER_SECOND) * TICK;
    return Instant.ofEpochSecond(secs, nano);
  }

  static UUID msAlignedMin(long timestamp) {
    return new UUID(makeMsb(fromUnixTimestamp(timestamp)), MIN_CLOCK_SEQ_AND_NODE);
  }

  static UUID msAlignedMax(long timestamp) {
    long uuidTstamp = fromUnixTimestamp(timestamp + 1) - 1;
    return new UUID(makeMsb(uuidTstamp), MAX_CLOCK_SEQ_AND_NODE);
  }

  static UUID fromTimeAndName(long timestamp, String name) {
    long uuidTstamp = fromUnixTimestamp(timestamp + 1) - 1;
    return new UUID(makeMsb(uuidTstamp), makeFromName(name));
  }

  static UUID fromTimeAndName(Instant instant, String name) {
    long uuidTstamp = fromUnixTimestamp(instant);
    return new UUID(makeMsb(uuidTstamp), makeFromName(name));
  }

  static byte[] toByteArray(UUID uuid) {
    byte[] bytes = new byte[16];
    long x = uuid.getMostSignificantBits();
    for (int i = 0; i < 8; i++) {
      bytes[7 - i] = (byte) x;
      x >>= 8;
    }
    x = uuid.getLeastSignificantBits();
    for (int i = 8; i < 16; i++) {
      bytes[23 - i] = (byte) x;
      x >>= 8;
    }
    return bytes;
  }

  static BigInteger toBigInteger(UUID uuid) {
    return new BigInteger(1, toByteArray(uuid));
  }

  static UUID fromByteArray(final byte[] uuid) {
    byte[] bytes = uuid;
    if (uuid.length < 16) {
      bytes = new byte[16];
      int delta = 16 - uuid.length;
      System.arraycopy(uuid, 0, bytes, delta, 16 - delta);
    } else if (uuid.length > 16) {
      bytes = new byte[16];
      int delta = uuid.length - 16;
      System.arraycopy(uuid, delta, bytes, 0, 16);
    }
    long msb = 0L;
    for (int i = 0; i < 8; i++) {
      msb <<= 8;
      msb ^= (long) bytes[i] & 0xFF;
    }
    long lsb = 0L;
    for (int i = 8; i < 16; i++) {
      lsb <<= 8;
      lsb ^= (long) bytes[i] & 0xFF;
    }
    return new UUID(msb, lsb);
  }

  static UUID fromBigInteger(BigInteger uuid) {
    return fromByteArray(uuid.toByteArray());
  }

  static UUID getNameBasedV3(String name) {
    byte[] nameBytes = name.getBytes(StandardCharsets.UTF_8);
    byte[] result = new byte[NAMESPACE_URL_BYTES.length + nameBytes.length];
    System.arraycopy(NAMESPACE_URL_BYTES, 0, result, 0, NAMESPACE_URL_BYTES.length);
    System.arraycopy(nameBytes, 0, result, NAMESPACE_URL_BYTES.length, nameBytes.length);

    return UUID.nameUUIDFromBytes(result);
  }

  static UUID getNameBasedV3(Class<?> clazz, String name) {
    String newName = clazz.getSimpleName() + "." + name;
    return getNameBasedV3(newName);
  }

  static UUID getTimeBased() {
    return fromTimeAndName(Instant.now(), RUNTIME_RANDOM);
  }

  static String getRandom() {
    return UUID.randomUUID().toString();
  }

  static boolean isUuid(String uuid) {
    if (uuid == null || uuid.isEmpty()) {
      return false;
    }
    try {
      if (uuid.length() != 36) {
        return false;
      }
      //noinspection ResultOfMethodCallIgnored
      UUID.fromString(uuid);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  static boolean isUuids(Collection<String> uuids) {
    for (String uuid : uuids) {
      if (!isUuid(uuid)) {
        return false;
      }
    }
    return true;
  }
}
