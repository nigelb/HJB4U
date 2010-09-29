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

import hjb4u.AnnotationPair;
import hjb4u.ListClass;
import hjb4u.Pair;
import hjb4u.SettingsStore;

import javax.persistence.Table;
import javax.swing.*;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * <code>Mapping</code>
 * Date: Jul 9, 2009
 * Time: 11:25:45 AM
 *
 * @author Nigel B
 */
public class Mapping {
    private JPanel panel1;
    private JTextPane TableNamePane;
    private JTabbedPane tabbedPane1;
    private JTextPane EnumValuePane;
    private JScrollPane tableScroll;
    private JScrollPane enumScroll;
    private JDialog parent;


    public Mapping(JDialog parent) {

        this.parent = parent;
        $$$setupUI$$$();
    }

    private void createUIComponents() {
        List<ListClass> classes = SettingsStore.getInstance().makeRootElementList();
        List<AnnotationPair> ant = new ArrayList<AnnotationPair>();
        for (ListClass aClass : classes) {
            getAnnotationPairs(new Class[]{aClass.getMyclass()}, ant, null);
            getAnnotationPairs(aClass.getMyclass().getClasses(), ant, aClass.getMyclass());
        }

        Collections.sort(ant);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintWriter f = new PrintWriter(bos);
        f.printf("%35s %s ", "XML Element Name", "SQL Table Name");
        f.println();
        f.println();
        for (AnnotationPair annotationPair : ant) {
            f.printf("%35s %s ", annotationPair.getXr().name(), annotationPair.getTb().name());
            f.println();
        }
        f.flush();


        TableNamePane = new JTextPane();
        TableNamePane.setText(new String(bos.toByteArray()));
        TableNamePane.setFont(new Font("Monospaced", Font.PLAIN, 12));
        tableScroll = new JScrollPane();
//        tableScroll.scrollRectToVisible(new Rectangle(0, 0, 0, 0));

        bos = new ByteArrayOutputStream();
        f = new PrintWriter(bos);
        ArrayList<Pair<String, ArrayList<Pair<String, String>>>> enums = getEnumAnnotations(SettingsStore.getInstance().getAllJAXBClasses());
        int len = 0;
        for (Pair<String, ArrayList<Pair<String, String>>> anEnum : enums) {
            len = anEnum.getItem1().length();
            f.printf(String.format("%%%ds", (int) ((50d + len) / 2)), anEnum.getItem1());
            f.println();
            f.printf("==================================================");
            f.println();
            f.printf("%24s   %s ", "Database Value", "Xml Value");
            f.println();
            f.println();
            for (Pair<String, String> item : anEnum.getItem2()) {
                f.printf("%24s   %s ", item.getItem1(), item.getItem2());
                f.println();
            }
            f.println();
            f.println();
        }
        f.flush();

        EnumValuePane = new JTextPane();
        EnumValuePane.setText(new String(bos.toByteArray()));
        EnumValuePane.setFont(new Font("Monospaced", Font.PLAIN, 12));
        enumScroll = new JScrollPane();
    }

    private ArrayList<Pair<String, ArrayList<Pair<String, String>>>> getEnumAnnotations(Class[] classes) {
        XmlType en;
        Annotation[] at;
        Class cl;
        ArrayList<Pair<String, ArrayList<Pair<String, String>>>> toRet = new ArrayList<Pair<String, ArrayList<Pair<String, String>>>>();
        Pair<String, ArrayList<Pair<String, String>>> enumV;
        for (Class aClass : classes) {
            if (aClass.isEnum()) {
                en = (XmlType) aClass.getAnnotation(XmlType.class);
                enumV = new Pair<String, ArrayList<Pair<String, String>>>(en == null ? aClass.getName() : en.name(), new ArrayList<Pair<String, String>>());
                toRet.add(enumV);
                Field[] fields = aClass.getFields();
                for (Field field : fields) {
                    XmlEnumValue an = field.getAnnotation(XmlEnumValue.class);
                    if (an != null) {
                        enumV.getItem2().add(new Pair<String, String>(field.getName(), an.value()));
                    } else if (field.getType() == aClass) {
                        enumV.getItem2().add(new Pair<String, String>(field.getName(), field.getName()));
                    }

                }
            }
        }
        return toRet;
    }

    private void getAnnotationPairs(Class[] classes, List<AnnotationPair> store, Class myclass) {
        Annotation[] at;
        XmlRootElement re = null;
        Table tb = null;
        Class cl;
        for (Class aClass : classes) {
            re = null;
            tb = null;
            at = aClass.getAnnotations();
            for (Annotation annotation : at) {
                if (annotation instanceof XmlRootElement) {
                    re = (XmlRootElement) annotation;

                } else if (annotation instanceof Table) {
                    tb = (Table) annotation;
                }
            }

            if (myclass != null) {
                Field[] f = myclass.getDeclaredFields();
                for (final Field field : f) {
                    if (field.getType().equals(aClass)) {
                        store.add(new AnnotationPair(tb, new XmlRootElement() {
                            public String namespace() {
                                return null;
                            }

                            public String name() {
                                return field.getName();
                            }

                            public Class<? extends Annotation> annotationType() {
                                return null;
                            }
                        }));
                    }

                }
            } else if (re != null && tb != null) {
                store.add(new AnnotationPair(tb, re));
            }
        }

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
        tabbedPane1 = new JTabbedPane();
        panel1.add(tabbedPane1, BorderLayout.CENTER);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout(0, 0));
        tabbedPane1.addTab("Table Names", panel2);
        tableScroll = new JScrollPane();
        panel2.add(tableScroll, BorderLayout.CENTER);
        TableNamePane.setEditable(false);
        tableScroll.setViewportView(TableNamePane);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new BorderLayout(0, 0));
        tabbedPane1.addTab("Enum Values", panel3);
        enumScroll = new JScrollPane();
        panel3.add(enumScroll, BorderLayout.CENTER);
        EnumValuePane.setEditable(false);
        enumScroll.setViewportView(EnumValuePane);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}
