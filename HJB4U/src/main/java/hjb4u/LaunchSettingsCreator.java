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

import hjb4u.config.DBList;

import javax.swing.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;

/**
 * <code>LaunchSettingsCreator</code>
 * Date: 19/09/2010
 * Time: 12:48:27 PM
 *
 * @author Nigel Bajema
 */
public class LaunchSettingsCreator {

    public static void main(String[] args) {
		try{
        new LaunchSettingsCreator().run(args);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private void run(String[] args) throws JAXBException, FileNotFoundException {
		String pgkPath = Launch.class.getPackage().getName().replace('.', '/');
		String conf_path = pgkPath + "/conf";
		ClassLoader cl = MyRoundtripTest.class.getClassLoader();
		SettingsStore.instanciate(args[0], "settings.xml");
		Unmarshaller umas = JAXBContext.newInstance(DBList.class).createUnmarshaller();
		DBList templates = (DBList) umas.unmarshal(cl.getResourceAsStream(conf_path + "/databases.xml"));
		SettingsStore store = SettingsStore.getInstance();
		store.setDatabaseTemplates(templates);
		Settings.display(null, "Configuration : Settings.");
		SettingsStore.getInstance().save();
	}
}
