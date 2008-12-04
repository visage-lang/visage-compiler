package com.sun.javafx.tools.stringtemplate;

import java.io.*;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

/**
 * RunStringTemplate
 *
 * @author Brian Goetz
 */
public class ExpandXxxTemplate {

    private static final String[] keys = { "Int", "Double", "Float", "Short", "Char", "Long", "Boolean", "Byte" };

    /**
     * Usage: ExpandXxxTemplate dest-root relative-source-path template-name...
     */
    public static void main(String[] args) throws IOException {
        File destDir = new File(args[0]);
        String sourcePath = args[1];
        InputStream stream = ExpandXxxTemplate.class.getClassLoader().getResourceAsStream(sourcePath + File.separator + "XxxTemplate.stg");
        if (stream == null)
            throw new RuntimeException("Cannot find " + sourcePath + File.separator + "XxxTemplate.stg on class path");
        StringTemplateGroup stg = new StringTemplateGroup(new InputStreamReader(stream));
        StringTemplateGroup loader = new StringTemplateGroup("Xxx");
        for (int i=2; i<args.length; i++) {
            String templateName = args[i];
            File outputDir = new File(destDir, sourcePath);
            outputDir.mkdirs();
            for (String k : keys) {
                String outName = templateName.replace("Xxx", k);
                File outFile = new File(outputDir, outName + ".java");
                if (outFile.exists())
                    continue;
                StringTemplate st = loader.getInstanceOf(sourcePath + File.separator + templateName);
                st.setGroup(stg);
                st.setAttribute("PREFIX", k);
                st.setAttribute("NUMERIC", !k.equals("Boolean") && !k.equals("Char"));
                st.setAttribute("TEMPLATE_NAME", templateName);
                Writer out = new FileWriter(outFile);
                out.write(st.toString());
                out.close();
            }
        }
    }
}
