package com.martiansoftware.macnificent;

import java.net.NetworkInterface;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses, encapsulates, formats, and represents a 6-byte MAC address.
 *
 * @author <a href="http://martiansoftware.com/contact.html">Marty Lamb</a>
 */
public class MACAddress implements Comparable<MACAddress> {

    /**
     * Length of a MAC address, in _bytes
     */
    public static final int ETH_ALEN = 6;

    /**
     * regex used to parse MAC addresses
     */
    private static final Pattern MACREGEX;
    
    /**
     * Multicast bit in first byte of MAC.
     */
    static final byte FLAG_MCAST = 0x01;

    /**
     * Local bit in first byte of MAC.
     */
    static final byte FLAG_LOCAL = 0x02;

    /**
     * Build the MACREGEX pattern.  Allow whitespace or any of '-' ':' '.' or '_'
     * to separate _bytes in the MAC.  MAC byte separation is not required.
     * Each byte of the MAC is captured in its own group.
     */
    static {
        StringBuilder buf = new StringBuilder("^\\s*");
        for (int i = 0; i < ETH_ALEN; ++i) {
            buf.append("([0-9a-fA-F]{2})"); // a MAC byte with a capturing group
            if (i == 0) {
                buf.append("([\\s-:._]?)");  // ignored chars that may separate MAC _bytes
            } else if (i < ETH_ALEN - 1) {
                buf.append("\\2"); // backreference for whatever the separator is (if any)
            }
        }
        buf.append("\\s*$");
        MACREGEX = Pattern.compile(buf.toString());
    }
            
    /**
     * The _bytes comprising this MACAddress.
     */
    private byte[] _bytes = new byte[ETH_ALEN];

    /**
     * Creates a new MACAddress by copying the specified byte array (which must
     * contain ETH_ALEN _bytes)
     *
     * @param _bytes the byte array representing the new MACAddress
     */
    public MACAddress(byte[] bytes) {
        if (bytes.length != ETH_ALEN) throw new IllegalArgumentException("MACAddress bytes array must contain exactly " + ETH_ALEN + " bytes.  Instead received " + bytes.length + " (" + hexString(bytes, bytes.length) + ")");
        System.arraycopy(bytes, 0, this._bytes, 0, ETH_ALEN);
    }

    /**
     * Creates a new MACAddress from a given NetworkInterface
     * @param ni the NetworkInterface for which the MACAddress is to be created
     * @throws java.net.SocketException if thrown by NetworkInterface.getHardwareAddress()
     */
    public MACAddress(NetworkInterface ni) throws java.net.SocketException {
        this(ni.getHardwareAddress());
    }
    
    /**
     * Creates a new MACAddress from the specified String.  The String must contain
     * ETH_ALEN hexadecimal-encoded _bytes, optionally separated by any whitespace
     * or any of '-', ':', '.', or '_'.  Whatever separator is used (if any)
     * must be used consistently within the String.  Leading and trailing
     * whitespace are ignored.
     *
     * For example, the following are valid MACAddress strings:
     * <ul>
     * <li>11:22:33:44:55:66</li>
     * <li>1a-2b-3c-4d-5e-6f</li>
     * <li>FFFFFFFFFFFF</li>
     * <li>AA BB CC 11 22 33</li>
     * </ul>
     *
     * The following are NOT valid MACAddress strings:
     * <ul>
     * <li>11:22-33:44-55:66</li>
     * <li>1a2b3c 4d5e6f</li>
     * <li>FFFFFFFFFFF (not long enough)</li>
     * <li>FFFFFFFFFFFFF (too long)</li>
     * </ul>
     * @param s
     */
    public MACAddress(String s) {
        Matcher m = MACREGEX.matcher(s);
        if (!m.matches()) throw new IllegalArgumentException("Invalid MACAddress string: \"" + s + "\"");
        int groupIndex;
        for (int i = 0; i < ETH_ALEN; ++i) {
            groupIndex = i + 1;
            if (i > 0) ++groupIndex;
            _bytes[i] = (byte) Integer.parseInt(m.group(groupIndex), 16);
        }
    }

    /**
     * Returns the internal byte array storing this MAC address.  
     * <b>NOTE:</b> This is NOT a copy of the array.  Modifying the array
     * returned may yield unexpected behavior (e.g., changing the hashCode).
     * 
     * @return the internal byte array storing this MAC address.  
     */
    byte[] getInternalBytes() { return _bytes; }

    /**
     * Returns a copy of the internal byte array storing this MAC address.
     * 
     * @return a copy of the internal byte array storing this MAC address.
     */
    public byte[] getBytes() {
        return Arrays.copyOf(_bytes, ETH_ALEN);
    }

    /**
     * Indicates whether this MACAddress has its multicast bit set
     * @return true if this MACAddress has its multicast bit set
     */
    public boolean isMulticast() {
        return (_bytes[0] & FLAG_MCAST) == FLAG_MCAST;
    }

    /**
     * Indicates whether this MACAddress has its local bit set
     * @return true if this MACAddress has its local bit set
     */
    public boolean isLocal() {
        return (_bytes[0] & FLAG_LOCAL) == FLAG_LOCAL;
    }

    private String hexString(byte[] b, int len) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < len; ++i) {
            if (i != 0) buf.append(":");
            buf.append(String.format("%02x", b[i]));
        }
        return buf.toString();        
    }
    
    @Override
    public String toString() {
        return hexString(_bytes, ETH_ALEN);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MACAddress other = (MACAddress) obj;
        if (!Arrays.equals(this._bytes, other._bytes)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Arrays.hashCode(this._bytes);
        return hash;
    }

    @Override
    public int compareTo(MACAddress o) {
        int r;
        for (int i = 0; i < ETH_ALEN; ++i) {
            r = _bytes[i] - o._bytes[i];
            if (r != 0) return r;
        }
        return 0;
    }

}
