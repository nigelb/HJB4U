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

package hjb4u.config.resources;


import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * <code>Resource</code>
 * Date: 2/18/13
 * Time: 12:11 PM
 *
 * @author Nigel B
 */
public class Resource {
    private String resourceLocation;
    private String outputLocation;
    private boolean errorOnFail = true;


    public String getResourceLocation() {
        return resourceLocation;
    }

    @XmlElement
    public void setResourceLocation(String resourceLocation) {
        this.resourceLocation = resourceLocation;
    }

    public String getOutputLocation() {
        return outputLocation;
    }

    @XmlElement
    public void setOutputLocation(String outputLocation) {
        this.outputLocation = outputLocation;
    }

    public boolean isErrorOnFail() {
        return errorOnFail;
    }

    @XmlAttribute
    public void setErrorOnFail(boolean errorOnFail) {
        this.errorOnFail = errorOnFail;
    }
}
