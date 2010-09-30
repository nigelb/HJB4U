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

import hjb4u.config.DBConf;
import hjb4u.config.ELogLevel;
import hjb4u.config.HJB4UConfiguration;
import hjb4u.config.NameSpaceMapping;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.bind.JAXBException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * <code>Settings</code>
 * Date: Jun 2, 2009
 * Time: 8:45:30 AM
 *
 * @author Nigel B
 */
public class Settings {
	private JPanel panel1;
	private JButton cancelButton;
	private JButton applyButton;
	private JButton OKButton;
	private JTabbedPane tabbedPane1;
	private JList nameSpaces;
	private JTextField nameSpace;
	private JTextField prefix;
	private JButton nsSaveAddButton;
	private JButton nsDeleteButton;
	private JTextField dbNewName;
	private JTextField dbNewDriver;
	private JTextField dbNewDialect;
	private JButton nsClearButton;
	private JComboBox dbTemplates;
	private JTextField dbNewUsername;
	private JTextField dbNewPassword;
	private JTextField dbNewURL;
	private JButton dbClearButton;
	private JButton dbSaveAddButton;
	private JButton dbDeleteButton;
	private JComboBox selectedDatabase;
	private JCheckBox validate;
	private JPanel generalSettingsPanel;
	private JPanel dbPanel;
	private JPanel dbButPan;
	private JPanel nsInputPanel;
	private JPanel nsWrappedPanel;
	private JPanel nsTabPanel;
	private JPanel dbInputWrapperPanel;
	private JPanel dbInputPanel;
	private JPanel buttonPanel;
	private JList databases;
	private JProgressBar nsSpacer;
	private JProgressBar dbSpacer;
	private JComboBox rootElement;
	private JTextArea rootID;
	private JCheckBox lgEnableLF5;
	private JCheckBox lgEnablePane;
	private JTextField lgLF5Size;
	private JComboBox lgLF5Level;
	private JTextField lgPaneSize;
	private JComboBox lgPaneLevel;
	private JButton showLoggingPanelButton;
	private JTextField lgPanelPattern;
	private JProgressBar lgSpacer;
	private JPanel logging;
	private JPanel lgSettings;
	private JLabel lgPatternLink;
	private JCheckBox defaultNS;
	private HJB4UConfiguration settings = SettingsStore.getInstance().getSettings();
	private Logger logger = Logger.getLogger(Settings.class);
	private JDialog parent;

	public Settings(JDialog parent) {
		this();
		this.parent = parent;
	}

	public Settings() {
		$$$setupUI$$$();
		lgPatternLink.setText("<html><a href=\"http://www.jcu.edu.au\"><font color=\"blue\">Logging&nbsp;Panel&nbsp;Log&nbsp;Pattern:</font></a></html>");
		OKButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fromGUI();
				try {
					SettingsStore.getInstance().save();
				} catch (JAXBException e1) {
					logger.error("Could Not Save: " + e1, e1);
				} catch (FileNotFoundException e1) {
					logger.error("Could Not Save: " + e1, e1);
				}
				parent.setVisible(false);
			}
		});
		applyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fromGUI();
				try {
					SettingsStore.getInstance().save();
				} catch (JAXBException e1) {
					logger.error("Could Not Save: " + e1, e1);
				} catch (FileNotFoundException e1) {
					logger.error("Could Not Save: " + e1, e1);
				}
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent.setVisible(false);
			}
		});
		nameSpaces.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				NameSpaceMapping nm = (NameSpaceMapping) nameSpaces.getSelectedValue();
				ListModel m = nameSpaces.getModel();
				defaultNS.setEnabled(!NameSpaceMapping.defaultExists());
				if (nm != null) {
					defaultNS.getModel().setSelected(!nm.isDefault());
					if (!nm.isDefault()) {
						prefix.setEnabled(true);
						nameSpace.setText(nm.getNamespace());
						prefix.setText(nm.getPrefix());
					} else {
						nameSpace.setText(nm.getNamespace());
						prefix.setText(null);
						prefix.setEnabled(false);
						defaultNS.setEnabled(true);
					}
				}
			}
		});

		nsClearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nameSpace.setText("");
				prefix.setText("");
			}
		});
		nsSaveAddButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HashListModel model = (HashListModel) nameSpaces.getModel();
				NameSpaceMapping nm = (NameSpaceMapping) model.getModel().get(nameSpace.getText());
				if (nm != null && nm.isDefault()) {
					nm.setDefault(false);
				}
				if (prefix.isEnabled()) {
					nm = new NameSpaceMapping(nameSpace.getText(), prefix.getText());
				} else {
					nm = new NameSpaceMapping(nameSpace.getText());
				}

				model.insert(nm.getNamespace(), nm);
				nameSpaces.repaint();
			}
		});
		nsDeleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object[] sel = nameSpaces.getSelectedValues();
				nameSpaces.setSelectedIndices(new int[]{});
				HashListModel model = (HashListModel) nameSpaces.getModel();
				NameSpaceMapping nm;
				for (Object o : sel) {
					nm = (NameSpaceMapping) o;
					nm.setDefault(false);
					model.remove(nm.getNamespace());
				}
				nameSpaces.repaint();
			}
		});

		dbClearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dbNewName.setText("");
				dbNewDialect.setText("");
				dbNewDriver.setText("");
				dbNewUsername.setText("");
				dbNewPassword.setText("");
				dbNewURL.setText("");
			}
		});
		dbSaveAddButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DBConf newDB = new DBConf();
				newDB.setName(reduce(dbNewName.getText()));
				newDB.setDialect(reduce(dbNewDialect.getText()));
				newDB.setDriver(reduce(dbNewDriver.getText()));
				newDB.setUsername(reduce(dbNewUsername.getText()));
				newDB.setPassword(reduce(dbNewPassword.getText()));
				newDB.setUrl(reduce(dbNewURL.getText()));
				newDB.setUuid(UUID.randomUUID().toString());
				if (newDB.getDialect() != null &&
						newDB.getName() != null &&
						newDB.getDriver() != null) {
					((HashListModel) databases.getModel()).insert(newDB.getUuid(), newDB);
				}
			}
		});
		dbDeleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object[] selected = databases.getSelectedValues();
				for (Object o : selected) {
					((HashListModel) databases.getModel()).remove(((DBConf) o).getUuid());
				}
			}
		});
		dbTemplates.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DBConf templ = (DBConf) dbTemplates.getSelectedItem();
				dbNewName.setText(templ.getName());
				dbNewDialect.setText(templ.getDialect());
				dbNewDriver.setText(templ.getDriver());
				dbNewUsername.setText(templ.getUsername());
				dbNewPassword.setText(templ.getPassword());
				dbNewURL.setText(templ.getUrl());
			}
		});
		databases.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				DBConf templ = (DBConf) databases.getSelectedValue();
				dbNewName.setText(templ.getName());
				dbNewDialect.setText(templ.getDialect());
				dbNewDriver.setText(templ.getDriver());
				dbNewUsername.setText(templ.getUsername());
				dbNewPassword.setText(templ.getPassword());
				dbNewURL.setText(templ.getUrl());
			}
		});

		showLoggingPanelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
		});

		lgLF5Size.setInputVerifier(new InputVerifier() {
			@Override
			public boolean verify(JComponent input) {
				try {
					Integer.parseInt(lgLF5Size.getText());
				} catch (NumberFormatException ne) {
					JOptionPane.showMessageDialog(lgLF5Size, "Must be a valid Integer", "Size not formatted correctly.", JOptionPane.ERROR_MESSAGE);
					return false;
				}
				return true;
			}
		});

		lgPaneSize.setInputVerifier(new InputVerifier() {
			@Override
			public boolean verify(JComponent input) {
				try {
					Integer.parseInt(lgPaneSize.getText());
				} catch (NumberFormatException ne) {
					JOptionPane.showMessageDialog(lgPaneSize, "Must be a valid Integer", "Size not formatted correctly.", JOptionPane.ERROR_MESSAGE);
					return false;
				}
				return true;
			}
		});

		lgEnableLF5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateLF5State();
			}
		});

		lgEnablePane.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updatePaneState();
			}
		});

		lgPatternLink.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				lgPatternLink.setText("<html><a href=\"#\"><font color=\"red\">Logging&nbsp;Panel&nbsp;Log&nbsp;Pattern:</font></a></html>");
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				lgPatternLink.setText("<html><a href=\"#\"><font color=\"blue\">Logging&nbsp;Panel&nbsp;Log&nbsp;Pattern:</font></a></html>");
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Desktop.getDesktop().browse(new URI(settings.getPatternURL()));
				}
				catch (Exception ex) {
					logger.error("Could not open URL: " + settings.getPatternURL(), ex);
				}


			}
		});

		lgPatternLink.setCursor(new Cursor(Cursor.HAND_CURSOR));

		defaultNS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				prefix.setEnabled(defaultNS.getModel().isSelected());
			}
		});

		toGUI();

	}

	private void updateLF5State() {
		boolean state = lgEnableLF5.isSelected();
		lgLF5Level.setEnabled(state);
		lgLF5Size.setEnabled(state);
	}

	private void updatePaneState() {
		boolean state = lgEnablePane.isSelected();
		lgPaneSize.setEnabled(state);
		lgPaneLevel.setEnabled(state);
		lgPanelPattern.setEnabled(state);
	}

	private void toGUI() {
		validate.getModel().setPressed(settings.isValidate());

		//Populate NameSpaces/
		List<NameSpaceMapping> nslist = settings.getNamespaces();
		HashListModel<NameSpaceMapping> nsModel = new HashListModel<NameSpaceMapping>();
		for (NameSpaceMapping aNslist : nslist) {
			nsModel.insert(aNslist.getNamespace(), aNslist);
		}
		nameSpaces.setModel(nsModel);
		if (NameSpaceMapping.defaultExists()) {
			defaultNS.getModel().setSelected(true);
			defaultNS.setEnabled(false);
			prefix.setEnabled(true);
		}

		//Populate Databases.
		List<DBConf> dbs = settings.getDbs();
		final HashListModel<DBConf> dbModel = new HashListModel<DBConf>();
		for (DBConf db : dbs) {
			dbModel.insert(db.getUuid(), db);
		}
		dbModel.setCmp(new Comparator<String>() {
			public int compare(String o1, String o2) {
				return dbModel.getModel().get(o1).getName()
						.compareTo(dbModel.getModel().get(o2).getName());
			}
		});
		databases.setModel(dbModel);
		selectedDatabase.setModel(new ComboListModelLW(dbModel));
		if (settings.getDatabase() != null && dbModel.getModel().get(settings.getDatabase()) != null) {
			selectedDatabase.setSelectedItem(dbModel.getModel().get(settings.getDatabase()));
		}

		//Populate Dataase Templates.
		DefaultComboBoxModel templatesModel = new DefaultComboBoxModel();
		templatesModel.addElement(new DBConf());
		for (DBConf dbList : SettingsStore.getInstance().getTemplates().getDbs()) {
			templatesModel.addElement(dbList);
		}
		dbTemplates.setModel(templatesModel);

		//Populate Root ID
		rootID.setText(Long.toString(settings.getRootID()));

		//Populate XMLElelemnts
		HashListModel<ListClass> rootModel = new HashListModel<ListClass>();
		for (ListClass aClass : SettingsStore.getInstance().makeRootElementList()) {
			rootModel.insert(aClass.getMyclass().getName(), aClass);
		}
		rootElement.setModel(new ComboListModelLW(rootModel));
		if (settings.getRootElementType() != null) {
			rootElement.setSelectedItem(rootModel.getModel().get(settings.getRootElementType()));
		}

		lgEnableLF5.setSelected(settings.isEnableLF5());
		lgEnablePane.setSelected(settings.isEnableLoggingPane());

		lgLF5Level.setModel(new DefaultComboBoxModel(ELogLevel.values()));
		lgLF5Level.getModel().setSelectedItem(settings.getLf5LogLevel());

		lgPaneLevel.setModel(new DefaultComboBoxModel(ELogLevel.values()));
		lgPaneLevel.getModel().setSelectedItem(settings.getPaneLogLevel());

		lgPaneSize.setText("" + settings.getPaneSize());
		lgLF5Size.setText("" + settings.getLf5Size());

		lgPanelPattern.setText(settings.getPanePattern());
		updateLF5State();
		updatePaneState();

	}

	private void fromGUI() {
		settings.setValidate(validate.getModel().isPressed());
		if (selectedDatabase.getSelectedItem() != null) {
			settings.setDatabase(((DBConf) selectedDatabase.getSelectedItem()).getUuid());
		}

		if (rootElement.getSelectedItem() != null) {
			settings.setRootElementType(((ListClass) rootElement.getSelectedItem()).getMyclass().getName());
		}

		try {
			settings.setRootID(Long.parseLong(rootID.getText()));
		} catch (NumberFormatException ne) {
			JOptionPane.showMessageDialog($$$getRootComponent$$$(),
					"The root ID you entered is invalid. It needs to be a valid Integer.",
					"Invalid ID.",
					JOptionPane.ERROR_MESSAGE);
			throw ne;
		}

		HashListModel<NameSpaceMapping> model = (HashListModel<NameSpaceMapping>) nameSpaces.getModel();
		settings.setNamespaces(new ArrayList<NameSpaceMapping>(model.getModel().values()));

		HashListModel<DBConf> dbmodel = (HashListModel<DBConf>) databases.getModel();
		settings.setDbs(new ArrayList<DBConf>(dbmodel.getModel().values()));


		settings.setEnableLF5(lgEnableLF5.isSelected());
		settings.setLf5LogLevel((ELogLevel) lgLF5Level.getSelectedItem());
		settings.setLf5Size(Integer.parseInt(lgLF5Size.getText()));

		settings.setEnableLoggingPane(lgEnablePane.isSelected());
		settings.setPaneLogLevel((ELogLevel) lgPaneLevel.getSelectedItem());
		settings.setPaneSize(Integer.parseInt(lgPaneSize.getText()));
		settings.setPanePattern(lgPanelPattern.getText());
	}

	private String reduce(String toR) {
		if (toR.length() == 0) {
			return null;
		}
		return toR;
	}

	private void createUIComponents() {
		nsSpacer = new JProgressBar() {
			@Override
			public void paint(Graphics g) {
			}
		};
		dbSpacer = new JProgressBar() {
			@Override
			public void paint(Graphics g) {
			}
		};
		lgSpacer = new JProgressBar() {
			@Override
			public void paint(Graphics g) {
			}
		};
		rootID = new JTextArea();
		rootID.setBorder(new LineBorder(new Color(0, 0, 0), 1));
	}

	public static void display(JFrame parent, String Title) {
		JDialog sets = new JDialog(parent, SettingsStore.getInstance().getSettings().getSchema() + ": Settings.");
		sets.setContentPane(new Settings(sets).$$$getRootComponent$$$());
		sets.setModal(true);
		sets.setSize(800, 400);
		sets.setLocationRelativeTo(null);
		sets.setVisible(true);
		sets.dispose();
	}

	/**
	 * Method generated by IntelliJ IDEA GUI Designer
	 * >>> IMPORTANT!! <<<
	 * DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$() {
		createUIComponents();
		panel1 = new JPanel();
		panel1.setLayout(new BorderLayout(0, 0));
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		panel1.add(buttonPanel, BorderLayout.SOUTH);
		OKButton = new JButton();
		OKButton.setText("OK");
		buttonPanel.add(OKButton);
		applyButton = new JButton();
		applyButton.setText("Apply");
		buttonPanel.add(applyButton);
		cancelButton = new JButton();
		cancelButton.setText("Cancel");
		buttonPanel.add(cancelButton);
		tabbedPane1 = new JTabbedPane();
		panel1.add(tabbedPane1, BorderLayout.CENTER);
		generalSettingsPanel = new JPanel();
		generalSettingsPanel.setLayout(new GridBagLayout());
		tabbedPane1.addTab("General", generalSettingsPanel);
		selectedDatabase = new JComboBox();
		final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
		selectedDatabase.setModel(defaultComboBoxModel1);
		GridBagConstraints gbc;
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		generalSettingsPanel.add(selectedDatabase, gbc);
		final JLabel label1 = new JLabel();
		label1.setText("Database");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		generalSettingsPanel.add(label1, gbc);
		rootElement = new JComboBox();
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		generalSettingsPanel.add(rootElement, gbc);
		final JLabel label2 = new JLabel();
		label2.setText("Root XML Element ");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.WEST;
		generalSettingsPanel.add(label2, gbc);
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.ipadx = 1;
		gbc.ipady = 1;
		gbc.insets = new Insets(2, 2, 2, 2);
		generalSettingsPanel.add(rootID, gbc);
		final JLabel label3 = new JLabel();
		label3.setText("Item ID to look up");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.WEST;
		generalSettingsPanel.add(label3, gbc);
		validate = new JCheckBox();
		validate.setText("");
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.WEST;
		generalSettingsPanel.add(validate, gbc);
		final JLabel label4 = new JLabel();
		label4.setText("Validate");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.WEST;
		generalSettingsPanel.add(label4, gbc);
		dbPanel = new JPanel();
		dbPanel.setLayout(new BorderLayout(0, 0));
		tabbedPane1.addTab("Databases", dbPanel);
		dbInputWrapperPanel = new JPanel();
		dbInputWrapperPanel.setLayout(new BorderLayout(0, 0));
		dbPanel.add(dbInputWrapperPanel, BorderLayout.EAST);
		dbInputPanel = new JPanel();
		dbInputPanel.setLayout(new GridBagLayout());
		dbInputWrapperPanel.add(dbInputPanel, BorderLayout.CENTER);
		final JLabel label5 = new JLabel();
		label5.setText("Driver");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(0, 5, 0, 0);
		dbInputPanel.add(label5, gbc);
		final JLabel label6 = new JLabel();
		label6.setText("Dialect");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(0, 5, 0, 0);
		dbInputPanel.add(label6, gbc);
		final JLabel label7 = new JLabel();
		label7.setText("Name");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(0, 5, 0, 0);
		dbInputPanel.add(label7, gbc);
		final JLabel label8 = new JLabel();
		label8.setText("Templates");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.ipadx = 5;
		gbc.insets = new Insets(0, 5, 0, 0);
		dbInputPanel.add(label8, gbc);
		dbTemplates = new JComboBox();
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		dbInputPanel.add(dbTemplates, gbc);
		dbNewName = new JTextField();
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		dbInputPanel.add(dbNewName, gbc);
		dbNewDriver = new JTextField();
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		dbInputPanel.add(dbNewDriver, gbc);
		dbNewDialect = new JTextField();
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		dbInputPanel.add(dbNewDialect, gbc);
		final JLabel label9 = new JLabel();
		label9.setText("Username");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(0, 5, 0, 0);
		dbInputPanel.add(label9, gbc);
		final JLabel label10 = new JLabel();
		label10.setText("Password");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(0, 5, 0, 0);
		dbInputPanel.add(label10, gbc);
		final JLabel label11 = new JLabel();
		label11.setText("URL");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(0, 5, 0, 0);
		dbInputPanel.add(label11, gbc);
		dbNewUsername = new JTextField();
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		dbInputPanel.add(dbNewUsername, gbc);
		dbNewPassword = new JTextField();
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 5;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		dbInputPanel.add(dbNewPassword, gbc);
		dbNewURL = new JTextField();
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 6;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		dbInputPanel.add(dbNewURL, gbc);
		dbButPan = new JPanel();
		dbButPan.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 7;
		dbInputPanel.add(dbButPan, gbc);
		dbDeleteButton = new JButton();
		dbDeleteButton.setText("Delete ->");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		dbButPan.add(dbDeleteButton, gbc);
		dbClearButton = new JButton();
		dbClearButton.setText("^Clear^");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		dbButPan.add(dbClearButton, gbc);
		dbSaveAddButton = new JButton();
		dbSaveAddButton.setText("<- Save/Add");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		dbButPan.add(dbSaveAddButton, gbc);
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 8;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.ipadx = 180;
		dbInputPanel.add(dbSpacer, gbc);
		final JPanel panel2 = new JPanel();
		panel2.setLayout(new BorderLayout(0, 0));
		dbPanel.add(panel2, BorderLayout.CENTER);
		final JScrollPane scrollPane1 = new JScrollPane();
		panel2.add(scrollPane1, BorderLayout.CENTER);
		scrollPane1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-16777216)), null));
		databases = new JList();
		scrollPane1.setViewportView(databases);
		nsTabPanel = new JPanel();
		nsTabPanel.setLayout(new BorderLayout(0, 0));
		tabbedPane1.addTab("Namespaces", nsTabPanel);
		nsWrappedPanel = new JPanel();
		nsWrappedPanel.setLayout(new GridBagLayout());
		nsTabPanel.add(nsWrappedPanel, BorderLayout.EAST);
		nsInputPanel = new JPanel();
		nsInputPanel.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.ipadx = 3;
		nsWrappedPanel.add(nsInputPanel, gbc);
		final JLabel label12 = new JLabel();
		label12.setText("Name Space");
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(0, 5, 0, 0);
		nsInputPanel.add(label12, gbc);
		nameSpace = new JTextField();
		nameSpace.setText("");
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		nsInputPanel.add(nameSpace, gbc);
		final JLabel label13 = new JLabel();
		label13.setText("Prefix");
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(0, 5, 0, 0);
		nsInputPanel.add(label13, gbc);
		prefix = new JTextField();
		prefix.setEnabled(false);
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		nsInputPanel.add(prefix, gbc);
		final JPanel panel3 = new JPanel();
		panel3.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 2;
		gbc.gridheight = 3;
		gbc.fill = GridBagConstraints.BOTH;
		nsInputPanel.add(panel3, gbc);
		nsDeleteButton = new JButton();
		nsDeleteButton.setText("Delete ->");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		panel3.add(nsDeleteButton, gbc);
		nsClearButton = new JButton();
		nsClearButton.setText("^Clear^");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel3.add(nsClearButton, gbc);
		nsSaveAddButton = new JButton();
		nsSaveAddButton.setText("<- Save/Add");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		panel3.add(nsSaveAddButton, gbc);
		defaultNS = new JCheckBox();
		defaultNS.setText("");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.WEST;
		nsInputPanel.add(defaultNS, gbc);
		nsSpacer.setEnabled(true);
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.ipadx = 180;
		nsWrappedPanel.add(nsSpacer, gbc);
		final JScrollPane scrollPane2 = new JScrollPane();
		nsTabPanel.add(scrollPane2, BorderLayout.CENTER);
		scrollPane2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-16777216)), null));
		nameSpaces = new JList();
		final DefaultListModel defaultListModel1 = new DefaultListModel();
		nameSpaces.setModel(defaultListModel1);
		scrollPane2.setViewportView(nameSpaces);
		logging = new JPanel();
		logging.setLayout(new BorderLayout(0, 0));
		tabbedPane1.addTab("Logging", logging);
		lgSettings = new JPanel();
		lgSettings.setLayout(new GridBagLayout());
		logging.add(lgSettings, BorderLayout.CENTER);
		lgEnableLF5 = new JCheckBox();
		lgEnableLF5.setText("Enable LF5 Logger Pane");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.WEST;
		lgSettings.add(lgEnableLF5, gbc);
		lgEnablePane = new JCheckBox();
		lgEnablePane.setText("Enable Logging Panel");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.WEST;
		lgSettings.add(lgEnablePane, gbc);
		lgLF5Size = new JTextField();
		lgLF5Size.setColumns(10);
		lgLF5Size.setEditable(true);
		lgLF5Size.setEnabled(true);
		lgLF5Size.setToolTipText("The number of log entries to keep in memory.");
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		lgSettings.add(lgLF5Size, gbc);
		lgLF5Level = new JComboBox();
		lgLF5Level.setEnabled(true);
		gbc = new GridBagConstraints();
		gbc.gridx = 3;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		lgSettings.add(lgLF5Level, gbc);
		final JLabel label14 = new JLabel();
		label14.setText("Size");
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		lgSettings.add(label14, gbc);
		lgPaneSize = new JTextField();
		lgPaneSize.setEnabled(true);
		lgPaneSize.setToolTipText("The number of log entries to keep in memory.");
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		lgSettings.add(lgPaneSize, gbc);
		lgPaneLevel = new JComboBox();
		lgPaneLevel.setEnabled(true);
		gbc = new GridBagConstraints();
		gbc.gridx = 3;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		lgSettings.add(lgPaneLevel, gbc);
		final JLabel label15 = new JLabel();
		label15.setText("Log Level");
		gbc = new GridBagConstraints();
		gbc.gridx = 3;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		lgSettings.add(label15, gbc);
		showLoggingPanelButton = new JButton();
		showLoggingPanelButton.setEnabled(true);
		showLoggingPanelButton.setText("Show Logging Panel");
		gbc = new GridBagConstraints();
		gbc.gridx = 5;
		gbc.gridy = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		lgSettings.add(showLoggingPanelButton, gbc);
		final JLabel label16 = new JLabel();
		label16.setEnabled(true);
		label16.setText(" ");
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.WEST;
		lgSettings.add(label16, gbc);
		final JLabel label17 = new JLabel();
		label17.setEnabled(true);
		label17.setText(" ");
		gbc = new GridBagConstraints();
		gbc.gridx = 4;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.WEST;
		lgSettings.add(label17, gbc);
		lgPanelPattern = new JTextField();
		lgPanelPattern.setEnabled(true);
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		lgSettings.add(lgPanelPattern, gbc);
		lgPatternLink = new JLabel();
		lgPatternLink.setText(" Jimmy");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.WEST;
		lgSettings.add(lgPatternLink, gbc);
		lgSpacer.setMaximum(100);
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.ipadx = 130;
		lgSettings.add(lgSpacer, gbc);
		final JPanel panel4 = new JPanel();
		panel4.setLayout(new GridBagLayout());
		logging.add(panel4, BorderLayout.NORTH);
		final JLabel label18 = new JLabel();
		label18.setText("The application has to be restarted before changes to these settings will take place.");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		panel4.add(label18, gbc);
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$() {
		return panel1;
	}
}
