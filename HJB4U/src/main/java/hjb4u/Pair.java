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

/**
 * <code>Pair</code>
 * Date: 15/06/2010
 * Time: 7:36:30 AM
 *
 * @author Nigel B
 */
public class Pair<ITEM1, ITEM2> {
    private ITEM1 item1;
    private ITEM2 item2;

    public Pair(ITEM1 item1, ITEM2 item2) {
        this.item1 = item1;
        this.item2 = item2;
    }

    public ITEM1 getItem1() {
        return item1;
    }

    public void setItem1(ITEM1 item1) {
        this.item1 = item1;
    }

    public ITEM2 getItem2() {
        return item2;
    }

    public void setItem2(ITEM2 item2) {
        this.item2 = item2;
    }
}
