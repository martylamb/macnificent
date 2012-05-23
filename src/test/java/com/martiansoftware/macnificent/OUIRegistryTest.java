package com.martiansoftware.macnificent;

import java.util.Date;
import junit.framework.TestCase;

/**
 *
 * @author mlamb
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
        OUIRegistry instance = new OUIRegistry();
        Date result = instance.getLastModified();
        assertEquals(TestConstants.TEST_REGISTRY_LASTMODIFIED, result);
    }

    /**
     * Test of size method, of class OUIRegistry.
     */
    public void testSize() throws Exception {
        System.out.println("size");
        OUIRegistry instance = new OUIRegistry();
        int result = instance.size();
        assertEquals(TestConstants.TEST_REGISTRY_SIZE, result);
    }

    /**
     * Test of getOUI method, of class OUIRegistry.
     */
    public void testGetOUI() throws Exception {
        System.out.println("getOUI");
        MACAddress mac = new MACAddress(TestConstants.TEST_MAC);
        OUIRegistry instance = new OUIRegistry();
        OUI result = instance.getOUI(mac);
        assertNotNull(result);
    }

    /**
     * Test of format method, of class OUIRegistry.
     */
    public void testFormat_MACAddress() throws Exception {
        System.out.println("format");
        MACAddress mac = new MACAddress(TestConstants.TEST_MAC);
        OUIRegistry instance = new OUIRegistry();
        String result = instance.format(mac);
        assertEquals(TestConstants.TEST_MAC_FORMATTED, result);
    }

    /**
     * Test of format method, of class OUIRegistry.
     */
    public void testFormat_String() throws Exception {
        System.out.println("format");
        String macString = TestConstants.TEST_MAC;
        OUIRegistry instance = new OUIRegistry();
        String result = instance.format(macString);
        assertEquals(TestConstants.TEST_MAC_FORMATTED, result);
    }


}
