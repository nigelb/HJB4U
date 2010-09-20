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
import java.net.URL;

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
        System.out.println("Copring resource: "+from+" to: "+to);
        try {
            OutputStream fos = new BufferedOutputStream(new FileOutputStream(to));
            InputStream in = new BufferedInputStream(from.openStream());
            int data = -1;
            while(true)
            {
                data = in.read();
                if(data == -1) break;
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
}
