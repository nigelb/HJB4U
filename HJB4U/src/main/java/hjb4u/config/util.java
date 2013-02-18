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

package hjb4u.config;

import hjb4u.config.hjb4u.DBConf;
import hjb4u.config.hjb4u.DBList;
import hjb4u.config.hjb4u.HJB4UConfiguration;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * <code>util</code>
 * Date: 07/07/2009
 * Time: 5:09:42 PM
 *
 * @author Nigel B
 */
public class util {
    public final static void first() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(DBList.class);
        Marshaller mar = context.createMarshaller();
        DBList list = new DBList();
        ArrayList<DBConf> dbs = new ArrayList<DBConf>();
        DBConf db = new DBConf("Mysql",
                "org.hibernate.dialect.MySQLDialect",
                "com.mysql.jdbc.Driver",
                "username",
                "password",
                "jdbc:mysql://<host>/<database name>");

        dbs.add(db);
        db = new DBConf("Mysql5InnoDB - Bits are converted to Integers",
                "hjb4u.database.NoBitsMySQL5InnoDBDialect",
                "com.mysql.jdbc.Driver",
                "username",
                "password",
                "jdbc:mysql://<host>/<database name>");
        dbs.add(db);
        list.setDbs(dbs);
        mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        mar.marshal(list, System.out);
    }

    public static void second() throws JAXBException {
        HJB4UConfiguration sc = new HJB4UConfiguration();

        JAXBContext context = JAXBContext.newInstance(HJB4UConfiguration.class);
        Marshaller mar = context.createMarshaller();
        mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        mar.marshal(sc, System.out);
    }

    public static void main(String[] args) throws JAXBException, IOException {
        first();
        second();
        Enumeration<URL> e = util.class.getClassLoader().getResources("META-INF/sun-jaxb.episode");
        while (e.hasMoreElements()) {
            URL url = e.nextElement();
            System.out.println(url);
        }



    }
}
