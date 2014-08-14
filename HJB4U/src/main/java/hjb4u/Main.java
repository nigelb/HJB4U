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

import hjb4u.config.hjb4u.DBConf;
import hjb4u.config.hjb4u.HJB4UConfiguration;
import hjb4u.config.hjb4u.HJB4UConfigurationException;
import com.sun.xml.bind.marshaller.NamespacePrefixMapper;
import hjb4u.roundtrip.RoundTripInterface;
import hjb4u.roundtrip.RoundTripProxy;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.xml.XMLConstants;
import javax.xml.bind.*;

import static javax.xml.bind.JAXBContext.newInstance;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Properties;

/**
 * <code>Main</code>
 * Date: May 20, 2009
 * Time: 10:40:27 PM
 *
 * @author Nigel B
 */
public class Main {

    private JAXBContext jaxbContext;
    private Schema schema;
    private Marshaller vmarshaller;
    private Marshaller marshaller;
    private Unmarshaller unmarshaller;
    private EntityManagerFactory fac;
    private RoundTripInterface roundTrip = RoundTripProxy.createRoundTrip();

    private HJB4UConfiguration settings = SettingsStore.getInstance().getSettings();
    private Logger logger = Logger.getLogger(Main.class.getName());

    public Main() throws SAXException, IOException, JAXBException {

		//Initialise JAXB.
        jaxbContext = newInstance(roundTrip.getContextPath());
        marshaller = jaxbContext.createMarshaller();
        vmarshaller = jaxbContext.createMarshaller();
        unmarshaller = jaxbContext.createUnmarshaller();
        schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
                .newSchema(this.getClass().getClassLoader().getResource(settings.getSchema()));

        vmarshaller.setSchema(schema);
        vmarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        vmarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

        NamespacePrefixMapper prefix = SettingsStore.getInstance().getNamespaceMapper();
        marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", prefix);
        vmarshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", prefix);

        unmarshaller.setSchema(schema);

    }

    private Properties getPersistance() throws HJB4UConfigurationException {
        Properties p = new Properties();
        try {
            p = new Properties();
            InputStream closer;
            p.load(closer = new FileInputStream(
                    Util.joinPath(SettingsStore.getInstance().getConfDir(), "persistence.properties")));
            closer.close();
        } catch (IOException e) {
            logger.error("Failed to load persistance settings." + e, e);
        }
        boolean configError = true;
        String id = settings.getDatabase();
        for (DBConf dbConf : settings.getDbs()) {
            if (dbConf.getUuid().equals(id)) {
                configError = false;
                logger.info("Using DB: " + dbConf);
                p.setProperty("hibernate.dialect", dbConf.getDialect());
                p.setProperty("hibernate.connection.driver_class", dbConf.getDriver());
                if (dbConf.getUsername() != null) {
                    p.setProperty("hibernate.connection.username", dbConf.getUsername());
                }
                if (dbConf.getPassword() != null) {
                    p.setProperty("hibernate.connection.password", dbConf.getPassword());
                }
                p.setProperty("hibernate.connection.url", dbConf.getUrl());
                break;
            }
        }

        if (configError) {
            if (id == null) {
                throw new HJB4UConfigurationException("You haven't select a database configuration to use. It can be set the the Settings Dialog.");
            } else {
                throw new HJB4UConfigurationException("The database configuration selected is no longer available. Please select another in the Settings Dialog");
            }

        }
        return p;
    }

    public void populateDatabase() throws HJB4UConfigurationException {
        Properties p = getPersistance();
        p.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        fac = Persistence.createEntityManagerFactory(roundTrip.getPersistenceUnitName(), p);
        createEM().close();
    }

    EntityManager createEM() throws HJB4UConfigurationException {
        Properties p = getPersistance();
        fac = Persistence.createEntityManagerFactory(roundTrip.getPersistenceUnitName(), p);
        return fac.createEntityManager(p);
    }

    public void createXML(OutputStream out) throws TransformerException, JAXBException, ParserConfigurationException, IOException, ClassNotFoundException, HJB4UConfigurationException {
        String data = getData();
        out.write(data.getBytes());
        out.close();
        if (settings.isValidate()) {
            test(data);
        }
    }

    /**
     * @param in - Input stream that provides the xml to be unmarsheled.
     * @throws JAXBException
     */
    public void insertData(InputStream in) throws JAXBException, HJB4UConfigurationException {
        EntityManager emanager = createEM();
        try {
            EntityTransaction t = emanager.getTransaction();
            t.begin();
            if (!settings.isValidate()) {
                unmarshaller.setSchema(null);
            }
            Object o = unmarshaller.unmarshal(in);
            if(o instanceof JAXBElement)
            {
                o = ((JAXBElement)o).getValue();
            }
            emanager.persist(o);
            t.commit();
        } finally {
            doClose(emanager);
        }

    }

    public void doClose(EntityManager emanager) {
        if (emanager != null) {
            emanager.close();
            emanager = null;
        }
    }

    private String getData() throws JAXBException, ParserConfigurationException, TransformerException, ClassNotFoundException, HJB4UConfigurationException {
        EntityManager emanager = createEM();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document doc = builder.newDocument();
            Element n;

            Object loadedObject = emanager.find(Class.forName(settings.getRootElementType()), settings.getRootID());
            marshaller.marshal(loadedObject, doc);
//            NodeList nl = n.getChildNodes();
//
//            for (int i = 0; i < nl.getLength(); i++) {
//                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
////                    removeHJID(nl.item(i), doc);
//                    doc.appendChild(nl.item(i));
//                }
//            }
            return getDomString(postProcess(doc, builder));
        } finally {
            doClose(emanager);
        }

    }

    private Document postProcess(Document doc, DocumentBuilder builder) throws TransformerException {
        DOMSource domSource = new DOMSource(doc);
        TransformerFactory tf = TransformerFactory.newInstance();
        Document result = doc;
        File[] transforms = new File(SettingsStore.getInstance().getXSLTDir()).listFiles();
        Arrays.sort(transforms, new Comparator<File>() {
            public int compare(File o1, File o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        for (File file : transforms) {
            logger.info(String.format("Transforming output with %s", file));
            Transformer transformer = tf.newTransformer(new StreamSource(file));
            result = builder.newDocument();
            transformer.transform(domSource, new DOMResult(result));
            domSource = new DOMSource(result);
        }
        return result;
    }


    private void removeHJID(Node n, Document doc) {
        Node cn;
        NodeList nl = n.getChildNodes();
        Element e;
        int length = nl.getLength();
        for (int i = 0; i < length; i++) {
            cn = nl.item(i);

            if (cn.getNodeType() == Node.ELEMENT_NODE) {
                removeHJID(cn, doc);
            }
        }
        if (n.getNodeType() == Node.ELEMENT_NODE) {
            e = (Element) n;
            e.removeAttribute("Hjid");
        }

    }

    private String getDomString(Document doc) throws TransformerException {
        DOMSource domSource = new DOMSource(doc);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
//		//transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");

        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        StreamResult sr = new StreamResult(bos);
        transformer.transform(domSource, sr);
        return bos.toString();

    }

    private void test(String data) throws JAXBException {
        try {
            DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = parser.parse(new File("myXMLDocument.xml"));
            Validator validator = schema.newValidator();
            validator.setErrorHandler(new ErrorHandler() {
                public void warning(SAXParseException exception) throws SAXException {
                    logger.warn(exception, exception);
                }

                public void error(SAXParseException exception) throws SAXException {
                    logger.error(exception, exception);
                }

                public void fatalError(SAXParseException exception) throws SAXException {
                    logger.fatal(exception, exception);
                }
            });
            validator.validate(new DOMSource(document));

        } catch (IOException e) {
            logger.error(e, e);
        } catch (SAXException e) {
            logger.error(e, e);
        } catch (ParserConfigurationException e) {
            logger.error(e, e);
        }
    }

}
