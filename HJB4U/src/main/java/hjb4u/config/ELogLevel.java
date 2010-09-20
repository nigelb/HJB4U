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

import org.apache.log4j.Level;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

/**
 * <code>ELogLevel</code>
 * Date: Aug 20, 2009
 * Time: 6:33:10 PM
 *
 * @author Nigel Bajema
 */

@XmlEnum
public enum ELogLevel {
    @XmlEnumValue("" + Level.ALL_INT)
    ALL(Level.ALL, "All"),
    @XmlEnumValue("" + Level.TRACE_INT)
    TRACE(Level.TRACE, "Trace"),
    @XmlEnumValue("" + Level.DEBUG_INT)
    DEBUG(Level.DEBUG, "Debug"),
    @XmlEnumValue("" + Level.INFO_INT)
    INFO(Level.INFO, "Info"),
    @XmlEnumValue("" + Level.WARN_INT)
    WARN(Level.WARN, "Warn"),
    @XmlEnumValue("" + Level.ERROR_INT)
    ERROR(Level.ERROR, "Error"),
    @XmlEnumValue("" + Level.FATAL_INT)
    FATAL(Level.FATAL, "Fatal"),
    @XmlEnumValue("" + Level.OFF_INT)
    OFF(Level.OFF, "Off");

    private Level level;
    private String name;

    ELogLevel(Level level, String name) {
        this.level = level;
        this.name = name;
    }

    public Level getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return name;
    }
}
