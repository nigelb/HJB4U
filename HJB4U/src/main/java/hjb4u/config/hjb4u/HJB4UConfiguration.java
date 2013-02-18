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

package hjb4u.config.hjb4u;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * <code>HJB4UConfiguration</code>
 * Date: 07/07/2009
 * Time: 4:32:47 PM
 *
 * @author Nigel B
 */
@XmlRootElement(name = "Configuration")
public class HJB4UConfiguration {

    private String schema = null;
    private boolean validate = false;
    private String database;
    private List<DBConf> dbs = new ArrayList<DBConf>();
    private List<NameSpaceMapping> namespaces = new ArrayList<NameSpaceMapping>();
    private long rootID = 1;
    private String rootElementType;
    private String saveDir;
    private String ingestDir;
    private boolean enableLF5 = false;
    private int lf5Size = 5000;
    private boolean enableLoggingPane = true;
    private int paneSize = 5000;
    private ELogLevel lf5LogLevel = ELogLevel.DEBUG;
    private ELogLevel paneLogLevel = ELogLevel.DEBUG;
    private String panePattern = "[%t:%-6p] at %C.%M(%F:%L) - %m%n";
    private String patternURL = "http://logging.apache.org/log4j/1.2/apidocs/org/apache/log4j/PatternLayout.html";


    public HJB4UConfiguration() {
    }

    public long getRootID() {
        return rootID;
    }

    public String getRootElementType() {
        return rootElementType;
    }

    public String getSaveDir() {
        return saveDir;
    }

    @XmlAttribute
    public void setSaveDir(String saveDir) {
        this.saveDir = saveDir;
    }

    public String getIngestDir() {
        return ingestDir;
    }

    @XmlAttribute
    public void setIngestDir(String ingestDir) {
        this.ingestDir = ingestDir;
    }

    @XmlAttribute
    public void setRootElementType(String rootElementType) {
        this.rootElementType = rootElementType;
    }

    @XmlAttribute
    public void setRootID(long rootID) {
        this.rootID = rootID;
    }

    @XmlAttribute
    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    @XmlAttribute
    public boolean isValidate() {
        return validate;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    @XmlAttribute(name = "database", required = true)
    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    @XmlElement(name = "Databases", required = true)
    public List<DBConf> getDbs() {
        return dbs;
    }

    public void setDbs(List<DBConf> dbs) {
        this.dbs = dbs;
    }

    @XmlElement(name = "Namespaces", required = true)
    public List<NameSpaceMapping> getNamespaces() {
        return namespaces;
    }

    public void setNamespaces(List<NameSpaceMapping> namespaces) {
        this.namespaces = namespaces;
    }

    @XmlAttribute
    public void setEnableLF5(boolean enableLF5) {
        this.enableLF5 = enableLF5;
    }

    @XmlAttribute
    public void setLf5Size(int lf5Size) {
        this.lf5Size = lf5Size;
    }

    @XmlAttribute
    public void setEnableLoggingPane(boolean enableLoggingPane) {
        this.enableLoggingPane = enableLoggingPane;
    }

    @XmlAttribute
    public void setPaneSize(int paneSize) {
        this.paneSize = paneSize;
    }

    @XmlAttribute
    public void setLf5LogLevel(ELogLevel lf5LogLevel) {
        this.lf5LogLevel = lf5LogLevel;
    }

    @XmlAttribute
    public void setPaneLogLevel(ELogLevel paneLogLevel) {
        this.paneLogLevel = paneLogLevel;
    }

    @XmlAttribute
    public void setPanePattern(String panePattern) {
        this.panePattern = panePattern;
    }

    @XmlAttribute
    public void setPatternURL(String patternURL) {
        this.patternURL = patternURL;
    }

    public boolean isEnableLF5() {
        return enableLF5;
    }

    public int getLf5Size() {
        return lf5Size;
    }

    public boolean isEnableLoggingPane() {
        return enableLoggingPane;
    }

    public int getPaneSize() {
        return paneSize;
    }

    public ELogLevel getLf5LogLevel() {
        return lf5LogLevel;
    }

    public ELogLevel getPaneLogLevel() {
        return paneLogLevel;
    }

    public String getPanePattern() {
        return panePattern;
    }

    public String getPatternURL() {
        return patternURL;
    }
}
