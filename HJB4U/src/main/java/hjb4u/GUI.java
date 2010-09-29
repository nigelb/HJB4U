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

import static hjb4u.config.Constants.line_sep;

import hjb4u.Main;
import hjb4u.SettingsStore;
import hjb4u.config.HJB4UConfiguration;
import hjb4u.config.HJB4UConfigurationException;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.bind.JAXBException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

/**
 * <code>GUI</code>
 * Date: Jun 2, 2009
 * Time: 8:29:25 AM
 *
 * @author Nigel B
 */
public class GUI {
	private JButton createDatabaseButton;
	private JButton createXMLButton;
	private JButton changeSettingsButton;
	private JButton ingestXMLButton;
	private JPanel panel;
	private JButton makeMapButton;
	private Main main = new Main();
	private Logger logger = Logger.getLogger(GUI.class.getName());
	private JFrame parent;

	public GUI(final JFrame parent) throws JAXBException, IOException, SAXException {
		this.parent = parent;

		createDatabaseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int val = JOptionPane.showConfirmDialog(panel,
						"This will destroy existing data in the database. Do you want to Continue?",
						"Destroy Data?",
						JOptionPane.WARNING_MESSAGE,
						JOptionPane.YES_NO_OPTION);

				if (val == JOptionPane.YES_OPTION) {
					try {
						main.populateDatabase();
						JOptionPane.showMessageDialog(panel, "Database Created.");
					} catch (HJB4UConfigurationException e1) {
						logger.error(e1, e1);
						JOptionPane.showMessageDialog(panel, e1.getMessage(), "Configuration Error", JOptionPane.ERROR_MESSAGE);
					} catch (Throwable t) {
						logger.error("An unexpected Error occoured:", t);
						String message;
						if (t.getCause() != null) {
							message = t.getMessage() + ":    \n" + t.getCause().getMessage();
						} else {
							message = t.getMessage();
						}

						JOptionPane.showMessageDialog(panel, message, "Configuration Error", JOptionPane.ERROR_MESSAGE);
					}

				}

			}
		});
		createXMLButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int val = JOptionPane.NO_OPTION;
				try {
					File f = get_save_to_File(panel);
					if (f != null) {
						main.createXML(new FileOutputStream(f));
						JOptionPane.showMessageDialog(panel, "XML Created.");
					}
				}
				catch (HJB4UConfigurationException e1) {
					logger.error(e1, e1);
					JOptionPane.showMessageDialog(panel, e1.getMessage(), "Configuration Error", JOptionPane.ERROR_MESSAGE);
				}
				catch (Exception e1) {
					logger.error("There was an Error while creating the XML:", e1);
					val = JOptionPane.showConfirmDialog(panel, "There was an Error while creating the XML:"
							+ line_sep + line_sep + e1.getMessage() + line_sep + line_sep
							+ "Would you like to open the Log Viewer?", "Error",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.ERROR_MESSAGE);
				}

				if (val == JOptionPane.YES_OPTION) {
					JDialog errorPane = new JDialog(parent, "Error Pane");
					LogWindow lw = new LogWindow(errorPane);
					lw.update();
					errorPane.setContentPane(lw.$$$getRootComponent$$$());
					errorPane.setModal(true);
					errorPane.setSize(600, 400);
					errorPane.setLocationRelativeTo(null);
					errorPane.setVisible(true);
					errorPane.dispose();
				}

			}
		});
		changeSettingsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Settings.display(parent, SettingsStore.getInstance().getSettings().getSchema() + ": Settings.");
			}
		});

		ingestXMLButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File f = get_data_file(panel);
				if (f != null) {
					try {
						main.insertData(new FileInputStream(f));
						JOptionPane.showMessageDialog(panel, "XML Ingested.");
					}
					catch (HJB4UConfigurationException e1) {
						logger.error(e1, e1);
						JOptionPane.showMessageDialog(panel, e1.getMessage(), "Configuration Error", JOptionPane.ERROR_MESSAGE);
					}
					catch (Exception e1) {
						logger.error(e1, e1);
						logger.error(e1.getCause(), e1.getCause());
						logger.error(e1.getCause().getCause(), e1.getCause().getCause());
					}
				}
			}
		});
		makeMapButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JDialog mapping = new JDialog(parent, SettingsStore.getInstance().getSettings().getSchema() + ": XML -> Table Mapping.");
				mapping.setContentPane(new Mapping(mapping).$$$getRootComponent$$$());
				mapping.setModal(true);
				mapping.setSize(600, 400);
				mapping.setLocationRelativeTo(null);
				mapping.setVisible(true);
				mapping.dispose();
			}
		});
	}

	private File get_data_file(Component parent) {
		HJB4UConfiguration settings = SettingsStore.getInstance().getSettings();
		JFileChooser fc = new JFileChooser();
		if (settings.getIngestDir() != null) {
			fc.setCurrentDirectory(new File(settings.getIngestDir()));
		}
		if (fc.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
			File load = fc.getSelectedFile();
			settings.setIngestDir(load.getParent());
			try {
				SettingsStore.getInstance().save();
			} catch (JAXBException e) {
				logger.error("Error saving settings to store Ingest From dir.", e);
			} catch (FileNotFoundException e) {
				logger.error("Error saving settings to store Ingest From dir.", e);
			}
			return load;
		}
		return null;
	}

	private File get_save_to_File(Component parent) {
		HJB4UConfiguration settings = SettingsStore.getInstance().getSettings();
		JFileChooser fc = new JFileChooser();
		if (settings.getSaveDir() != null) {
			fc.setCurrentDirectory(new File(settings.getSaveDir()));
		}
		if (fc.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
			File save_to = fc.getSelectedFile();
			settings.setSaveDir(save_to.getParent());
			try {
				SettingsStore.getInstance().save();
			} catch (JAXBException e) {
				logger.error("Error saving settings to store Save To dir.", e);
			} catch (FileNotFoundException e) {
				logger.error("Error saving settings to store Save To dir.", e);
			}
			return save_to;
		}
		return null;
	}


	{
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
		$$$setupUI$$$();
	}

	/**
	 * Method generated by IntelliJ IDEA GUI Designer
	 * >>> IMPORTANT!! <<<
	 * DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$() {
		panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		createDatabaseButton = new JButton();
		createDatabaseButton.setText("Create Database");
		GridBagConstraints gbc;
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(0, 0, 10, 0);
		panel.add(createDatabaseButton, gbc);
		createXMLButton = new JButton();
		createXMLButton.setText("Create XML");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(0, 0, 10, 0);
		panel.add(createXMLButton, gbc);
		changeSettingsButton = new JButton();
		changeSettingsButton.setText("Change Settings");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(0, 0, 10, 0);
		panel.add(changeSettingsButton, gbc);
		ingestXMLButton = new JButton();
		ingestXMLButton.setText("Ingest XML");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(0, 0, 10, 0);
		panel.add(ingestXMLButton, gbc);
		makeMapButton = new JButton();
		makeMapButton.setText("Make Map");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(0, 0, 10, 0);
		panel.add(makeMapButton, gbc);
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$() {
		return panel;
	}
}
