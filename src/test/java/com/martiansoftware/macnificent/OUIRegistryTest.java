package com.martiansoftware.macnificent;

/*
 * Copyright (C) Martian Software, Inc.
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
 * SPDX-License-Identifier: Apache-2.0
 *
 */

import java.util.Date;
import junit.framework.TestCase;

/**
 *
 * @author <a href="http://martylamb.com">Marty Lamb</a>
 */
public class OUIRegistryTest extends TestCase {
    
    public OUIRegistryTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getLastModified method, of class OUIRegistry.
     */
    public void testGetLastModified() throws Exception {
        System.out.println("getLastModified");
        OuiRegistry instance = new OuiRegistry();
        Date result = instance.getLastModified();
        assertEquals(TestConstants.TEST_REGISTRY_LASTMODIFIED, result);
    }

    /**
     * Test of size method, of class OUIRegistry.
     */
    public void testSize() throws Exception {
        System.out.println("size");
        OuiRegistry instance = new OuiRegistry();
        int result = instance.size();
        assertEquals(TestConstants.TEST_REGISTRY_SIZE, result);
    }

    /**
     * Test of getOUI method, of class OUIRegistry.
     */
    public void testGetOUI() throws Exception {
        System.out.println("getOUI");
        MacAddress mac = new MacAddress(TestConstants.TEST_MAC);
        OuiRegistry instance = new OuiRegistry();
        Oui result = instance.getOui(mac);
        assertNotNull(result);
    }

    /**
     * Test of format method, of class OUIRegistry.
     */
    public void testFormat_MacAddress() throws Exception {
        System.out.println("format");
        MacAddress mac = new MacAddress(TestConstants.TEST_MAC);
        OuiRegistry instance = new OuiRegistry();
        String result = instance.format(mac);
        assertEquals(TestConstants.TEST_MAC_FORMATTED, result);
    }

    /**
     * Test of format method, of class OUIRegistry.
     */
    public void testFormat_String() throws Exception {
        System.out.println("format");
        String macString = TestConstants.TEST_MAC;
        OuiRegistry instance = new OuiRegistry();
        String result = instance.format(macString);
        assertEquals(TestConstants.TEST_MAC_FORMATTED, result);
    }


}
