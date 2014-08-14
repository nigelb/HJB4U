/*
 * HJB4U is toolchain for creating a HyperJAXB front end for database users.
 * Copyright (C) 2010  NigelB
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package hjb4u;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class Util {
    public static boolean parseBoolean(String toParse) {
        return toParse != null && (toParse.equals("1") || toParse.toLowerCase().equals("true"));
    }


    public static String joinPath(String prefix, String... tojoin) {
        StringBuffer buf = new StringBuffer(prefix);
        for (String s : tojoin) {
            buf.append(File.separator).append(s);
        }
        return buf.toString();
    }

    public static void copyResource(URL from, File to) {
        if (from == null) {
            System.out.printf("Resource could not be copied to: %s%n", to);
            return;
        }
        System.out.printf("Copring resource: %s to: %s%n", from, to);
        try {
            OutputStream fos = new BufferedOutputStream(new FileOutputStream(to));
            InputStream in = new BufferedInputStream(from.openStream());
            int data = -1;
            while (true) {
                data = in.read();
                if (data == -1) break;
                fos.write(data);

            }
            fos.close();
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static ArrayList<URL> findResourceSiblings(URL res) throws IOException, URISyntaxException {
        ArrayList<URL> toRet = new ArrayList<URL>();
        if (res.getProtocol().equalsIgnoreCase("jar")) {
            String file = res.getFile();
            String[] parts = file.split("!");
            String jarFile = parts[0];
            String jarLoc = parts[1];
            ZipFile f = new ZipFile(new File(new URL(jarFile).toURI()));

            String[] jarComps = jarLoc.split("/");
            int pos = 0;
            if (jarComps[0].length() == 0) {
                pos = 1;
            }
            StringBuilder bu = new StringBuilder();
            String del = "";
            for (int i = pos; i < (jarComps.length - 1); i++) {
                bu.append(del).append(jarComps[i]);
                del = "\\/";
            }
            bu.append(del).append("*");
            Pattern p = Pattern.compile(bu.toString());
            Matcher m;
            Enumeration<? extends ZipEntry> entries = f.entries();
            while (entries.hasMoreElements()) {
                ZipEntry zipEntry = entries.nextElement();
                m = p.matcher(zipEntry.getName());
                if (m.find()) {
                    toRet.add(makeJARURL(jarFile, zipEntry.getName()));
                }

            }
        } else if (res.getProtocol().equalsIgnoreCase("file")) {
            for (File file : new File(res.toURI()).getParentFile().listFiles()) {
                toRet.add(file.toURI().toURL());
            }
        }
        return toRet;
    }

    private static URL makeJARURL(String jarFile, String resourceLoc) throws MalformedURLException {
        return new URL(String.format("jar:%s!/%s", jarFile, resourceLoc));
    }

    public static byte[] readStream(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int val;
        while ((val = is.read()) != -1) {
            bos.write(val);
        }
        is.close();
        return bos.toByteArray();
    }
}
