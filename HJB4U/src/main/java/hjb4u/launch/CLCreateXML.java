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
package hjb4u.launch;

import hjb4u.Main;
import hjb4u.config.hjb4u.HJB4UConfiguration;
import hjb4u.config.hjb4u.HJB4UConfigurationException;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Date: 2/1/12
 * Time: 11:41 AM
 *
 * @Author NigelB
 */
public class CLCreateXML extends AbstractLaunch{
    public static void main(String[] args) throws JAXBException, IOException, SAXException, TransformerException, ClassNotFoundException, HJB4UConfigurationException, ParserConfigurationException {
        HJB4UConfiguration settings = initializeHAJJ4U(true);
        settings.setRootID(Long.parseLong(args[0]));
        Main main = new Main();
        main.createXML(new FileOutputStream(new File(args[1])));
    }
}
