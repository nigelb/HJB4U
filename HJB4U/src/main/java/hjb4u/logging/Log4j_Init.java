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
package hjb4u.logging;


import org.apache.log4j.xml.DOMConfigurator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;

/**
 * <code>Log4j_Init</code> initilizes the log4j logging system.
 * <p/>
 * Date: Feb 16, 2005
 * Time: 9:04:17 AM
 *
 * @author Nigel Blair
 */
public class Log4j_Init {
  private static final String defaultConf = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
      // "<!DOCTYPE log4j:configuration SYSTEM \"./conf/log4j.dtd\">\n" +
      "\n" +
      // "<log4j:configuration xmlns:log4j='http://jakarta.apache.org/log4j/'>\n" +
      "<log4j:configuration>\n" +
      "\n" +
      "\t<appender name=\"STDOUT\" class=\"org.apache.log4j.ConsoleAppender\">\n" +
      "           <layout class=\"org.apache.log4j.PatternLayout\">\n" +
      "             <param name=\"ConversionPattern\"\n" +
      "\t\t    value=\"[%t:%-6p] at %C.%M(%F:%L) - %m%n\"/>\n" +
      "           </layout>\n" +
      "\t</appender>\n" +
      "\n" +
      "\t<root>\n" +
      "\t   <priority value =\"debug\" />\n" +
      "   \t   <appender-ref ref=\"STDOUT\" />\n" +
      "\t</root>\n" +
      "\n" +
      "</log4j:configuration>";
  private static DOMConfigurator logConf = null;
  private static final File LOG4J_CONFIG_FILE = new File("./conf/log4j.xml");
  private InputStream conf_file;


  /**
   * <code>Log4j_Init</code> calls the constructor this(null). Just uses
   * the default configuration.
   */
  public Log4j_Init() {
    this(null);
  }

  /**
   * <code>Log4j_Init</code> sets the location of the configuration file to
   * initilize the log4j logging system with.
   *
   * @param LOG4J_CONFIG_FILE the XML configuration file to used to configure
   *                          the log4j logging system, if null then
   *                          ./config/log4j.xml is tried, if that does not exist
   *                          then the default configuration is used.
   */

  public Log4j_Init(InputStream LOG4J_CONFIG_FILE) {
    conf_file = LOG4J_CONFIG_FILE;
  }

  /**
   * <code>Log4j_Init</code> initilizes the log4j logging system with the supplied
   * configuration file.
   */
  public void logConf() {
    if (logConf == null) {
      System.setProperty("log4j.ignoreTCL", "true");
      logConf = new DOMConfigurator();
      if (conf_file == null) {
        if (LOG4J_CONFIG_FILE.exists()) {
          try {
            DOMConfigurator.configure(getConfElement(new FileInputStream(LOG4J_CONFIG_FILE)));
          } catch (Exception e) {
            e.printStackTrace();
          }
        } else {
          Element e = getConfElement(new ByteArrayInputStream(defaultConf.getBytes()));
          DOMConfigurator.configure(e);
        }
      } else {
        DOMConfigurator.configure(getConfElement(conf_file));
          try {
              conf_file.close();
          } catch (IOException e) {
              e.printStackTrace();  
          }
      }
    }
  }

  /**
   * <code>getDefaultConfElement</code> constructs a DOM of the default
   * configuration.
   *
   * @param in - The inputstream to read the configuration from.
   * @return The root element of the Default DOM to be used to configure the
   *         Log4j system.
   */
  private Element getConfElement(InputStream in) {
    Document doc;
    try {
      doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
          .parse(in);
      return doc.getDocumentElement();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
