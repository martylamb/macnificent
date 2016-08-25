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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

/**
 * Encapsulates an <i>Organizationally Unique Identifier</i>,
 * a 24-bit identifier assigned by IEEE to uniquely identify a hardware
 * _manufacturer.  Most programmers won't have create these directly, but will
 * instead obtain them from an OUIRegistry.
 *
 * @author <a href="http://martiansoftware.com/contact.html">Marty Lamb</a>
 */
public class Oui {

    /**
     * The three-byte OUI
     */
    private byte[] _bytes;

    /**
     * The full _manufacturer name as listed by the IEEE
     */
    private String _manufacturer;

    /**
     * A "short name" automatically generated from the _manufacturer name by
     * macnificent.
     */
    private transient String _shortName;

    /**
     * Creates a new Oui
     * @param _bytes the 3-byte identifier assigned by IEEE
     * @param _manufacturer the full _manufacturer name as listed by IEEE
     */
    public Oui(byte[] bytes, String manufacturer) {
        if (bytes.length != 3) throw new IllegalArgumentException("OUI byte array must contain exactly three bytes.");
        if (manufacturer == null) throw new NullPointerException("OUI manufacturer may not be null.");
        this._bytes = new byte[3];
        System.arraycopy(bytes, 0, this._bytes, 0, 3);
        this._manufacturer = manufacturer;
    }

    /**
     * Creates a new OUI by reading it in macnificent's binary format
     * @param in the binary Oui source
     * @throws IOException
     */
    public Oui(DataInput in) throws IOException {
        _bytes = new byte[3];
        in.readFully(_bytes);
        _manufacturer = in.readUTF();
    }

    /**
     * Returns the full _manufacturer name as listed by IEEE
     * @return the full _manufacturer name as listed by IEEE
     */
    public String getManufacturer() { return _manufacturer; }

    /**
     * Returns a copy of the 3-byte identifier assigned by IEEE
     * @return a copy of the 3-byte identifier assigned by IEEE
     */
    public byte[] getBytes() {
        return Arrays.copyOf(_bytes, 3);
    }

    /**
     * Copies the 3-byte identifier assigned by IEEE to the specified destination
     * @param dest the destination array to which the identifier is to be copied
     * @param offset the offset in the destination array in which the identifier
     * is to be copied
     */
    public void copyBytes(byte[] dest, int offset) {
        for (int i = 0; i < 3; ++i) dest[offset + i] = _bytes[i];
    }

    /**
     * Returns an automatically-generated "short name" based upon the full
     * _manufacturer name.  The short name is generated by taking the first
     * word of the _manufacturer name (ignoring "The"), removing any trailing
     * characters that are not letters, and then removing any periods to
     * collapse acronyms.
     *
     * If a short name cannot be determined based upon the _manufacturer name,
     * the name "Unknown-AA-BB-CC" will be returned, where "AA-BB-CC" is the
     * OUI ID in hexadecimal form.
     *
     * @return an automatically-generated "short name" based upon the full
     * _manufacturer name.
     */
    public String getShortName() {
        if (_shortName == null) {
            String[] words = _manufacturer.split("\\s+", 3);
            int i = (words[0].equalsIgnoreCase("The")) ? 1 : 0;
            _shortName = words[i].replaceAll("[^a-zA-Z]*$", "").replaceAll("\\.", "");
        }
        if (_shortName == null || _shortName.length() == 0) _shortName = String.format("Unknown-%02x-%02x-%02x", _bytes[0], _bytes[1], _bytes[2]);
        return _shortName;
    }

    /**
     * Returns a hash code for the OUI represented by the specified _bytes.  The
     * hash code is computed as the unsigned 24-bit value contained in the OUI
     * in network byte order.
     *
     * @param _bytes an array containing the 3-byte OUI identifier
     * @return a hash code
     */
    public static int hashCode(byte[] bytes) {
        return (  ((0x000000ff & bytes[0]) << 16) // ((0x000000fc & _bytes[0]) << 16) // mask out local and multicast bits
                | ((0x000000ff & bytes[1]) << 8)
                | ((0x000000ff & bytes[2])));
    }

    @Override
    public String toString() {
        return String.format("%02x-%02x-%02x: [%s] %s", _bytes[0], _bytes[1], _bytes[2], getShortName(), _manufacturer);
    }
    
    @Override
    public int hashCode() {
        return hashCode(_bytes);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Oui other = (Oui) obj;
        if (!Arrays.equals(this._bytes, other._bytes)) {
            return false;
        }
        return true;
    }
}
