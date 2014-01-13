macnificent
===========

Marty Lamb mlamb@martiansoftware.com at [Martian Software, Inc.](http://martiansoftware.com)

Macnificent is a set of utility classes for working with MAC addresses 
in Java. In addition to a lightweight MacAddress class and parser, this 
includes IEEE OUI registry information and a formatter for MAC addresses 
that displays an abbreviated form of the device's manufacturer's name.

If you're using this project, you probably also want to use
[macnificent-plugin](https://github.com/martylamb/macnificent-plugin)
to generate the data file for your project.

Example Usage:
--------------

The following main() method is included in the OuiRegistry class
provided with this project.  It showcases functionality fairly well:

```java
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
```

Add the repository to your project:
-----------------------------------

```xml
<project>
	...
    <repositories>
        <repository>
            <id>martiansoftware</id>
            <url>http://mvn.martiansoftware.com</url>
        </repository>
    </repositories> 
	...
</project>
```

Add the dependency to your project:
-----------------------------------

```xml
<dependencies>
	<dependency>
		<groupId>com.martiansoftware</groupId>
		<artifactId>macnificent</artifactId>
		<version>0.2.0-SNAPSHOT</version>
		<scope>compile</scope>
	</dependency>
</dependencies>
```
