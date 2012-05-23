package com.martiansoftware.macnificent;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;

/**
 * Class that encapsulates an entire IEEE OUI registry, provides a means to
 * lookup an OUI based upon a MACAddress, and provide reformatted MAC
 * address Strings that use the manufacturer's name.
 *
 * @author <a href="http://martiansoftware.com/contact.html">Marty Lamb</a>
 */
public class OUIRegistry {

    /**
     * Default location (in classpath) of IEEE OUI data file, in MACnificent's
     * binary format.
     */
    public static final String DEFAULT_RESOURCE = "ieee-oui.dat";

    /**
     * The actual OUI data
     */
    private Map<Integer, OUI> _byHashCode = new java.util.HashMap<Integer, OUI>();

    /**
     * The timestamp of when the OUI data was posted to the IEEE website or
     * was converted to MACnificent's binary format.
     */
    private Date _lastModified;

    /**
     * Creates a new OUIRegistry by reading the IEEE OUI data from the
     * classpath resource DEFAULT_RESOURCE ("ieee-oui.dat").  This requires a
     * MACnificent data jar file in the application's classpath.
     *
     * @throws IOException
     */
    public OUIRegistry() throws IOException {
        InputStream in = OUIRegistry.class.getClassLoader().getResourceAsStream(DEFAULT_RESOURCE);
        if (in == null) throw new IOException(DEFAULT_RESOURCE + " not found in classpath.");
        init(in);
    }

    /**
     * Creates a new OUIRegistry by reading the IEEE OUI data from the
     * specified InputStream in MACnificent's binary format.  Most users of
     * this library should be able to use the no-arg constructor and just
     * put the most recent data file <http://martiansoftware.com/MACnificent/oui-current.jar>
     * in the classpath.
     *
     * @param ouiData the IEEE OUI data source in MACnificent's binary format
     * @throws IOException
     */
    public OUIRegistry(InputStream ouiData) throws IOException {
        init(ouiData);
    }

    /**
     * Returns the time at which the either the OUI data was posted to the
     * IEEE website (ideally) or, if that information is not available,
     * the time at which the OUI data file was converted to MACnificent's
     * binary format.  Hopefully the former.
     *
     * @return a Date representing the last change to the OUI registry data.
     */
    public Date getLastModified() {
        return _lastModified;
    }

    /**
     * Performs the actual loading of the registry data.
     *
     * @param ouiData the IEEE OUI data source in MACnificent's binary format
     * @throws IOException
     */
    private void init(InputStream ouiData) throws IOException {
        DataInputStream din = new DataInputStream(ouiData);
        _lastModified = new Date(din.readLong());
        while (din.available() > 0) {
            OUI oui = new OUI(din);
            _byHashCode.put(oui.hashCode(), oui);
        }
    }

    /**
     * Returns the number of OUIs contained by this registry
     * @return the number of OUIs contained by this registry
     */
    public int size() { return _byHashCode.size(); }

    /**
     * Returns the OUI associated with the specified MAC address, or null
     * if no such OUI exists in this registry.
     * 
     * @param mac the MACAddress providing the OUI to retrieve
     * @return the requested OUI, or null if not found
     */
    public OUI getOUI(MACAddress mac) {
        OUI result = _byHashCode.get(OUI.hashCode(mac.getInternalBytes()));
        if (result == null && (mac.isLocal() || mac.isMulticast())) {
            byte[] b = mac.getBytes();
            b[0] &= ~(MACAddress.FLAG_LOCAL | MACAddress.FLAG_MCAST);
            result = _byHashCode.get(OUI.hashCode(b));
        }
        return result;
    }

    /**
     * Formats a MACAddress as a String, replacing the OUI portion of the
     * MAC with the short name of the manufacturer.
     *
     * For example, the MACAddress 00:00:00:11:22:33 is formatted as
     * "XEROX-11:22:33"
     *
     * @param mac the MACAddress to format
     * @return the reformatted MACAddress using the manufacturer's short name
     */
    public String format(MACAddress mac) {
        byte[] b = mac.getInternalBytes();
        OUI oui = _byHashCode.get(OUI.hashCode(mac.getInternalBytes()));
        StringBuilder buf = new StringBuilder();

        if (oui == null) {
            buf.append(String.format("Unknown-%02x-%02x-%02x", b[0], b[1], b[2]));
        } else {
            buf.append(oui.getShortName());
        }
        buf.append('-');

        for (int i = 3; i < MACAddress.ETH_ALEN; ++i) {
            if (i > 3) buf.append(':');
            buf.append(String.format("%02x", b[i]));
        }

        return buf.toString();
    }

    /**
     * Attempts to reformat a MACAddress as a String, replacing the OUI portion
     * of the MAC with the short name of the manufacturer.
     *
     * For example, the MACAddress 00:00:00:11:22:33 is formatted as
     * "XEROX-11:22:33"
     *
     * @param macString the MACAddress to format
     * @return the reformatted macString using the manufacturer's short name,
     * or the original, unmodified macString if it's not actually a valid MAC.
     */
    public String format(String macString) {
        try {
            return format(new MACAddress(macString));
        } catch (Exception e) {
            return macString;
        }
    }

    /**
     * Provides a simple interactive tester of this class.
     *
     * @param args ignored
     * @throws Exception if anything asplodes
     */
    public static void main(String[] args) throws Exception {
        OUIRegistry reg = new OUIRegistry();
        System.out.println("OUIRegistry loaded with " + reg.size() + " entries.");
        System.out.println("Enter MAC addresses (one per line) to try it out.");

        java.io.LineNumberReader in = new java.io.LineNumberReader(new java.io.InputStreamReader(System.in));
        String s = in.readLine();
        while (s != null) {
            MACAddress mac = new MACAddress(s);
            OUI oui = reg.getOUI(mac);
            System.out.println("   MAC Address:  " + mac);
            System.out.println("   isMulticast:  " + mac.isMulticast());
            System.out.println("       isLocal:  " + mac.isLocal());
            System.out.println("  Manufacturer:  " + (oui == null ? "Unknown" : oui.getManufacturer()));
            System.out.println("   Reformatted:  " + reg.format(mac));
            System.out.println();
            s = in.readLine();
        }

    }
}
