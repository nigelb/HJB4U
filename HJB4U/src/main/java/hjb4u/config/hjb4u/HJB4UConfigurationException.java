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

/**
 * <code>HJB4UConfigurationException</code>
 * Date: Aug 23, 2009
 * Time: 1:04:11 PM
 *
 * @author Nigel B
 */
public class HJB4UConfigurationException extends Exception {
    public HJB4UConfigurationException() {
        super();
    }

    public HJB4UConfigurationException(String message) {
        super(message);
    }

    public HJB4UConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public HJB4UConfigurationException(Throwable cause) {
        super(cause);
    }
}
