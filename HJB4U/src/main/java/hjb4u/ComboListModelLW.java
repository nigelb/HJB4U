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
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * <code>ComboListModelLW</code>
 * Date: 08/07/2009
 * Time: 2:20:02 AM
 *
 * @author Nigel Bajema
 */
public class ComboListModelLW extends AbstractListModel implements ComboBoxModel{
    private ListModel model;
    private Object selected;

    public ComboListModelLW(ListModel model) {
        this.model = model;
        model.addListDataListener(new ListDataListener() {
            public void intervalAdded(ListDataEvent e) {
                fireIntervalAdded(e.getSource(), e.getIndex0(), e.getIndex1());
            }

            public void intervalRemoved(ListDataEvent e) {
                fireIntervalRemoved(e.getSource(), e.getIndex0(), e.getIndex1());
            }

            public void contentsChanged(ListDataEvent e) {
                fireIntervalRemoved(e.getSource(), e.getIndex0(), e.getIndex1());
            }
        });
    }

    public void setSelectedItem(Object anItem) {
        selected = anItem;
    }

    public Object getSelectedItem() {
        return selected;
    }

    public int getSize() {
        return model.getSize();
    }

    public Object getElementAt(int index) {
        return model.getElementAt(index);
    }

}
