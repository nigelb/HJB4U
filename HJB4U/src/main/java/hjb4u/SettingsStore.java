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

import hjb4u.config.DBList;
import hjb4u.config.HJB4UConfiguration;
import hjb4u.config.NameSpaceMapping;
import com.sun.xml.bind.marshaller.NamespacePrefixMapper;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * <code>SettingsStore</code>
 * Date: 07/07/2009
 * Time: 6:15:36 PM
 *
 * @author Nigel Bajema
 */
public class SettingsStore {
    private HJB4UConfiguration settings;
    private static SettingsStore instance;
    private String conf_dir;
    private File store;
    private JAXBContext context = JAXBContext.newInstance(HJB4UConfiguration.class);
    private Unmarshaller unmar = context.createUnmarshaller();
    private Marshaller mar = context.createMarshaller();
    private Logger logger = Logger.getLogger(SettingsStore.class);
    private DBList templates;
    private final String episode = "META-INF/sun-jaxb.episode";

    public static void instanciate(String conf_dir, String settings) throws JAXBException {
        instance = new SettingsStore(conf_dir, new File(conf_dir + File.separator + settings));
    }

    private SettingsStore(String conf_dir, File store) throws JAXBException {
        this.conf_dir = conf_dir;
        this.store = store;


        try {
            mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            settings = (HJB4UConfiguration) unmar.unmarshal(store);
        } catch (JAXBException e) {

            if (e.getLinkedException() instanceof FileNotFoundException) {
                logger.debug("Settings Not Found.");
                settings = new HJB4UConfiguration();
                settings.setNamespaces(makeDefaultNamespaces());
            } else {
                throw e;
            }
        }

    }

    public List<NameSpaceMapping> makeDefaultNamespaces() {
        ArrayList<NameSpaceMapping> toRet = new ArrayList<NameSpaceMapping>();
        try {
            URL nsmap = this.getClass().getClassLoader().getResource(episode);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(nsmap.openStream());
            Element root = doc.getDocumentElement();
            NodeList nl = root.getChildNodes();
            Node n;
            Element e;
            int nscount = 1;
            NameSpaceMapping nsm;
            for (int i = nl.getLength(); i >= 0; i--) {
                n = nl.item(i);
                if (n instanceof Element) {
                    e = (Element) n;
                    toRet.add(nsm = new NameSpaceMapping(e.getAttribute("xmlns:tns"), "ns" + nscount++));
                    logger.debug(new StringBuilder().append(nsm.getPrefix()).append(": ").append(nsm.getNamespace()).toString());
                }
            }
        } catch (Exception e) {
            logger.debug(e, e);
            return toRet;
        }
        return toRet;
    }

    public List<ListClass> makeRootElementList() {
        ArrayList<ListClass> toRet = new ArrayList<ListClass>();
        try {
            Class[] cls = ClassFinder.getEpisodeClasses(this.getClass().getClassLoader().getResource(episode));
            for (Class cl : cls) {
                toRet.add(new ListClass(cl));
            }
        } catch (IOException e) {
            logger.error(e, e);
        }
        return toRet;
    }

    public Class[] getAllJAXBClasses() {
        Class[] cls = new Class[]{};
        try {
            cls = ClassFinder.findAllJAXBClasses(this.getClass().getClassLoader().getResource(episode));
        } catch (IOException e) {
            logger.error(e, e);
        }
        return cls;
    }

    public void save() throws JAXBException, FileNotFoundException {
        OutputStream os;
        mar.marshal(settings, os = new BufferedOutputStream(new FileOutputStream(store)));
        try {
            os.flush();
            os.close();
        } catch (IOException e) {
            logger.error(e, e);
        }

    }

    public static SettingsStore getInstance() {
        return instance;
    }

    public HJB4UConfiguration getSettings() {
        return settings;
    }

    public File getStoreLocation() {
        return store;
    }

    public String getStorePath() {
        try {
            return store.getAbsoluteFile().getCanonicalPath();
        } catch (IOException e) {
            logger.error(e, e);
        }
        return store.getPath();
    }

    public String getConfDir() {
        return conf_dir;
    }

    public NamespacePrefixMapper getNamespaceMapper() {
        Hashtable<String, String> map = new Hashtable<String, String>();
        for (int i = 0; i < settings.getNamespaces().size(); i++) {
            NameSpaceMapping ns = settings.getNamespaces().get(i);
            map.put(ns.getNamespace(), ns.getPrefix());
        }

        return new HJB4UNameSpaceMapper(map);
    }


    public void setDatabaseTemplates(DBList templates) {
        this.templates = templates;
    }

    public DBList getTemplates() {
        return templates;
    }
}
