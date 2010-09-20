package hjb4u.selector;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;
import java.util.UUID;


public class Launch {
    public static void main(String[] args) throws IOException {
        PrintStream os = System.out;
        if(args.length > 0)
        {

            os = new PrintStream(new FileOutputStream(new File(args[0])));
        }
        File schema = null;
        JFileChooser fc = new JFileChooser();
        Properties details = new Properties();
        fc.setCurrentDirectory(new File("."));
        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            schema = fc.getSelectedFile();
        }

        if(schema != null && schema.exists())
        {
            schema = schema.getAbsoluteFile().getCanonicalFile();
            details.setProperty("hjb4u.schema.file",schema.getName());
            details.setProperty("hjb4u.schema.directory",schema.getParent());
            details.setProperty("uuid", UUID.randomUUID().toString());
            details.store(os,"");
        }



    }

}