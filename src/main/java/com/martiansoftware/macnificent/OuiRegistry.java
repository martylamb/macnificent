package com.martiansoftware.macnificent;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;

/**
 * Class that encapsulates an entire IEEE OUI registry, provides a means to
 * lookup an OUI based upon a MacAddress, and provide reformatted MAC
 * address Strings that use the manufacturer's name.
 *
 * @author <a href="http://martiansoftware.com/contact.html">Marty Lamb</a>
 */
public class OuiRegistry {

    /**
     * Default location (in classpath) of IEEE OUI data file, in macnificent's
     * binary format.
     */
    public static final String DEFAULT_RESOURCE = "macnificent.dat";

    /**
     * The actual OUI data
     */
    private final Map<Integer, Oui> _byHashCode = new java.util.HashMap<Integer, Oui>();

    /**
     * The timestamp of when the OUI data was posted to the IEEE website or
     * was converted to macnificent's binary format.
     */
    private final Date _lastModified;

    /**
     * Creates a new OUIRegistry by reading the IEEE OUI data from the
     * classpath resource DEFAULT_RESOURCE ("macnificent.dat").  This requires a
     * Macnificent data jar file in the application's classpath.
     *
     * @throws IOException
     */
    public OuiRegistry() throws IOException {
        InputStream in = OuiRegistry.class.getClassLoader().getResourceAsStream(DEFAULT_RESOURCE);
        if (in == null) throw new IOException(DEFAULT_RESOURCE + " not found in classpath.");
        _lastModified = init(in);
    }

    /**
     * Creates a new OUIRegistry by reading the IEEE OUI data from the
     * specified InputStream in Macnificent's binary format.  Most users of
     * this library should be able to use the no-arg constructor and just
     * add the macnificent plugin to their build to create the data file.
     *
     * @param ouiData the IEEE OUI data source in macnificent's binary format
     * @throws IOException
     */
    public OuiRegistry(InputStream ouiData) throws IOException {
        _lastModified = init(ouiData);
    }

    /**
     * Returns the time at which the either the OUI data was posted to the
     * IEEE website (ideally) or, if that information is not available,
     * the time at which the OUI data file was converted to macnificent's
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
     * @param ouiData the IEEE OUI data source in macnificent's binary format
     * @return the timestamp of the data file's creation
     * @throws IOException
     */
    private Date init(InputStream ouiData) throws IOException {
        DataInputStream din = new DataInputStream(ouiData);
        Date result = new Date(din.readLong());
        while (din.available() > 0) {
            Oui oui = new Oui(din);
            _byHashCode.put(oui.hashCode(), oui);
        }
        return result;
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
     * @param mac the MacAddress providing the OUI to retrieve
     * @return the requested OUI, or null if not found
     */
    public Oui getOui(MacAddress mac) {
        Oui result = _byHashCode.get(Oui.hashCode(mac.getInternalBytes()));
        if (result == null && (mac.isLocal() || mac.isMulticast())) {
            byte[] b = mac.getBytes();
            b[0] &= ~(MacAddress.FLAG_LOCAL | MacAddress.FLAG_MCAST);
            result = _byHashCode.get(Oui.hashCode(b));
        }
        return result;
    }

    /**
     * Formats a MacAddress as a String, replacing the OUI portion of the
     * MAC with the short name of the manufacturer.
     *
     * For example, the MacAddress 00:00:00:11:22:33 is formatted as
     * "XEROX-11:22:33"
     *
     * @param mac the MacAddress to format
     * @return the reformatted MacAddress using the manufacturer's short name
     */
    public String format(MacAddress mac) {
        byte[] b = mac.getInternalBytes();
        Oui oui = _byHashCode.get(Oui.hashCode(mac.getInternalBytes()));
        StringBuilder buf = new StringBuilder();

        if (oui == null) {
            buf.append(String.format("Unknown-%02x-%02x-%02x", b[0], b[1], b[2]));
        } else {
            buf.append(oui.getShortName());
        }
        buf.append('-');

        for (int i = 3; i < MacAddress.ETH_ALEN; ++i) {
            if (i > 3) buf.append(':');
            buf.append(String.format("%02x", b[i]));
        }

        return buf.toString();
    }

    /**
     * Attempts to reformat a MacAddress as a String, replacing the OUI portion
     * of the MAC with the short name of the manufacturer.
     *
     * For example, the MacAddress 00:00:00:11:22:33 is formatted as
     * "XEROX-11:22:33"
     *
     * @param macString the MacAddress to format
     * @return the reformatted macString using the manufacturer's short name,
     * or the original, unmodified macString if it's not actually a valid MAC.
     */
    public String format(String macString) {
        try {
            return format(new MacAddress(macString));
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
        OuiRegistry reg = new OuiRegistry();
        System.out.println("OuiRegistry loaded with " + reg.size() + " entries.");
        System.out.println("Enter MAC addresses (one per line) to try it out.");

        java.io.LineNumberReader in = new java.io.LineNumberReader(new java.io.InputStreamReader(System.in));
        String s = in.readLine();
        while (s != null) {
            MacAddress mac = new MacAddress(s); // can also create from byte[] or NetworkInterface
            Oui oui = reg.getOui(mac);
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
