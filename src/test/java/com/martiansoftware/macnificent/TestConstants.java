/*
 * (C) Copyright 2012-2016 Martian Software, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

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
