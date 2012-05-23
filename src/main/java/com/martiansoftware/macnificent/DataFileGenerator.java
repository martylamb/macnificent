package com.martiansoftware.macnificent;

import java.io.*;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Converts the IEEE OUI data file from http://standards.ieee.org/regauth/oui/oui.txt
 * to MACnificent's binary file format.  Most users of this library won't need
 * this class at all and can simply use a data file from http://martiansoftware.com/MACnificent/macnificent-data-*.jar
 *
 * @author mlamb
 */
public class DataFileGenerator {

    public static final String IEEE_OUI_TXT_URL = "http://standards.ieee.org/regauth/oui/oui.txt";
    
    private LineNumberReader _in;
    
    private Matcher _m = Pattern.compile("^([0-9a-fA-f]{2})([0-9a-fA-f]{2})([0-9a-fA-f]{2})\\s+\\(base 16\\)\\s+(.*)$").matcher("");

    /**
     * Creates a new DataFileGenerator that will read the supplied InputStream.
     * @param in the InputStream to read.
     */
    DataFileGenerator(InputStream in) {
        this._in = new LineNumberReader(new InputStreamReader(in));
    }

    /**
     * Reads a single OUI from the input text stream
     * @return a single OUI from the input text stream, or null if eof is reached.
     * @throws IOException 
     */
    OUI readOUI() throws IOException {
        String s = _in.readLine();
        while (s != null) {
            _m.reset(s);
            if (_m.matches()) {
                return new OUI(new byte[] { (byte) Integer.parseInt(_m.group(1), 16),
                                                            (byte) Integer.parseInt(_m.group(2), 16),
                                                            (byte) Integer.parseInt(_m.group(3), 16)},
                                            _m.group(4));
            }
            s = _in.readLine();
        }
        return null;
    }


    public static void main(String args[]) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: " + DataFileGenerator.class.getName() + " OUTPUT_FILE");
            System.exit(1);
        }

        File destFile = new File(args[0]);
        File parent = destFile.getParentFile();
        if(!parent.exists()) {
            if (!parent.mkdirs()) throw new IOException("Unable to create directory " + parent.getAbsolutePath());
        }
        URL url = new URL(IEEE_OUI_TXT_URL);
        DataFileGenerator ouiReader = new DataFileGenerator(url.openStream());
        DataOutputStream dout = new DataOutputStream(new FileOutputStream(args[0]));
        dout.writeLong(System.currentTimeMillis());
        OUI oui = ouiReader.readOUI();
        while (oui != null) {
            System.out.print('.');
            oui.store(dout);
            oui = ouiReader.readOUI();
        }
        dout.close();
        System.out.println();
    }
}
