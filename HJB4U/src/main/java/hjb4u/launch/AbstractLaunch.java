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
import hjb4u.Pair;
import hjb4u.SettingsStore;
import hjb4u.config.hjb4u.DBList;
import hjb4u.config.hjb4u.HJB4UConfiguration;
import hjb4u.config.resources.Resource;
import hjb4u.config.resources.Resources;
import hjb4u.logging.Log4j_Init;
import hjb4u.logging.MemoryAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.lf5.LF5Appender;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Properties;

import static hjb4u.Util.copyResource;
import static hjb4u.Util.joinPath;
import static hjb4u.config.hjb4u.Constants.*;
import static hjb4u.config.hjb4u.Constants.PaneAppenderName;

/**
 * Date: 2/1/12
 * Time: 11:42 AM
 *
 * @Author NigelB
 */
public abstract class AbstractLaunch {
    private static String conf_dir;
    private static String xslt_dir;
    protected static Properties _settings = new Properties();

    protected static HJB4UConfiguration initializeHAJJ4U(boolean gui) throws IOException, JAXBException {
        ArrayList<Pair<Level, String>> preLoggingMessages = new ArrayList<Pair<Level, String>>();

        //Load the schema.properties file.
        String pgkPath = Main.class.getPackage().getName().replace('.', '/');
        String conf_resource_path = "META-INF/"+pgkPath + "/conf";
        String xslt_resource_path = pgkPath + "/xslt";
        String loc = conf_resource_path + "/settings.properties";
        ClassLoader cl = Launch.class.getClassLoader();
        _settings.load((cl.getResource(loc).openStream()));
        if (_settings.getProperty(PROJECT_NAME) == null) {
            System.out.println("Could not get Project Name. Exiting.");
            System.exit(1);
        }
        conf_dir = System.getProperty("user.home") + File.separator + HJB4U_PATH + File.separator + _settings.getProperty(PROJECT_NAME);
        xslt_dir = joinPath(conf_dir, XSLT_DIR_PATH);

        //Copy resources from classpath (generally inside the JAR) to the
        //local storage area. ReThrows Null pointers if the file is required.
        copyResources(cl.getResource("META-INF/hjb4u/conf/_resources.xml"), preLoggingMessages);
        copyResources(cl.getResource("META-INF/hjb4u/conf/resources.xml"), preLoggingMessages);

        //Initialise the logging.
        new Log4j_Init(new FileInputStream(joinPath(conf_dir, "log4j.xml"))).logConf();
        Logger logger = Logger.getLogger(Launch.class);

        //Log all messages that were qued up before logging was available.
        for (Pair<Level, String> preLoggingMessage : preLoggingMessages) {
            logger.log(preLoggingMessage.getItem1(), preLoggingMessage.getItem2());
        }
        //Instantiate the settings store.
        SettingsStore.instanciate(conf_dir, "settings.xml");
        HJB4UConfiguration settings = SettingsStore.getInstance().getSettings();
        if (settings.getSchema() == null) {
            settings.setSchema(_settings.getProperty(SCHEMA_FILE));
        }
        if (gui) {
            //More initialization for logging.
            if (settings.isEnableLF5()) {
                LF5Appender appender = new LF5Appender();
                appender.setMaxNumberOfRecords(settings.getLf5Size());
                appender.setThreshold(settings.getLf5LogLevel().getLevel());
                appender.setName(LF5AppenderName);
                Logger.getRootLogger().addAppender(appender);
            }

            if (settings.isEnableLoggingPane()) {
                PatternLayout layout = new PatternLayout(settings.getPanePattern());
                MemoryAppender appender = new MemoryAppender(layout, settings.getPaneSize());
                appender.setThreshold(settings.getPaneLogLevel().getLevel());
                appender.setName(PaneAppenderName);
                Logger.getRootLogger().addAppender(appender);
            }
        }
        logger.info("Logging Initialized.");

        //Get the Database Templates.
        try {
            Unmarshaller umas = JAXBContext.newInstance(DBList.class).createUnmarshaller();
            DBList templates = (DBList) umas.unmarshal(cl.getResourceAsStream(conf_resource_path + "/databases.xml"));
            SettingsStore.getInstance().setDatabaseTemplates(templates);
        } catch (Exception e) {
            logger.warn("Could not load database templates: " + e, e);
        }

        return settings;
    }

    protected static void copyResources(URL resource, ArrayList<Pair<Level, String>> preLoggingMessages) throws JAXBException {
        ClassLoader cl = Launch.class.getClassLoader();
        Unmarshaller um = JAXBContext.newInstance(Resources.class).createUnmarshaller();
        Resources r = (Resources) um.unmarshal(resource);
        MessageFormat mf;
        File out;
        for (Resource res : r.getResources()) {
            mf = new MessageFormat(res.getOutputLocation());
            out = new File(mf.format(new Object[]{conf_dir}));
            if (!out.exists()) {
                out.getParentFile().mkdirs();
                try {
                    copyResource(cl.getResource(res.getResourceLocation()), out);
                } catch (NullPointerException npe) {
                    if (res.isErrorOnFail()) {
                        throw npe;
                    } else {
                        preLoggingMessages.add(new Pair<Level, String>(Level.INFO, String.format("%s not included in classpath, ignoring.", res.getResourceLocation())));
                    }
                }
            }
        }
    }
}
