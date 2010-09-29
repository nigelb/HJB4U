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

import javax.swing.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;

/**
 * <code>HashListModel</code>
 * Date: 07/07/2009
 * Time: 10:37:24 PM
 *
 * @author Nigel B
 */
public class HashListModel<T> extends AbstractListModel {
    private Hashtable<String, T> model = new Hashtable<String, T>();
    private String[] keys = new String[]{};
    private boolean dirty = true;
    private Object lock = new Object();
    private Comparator cmp;


    public int getSize() {
        synchronized (lock) {
            return model.size();
        }

    }

    public Object getElementAt(int index) {

        synchronized (lock) {
            checkKeys();
            return model.get(keys[index]);
        }
    }

    private void checkKeys() {
        synchronized (lock) {
            if (dirty) {
                keys = new String[getSize()];
                model.keySet().toArray(keys);
                if (cmp == null) {
                    Arrays.sort(keys);
                } else {
                    Arrays.sort(keys, cmp);
                }
                dirty = false;
            }
        }
    }

    public void insert(String key, T object) {
        synchronized (lock) {
            dirty = true;
            model.put(key, object);
        }
        fireContentsChanged(this, 0, getSize());
    }

    public void remove(String key) {
        synchronized (lock) {
            model.remove(key);
            dirty = true;
            checkKeys();
        }
        fireContentsChanged(this, 0, getSize());
    }

    public Hashtable<String, T> getModel() {
        return model;
    }

    public Comparator getCmp() {
        return cmp;
    }

    public void setCmp(Comparator cmp) {
        this.cmp = cmp;
    }
}
