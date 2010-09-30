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

import hjb4u.exceptions.NamespaceException;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <code>NameSpaceMapping</code>
 * Date: 07/07/2009
 * Time: 5:03:06 PM
 *
 * @author Nigel B
 */
@XmlRootElement(name = "NameSpace")
public class NameSpaceMapping {
	private String namespace;
	private String prefix = null;
	private boolean _default = false;

	public static boolean __ignore = false;
	private static boolean __default = false;

	public NameSpaceMapping() {
	}

	public NameSpaceMapping(String namespace, String prefix) {
		this.namespace = namespace;
		this.prefix = prefix;
	}

	public NameSpaceMapping(String namespace) {
		this.namespace = namespace;
		setDefault(true);

	}

	@XmlAttribute
	public String getNamespace() {
		return namespace;
	}

	@XmlAttribute(name = "default")
	public boolean isDefault() {
		return _default;
	}

	public void setDefault(boolean _default) {
	   if((this._default && !_default && ! __default)
			   || (!this._default && _default &&  __default))
	   {
		       if(!__ignore){throw new NamespaceException();}
	   }
		this._default = _default;
		__default = _default;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	@XmlAttribute
	public String getPrefix() {
		return prefix;
	}


	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public static boolean defaultExists() {
		return __default;
	}

	@Override
	public String toString() {
		return new StringBuilder().append(prefix == null ? "[default]" : prefix).append("::").append(namespace).toString();
	}

}
