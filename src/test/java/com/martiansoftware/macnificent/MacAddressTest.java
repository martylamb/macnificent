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

import java.util.Arrays;
import junit.framework.TestCase;

/**
 *
 * @author mlamb
 */
public class MacAddressTest extends TestCase {
    
    public MacAddressTest(String testName) {
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
     * Test of getInternalBytes method, of class MacAddress.
     */
    public void testGetInternalBytes() {
        System.out.println("getInternalBytes");
        byte[] b = { 0, 1, 2, 3, 4, 5 };
        MacAddress m1 = new MacAddress(b);
        byte[] b2 = m1.getInternalBytes();
        assertTrue (b != m1.getInternalBytes());
        assertTrue (b2 == m1.getInternalBytes());
        assertTrue(m1.getInternalBytes() == m1.getInternalBytes());
    }

    /**
     * Test of getBytes method, of class MacAddress.
     */
    public void testGetBytes() {
        System.out.println("getBytes");
        MacAddress instance = new MacAddress(TestConstants.TEST_MAC);
        assertTrue(Arrays.equals(instance.getBytes(), TestConstants.TEST_MAC_BYTES));
        assertTrue(instance.getBytes() != instance.getBytes());
        assertTrue(instance.getBytes() != instance.getInternalBytes());
    }

    /**
     * Test of isMulticast method, of class MacAddress.
     */
    public void testIsMulticast() {
        System.out.println("isMulticast");
        MacAddress m1 = new MacAddress(TestConstants.TEST_MAC);
        assertTrue(!m1.isMulticast());
        byte[] b = m1.getBytes();
        b[0] |= 1;
        m1 = new MacAddress(b);
        assertTrue(m1.isMulticast());
    }

    /**
     * Test of isLocal method, of class MacAddress.
     */
    public void testIsLocal() {
        System.out.println("isLocal");
        MacAddress m1 = new MacAddress(TestConstants.TEST_MAC);
        assertTrue(!m1.isLocal());
        byte[] b = m1.getBytes();
        b[0] |= 2;
        m1 = new MacAddress(b);
        assertTrue(m1.isLocal());
    }

    /**
     * Test of toString method, of class MacAddress.
     */
    public void testToString() {
        System.out.println("toString");
        MacAddress m1 = new MacAddress(TestConstants.TEST_MAC_BYTES);
        assertEquals(TestConstants.TEST_MAC, m1.toString());
    }

    /**
     * Test of equals method, of class MacAddress.
     */
    public void testEquals() {
        System.out.println("equals");
        MacAddress m1 = new MacAddress(TestConstants.TEST_MAC);
        MacAddress m2 = new MacAddress(TestConstants.TEST_MAC);
        assertTrue(m1.equals(m2));
        byte[] b = m1.getBytes();
        b[5] = (byte) ~b[5];
        MacAddress m3 = new MacAddress(b);
        assertTrue(!m1.equals(m3));
    }

    public void testTooLong() {
        byte[] b = {01, 02, 03, 04, 05, 06, 07};
        try {
            MacAddress m1 = new MacAddress(b);
            fail("Accepted a 7-byte MAC.");
        } catch (Exception expected) {
            assertTrue(expected.getMessage().contains(" 7 ("));
        }
    }
}
