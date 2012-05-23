package com.martiansoftware.macnificent;

import java.util.Arrays;
import junit.framework.TestCase;

/**
 *
 * @author mlamb
 */
public class MACAddressTest extends TestCase {
    
    public MACAddressTest(String testName) {
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
     * Test of getInternalBytes method, of class MACAddress.
     */
    public void testGetInternalBytes() {
        System.out.println("getInternalBytes");
        byte[] b = { 0, 1, 2, 3, 4, 5 };
        MACAddress m1 = new MACAddress(b);
        byte[] b2 = m1.getInternalBytes();
        assertTrue (b != m1.getInternalBytes());
        assertTrue (b2 == m1.getInternalBytes());
        assertTrue(m1.getInternalBytes() == m1.getInternalBytes());
    }

    /**
     * Test of getBytes method, of class MACAddress.
     */
    public void testGetBytes() {
        System.out.println("getBytes");
        MACAddress instance = new MACAddress(TestConstants.TEST_MAC);
        assertTrue(Arrays.equals(instance.getBytes(), TestConstants.TEST_MAC_BYTES));
        assertTrue(instance.getBytes() != instance.getBytes());
        assertTrue(instance.getBytes() != instance.getInternalBytes());
    }

    /**
     * Test of isMulticast method, of class MACAddress.
     */
    public void testIsMulticast() {
        System.out.println("isMulticast");
        MACAddress m1 = new MACAddress(TestConstants.TEST_MAC);
        assertTrue(!m1.isMulticast());
        byte[] b = m1.getBytes();
        b[0] |= 1;
        m1 = new MACAddress(b);
        assertTrue(m1.isMulticast());
    }

    /**
     * Test of isLocal method, of class MACAddress.
     */
    public void testIsLocal() {
        System.out.println("isLocal");
        MACAddress m1 = new MACAddress(TestConstants.TEST_MAC);
        assertTrue(!m1.isLocal());
        byte[] b = m1.getBytes();
        b[0] |= 2;
        m1 = new MACAddress(b);
        assertTrue(m1.isLocal());
    }

    /**
     * Test of toString method, of class MACAddress.
     */
    public void testToString() {
        System.out.println("toString");
        MACAddress m1 = new MACAddress(TestConstants.TEST_MAC_BYTES);
        assertEquals(TestConstants.TEST_MAC, m1.toString());
    }

    /**
     * Test of equals method, of class MACAddress.
     */
    public void testEquals() {
        System.out.println("equals");
        MACAddress m1 = new MACAddress(TestConstants.TEST_MAC);
        MACAddress m2 = new MACAddress(TestConstants.TEST_MAC);
        assertTrue(m1.equals(m2));
        byte[] b = m1.getBytes();
        b[5] = (byte) ~b[5];
        MACAddress m3 = new MACAddress(b);
        assertTrue(!m1.equals(m3));
    }

}
