package com.martiansoftware.macnificent;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import junit.framework.TestCase;

/**
 *
 * @author mlamb
 */
public class IeeeOuiReaderTest extends TestCase {

    public IeeeOuiReaderTest(String testName) {
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

    public void testParsing() throws Exception {
        DataFileGenerator ouiReader = new DataFileGenerator(IeeeOuiReaderTest.class.getClassLoader().getResourceAsStream("oui.txt"));
        OUI oui = ouiReader.readOUI();
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(bout);
        dout.writeLong(System.currentTimeMillis());
        int count = 0;

        while (oui != null) {
            ++count;
            oui.store(dout);
            oui = ouiReader.readOUI();
        }
        dout.close();

        assertEquals(TestConstants.TEST_REGISTRY_SIZE + TestConstants.TEST_REGISTRY_DUPECOUNT, count); // closely married to the particular txt file in the test resources dir!
    }


}
