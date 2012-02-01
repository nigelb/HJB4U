package hjb4u.launch;

import hjb4u.Main;
import hjb4u.config.HJB4UConfiguration;
import hjb4u.config.HJB4UConfigurationException;
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
 * @Author Nigel Bajema
 */
public class CLCreateXML extends AbstractLaunch{
    public static void main(String[] args) throws JAXBException, IOException, SAXException, TransformerException, ClassNotFoundException, HJB4UConfigurationException, ParserConfigurationException {
        HJB4UConfiguration settings = initializeHAJJ4U(true);
        settings.setRootID(Long.parseLong(args[0]));
        Main main = new Main();
        main.createXML(new FileOutputStream(new File(args[1])));
    }
}
