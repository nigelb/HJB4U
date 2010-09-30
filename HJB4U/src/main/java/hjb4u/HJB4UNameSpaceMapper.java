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

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

import java.util.Hashtable;
import java.util.Set;


public class HJB4UNameSpaceMapper extends NamespacePrefixMapper {
    private Hashtable<String, String> namespace;

	public static final String DEFAULT = "__DEFAULT__";

    public HJB4UNameSpaceMapper(Hashtable<String, String> namespace) {
        this.namespace = namespace;
    }

    public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
        String toRet = namespace.get(namespaceUri);
		if(toRet.equals(DEFAULT)){return null;}
        if(toRet == null) {return suggestion;}
        return toRet;
    }

    @Override
    public String[] getPreDeclaredNamespaceUris() {
        Set<String> keys = namespace.keySet();
        String[] toRet = new String[keys.size()];
        keys.toArray(toRet);
        return toRet;
    }
}
