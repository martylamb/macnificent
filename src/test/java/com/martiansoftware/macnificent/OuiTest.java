package com.martiansoftware.macnificent;

import java.io.DataOutput;
import java.util.Arrays;
import junit.framework.TestCase;

/**
 *
 * @author mlamb
 */
public class OuiTest extends TestCase {
    
    public OuiTest(String testName) {
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
     * Test of getManufacturer method, of class OUI.
     */
    public void testGetManufacturer() throws Exception {
        System.out.println("getManufacturer");
        Oui instance = new OuiRegistry().getOui(new MacAddress(TestConstants.TEST_MAC));
        String result = instance.getManufacturer();
        assertEquals("Dell Inc", result);
    }

    /**
     * Test of getBytes method, of class OUI.
     */
    public void testGetBytes() throws Exception {
        System.out.println("getBytes");
        Oui instance = new OuiRegistry().getOui(new MacAddress(TestConstants.TEST_MAC));
        byte[] expResult = {(byte) 0x00, (byte) 0x21, (byte) 0x9b};
        byte[] result = instance.getBytes();
        assertTrue(Arrays.equals(expResult, result));
        assertTrue(instance.getBytes() != instance.getBytes());
    }

    /**
     * Test of copyBytes method, of class OUI.
     */
    public void testCopyBytes() throws Exception {
        System.out.println("copyBytes");
        byte[] dest = new byte[4];
        int offset = 1;
        Oui instance = new OuiRegistry().getOui(new MacAddress(TestConstants.TEST_MAC));
        instance.copyBytes(dest, offset);
        assertEquals(TestConstants.TEST_MAC_BYTES[0], dest[1]);
        assertEquals((byte) 0x21, dest[2]);
        assertEquals((byte) 0x9b, dest[3]);
    }

    /**
     * Test of getShortName method, of class OUI.
     */
    public void testGetShortName() throws Exception {
        System.out.println("getShortName");
        Oui instance = new OuiRegistry().getOui(new MacAddress(TestConstants.TEST_MAC));
        String result = instance.getShortName();
        assertEquals(TestConstants.TEST_MAC_SHORTNAME, result);
    }

    public void testEquals() throws Exception {
        OuiRegistry reg = new OuiRegistry();
        MacAddress m1 = new MacAddress(TestConstants.TEST_MAC);
        byte[] b = m1.getBytes();
        for (int i = 3; i < 6; ++i) b[i] = (byte) ~b[i];
        MacAddress m2 = new MacAddress(b);
        Oui o1 = reg.getOui(m1);
        Oui o2 = reg.getOui(m2);
        assertEquals(o1, o2);
        b[1] = (byte) ~b[1];
        m2 = new MacAddress(b);
        o2 = reg.getOui(m2);
        assertTrue(!o1.equals(o2));
    }
}
