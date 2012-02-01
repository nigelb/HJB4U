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

package hjb4u.launch;

import hjb4u.config.HJB4UConfiguration;
import hjb4u.gui.GUI;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.bind.JAXBException;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;


public class Launch extends AbstractLaunch{

    public static void main(String[] args) throws JAXBException, IOException, SAXException {

        HJB4UConfiguration settings = initializeHAJJ4U(true);
        final GUI g;
        JFrame w = new JFrame(settings.getSchema());
        w.getContentPane().add((g = new GUI(w)).$$$getRootComponent$$$());
        w.setBounds(100, 100, 300, 300);
        w.setLocationRelativeTo(null);
        w.setVisible(true);
        w.addWindowListener(new WindowListener() {
            public void windowOpened(WindowEvent e) {
            }

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }

            public void windowClosed(WindowEvent e) {
            }

            public void windowIconified(WindowEvent e) {
            }

            public void windowDeiconified(WindowEvent e) {
            }

            public void windowActivated(WindowEvent e) {
            }

            public void windowDeactivated(WindowEvent e) {
            }
        });
    }


}
