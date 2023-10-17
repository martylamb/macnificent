/*
 * (C) Copyright Martian Software, Inc.
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

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simple utility class that takes an IEEE MA-L data file (as obtained
 * canonically from http://standards-oui.ieee.org/oui/oui.txt) on stdin
 * and generates a binary data file suitable for use by OuiRegistry on
 * stdout.
 * 
 * There is one optional command line argument, which is the date that will
 * be returned by the resulting OuiRegistry's getLastModified() method.  This
 * argument must be provided in ISO8601 format, e.g. as obtained on a *nix
 * command line via <code>date -Iseconds</code>
 * 
 * To run this, execute something along the lines of:
 * <code>cat oui.txt | mvn -q exec:java -Dexec.mainClass=com.martiansoftware.macnificent.GenerateDataFile > macnificent.dat</code>
 * <code>cat oui.txt | mvn -q exec:java -Dexec.mainClass=com.martiansoftware.macnificent.GenerateDataFile -Dexec.args="$(date -Iseconds)" > macnificent.dat</code>
 * @author mlamb
 */
public class GenerateDataFile {
    
    private static void usageAndExit(String msg) {
        System.err.println(msg);
        System.exit(1);        
    }
    
    private static void processOuiTxt(long lastModified) throws IOException {
        Matcher m = Pattern.compile("^\\s*([0-9a-fA-f]{2})([0-9a-fA-f]{2})([0-9a-fA-f]{2})\\s+\\(base 16\\)\\s+(.*)$").matcher("");
        byte[] oui = new byte[3];
        int ouicount = 0;
        LineNumberReader r = new LineNumberReader(new InputStreamReader(System.in));
        DataOutput dout = new DataOutputStream(System.out);
        dout.writeLong(lastModified);
        String s = r.readLine();
        while (s != null) {
            m.reset(s);
            if (m.matches()) {
                for (int i = 0; i < 3; ++i) oui[i] = (byte) Integer.parseInt(m.group(i + 1), 16);
                dout.write(oui, 0, 3);
                dout.writeUTF(m.group(4));
                ++ouicount;
            }
            s = r.readLine();
        }
        System.err.format("Added %d OUIs.", ouicount);        
    }
    
    public static void main(String[] args) throws IOException {
        long lastModified = System.currentTimeMillis();
        
        if (args.length > 0) {
            if (args.length > 1) {
                usageAndExit("Only one argument is supported: the last modified time of the OUI database in ISO8601 format, e.g. as provided by 'date -Iseconds'.\n"
                            + "If omitted, the current time will be used as the last modified time of the OUI database.");
            }
            try {
                TemporalAccessor ta = DateTimeFormatter.ISO_INSTANT.parse(args[0]);
                Instant i = Instant.from(ta);
                lastModified = i.toEpochMilli();
            } catch (DateTimeParseException e) {
                usageAndExit("Not a valid ISO8601 date/time: " + e.getMessage());
            }
        }
        
        processOuiTxt(lastModified);
    }
}
