package com.martiansoftware.macnificent;

import java.util.Date;

/**
 *
 * @author mlamb
 */
public class TestConstants {
    public static final int TEST_REGISTRY_SIZE = 14238;
    public static final Date TEST_REGISTRY_LASTMODIFIED = new Date(1288553493623l);
    public static final int TEST_REGISTRY_DUPECOUNT = 3; // duplicate OUI defs in ieee oui text file

    public static final String TEST_MAC = "00:21:9b:07:20:74";
    public static final byte[] TEST_MAC_BYTES = {(byte) 0x00, (byte) 0x21, (byte) 0x9b, (byte) 0x07, (byte) 0x20, (byte) 0x74};
    public static final String TEST_MAC_SHORTNAME = "Dell";
    public static final String TEST_MAC_MFG = "Dell Inc";
    public static final String TEST_MAC_FORMATTED = "Dell-07:20:74";
}
