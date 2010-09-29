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

import javax.persistence.Table;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * <code>AnnotationPair</code>
 * Date: Jul 9, 2009
 * Time: 11:51:56 AM
 *
 * @author Nigel B
 */
public class AnnotationPair implements Comparable{
    private Table tb;
    private XmlRootElement xr;

    public AnnotationPair(Table tb, XmlRootElement xr) {
        this.tb = tb;
        this.xr = xr;
    }

    public Table getTb() {
        return tb;
    }

    public void setTb(Table tb) {
        this.tb = tb;
    }

    public XmlRootElement getXr() {
        return xr;
    }

    public void setXr(XmlRootElement xr) {
        this.xr = xr;
    }

    public int compareTo(Object o) {
        if(o instanceof AnnotationPair)
        {
            AnnotationPair d = (AnnotationPair) o;
            return xr.name().compareTo(d.xr.name());
        }
        else{
            return -1;
        }
    }
}
