package com.sun.javafx.tools.stringtemplate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

/**
 * RunStringTemplate
 *
 * @author Brian Goetz
 */
public class ExpandXxxTemplate {

    private static final String[] keys = { "Int", "Double", "Float", "Short", "Char", "Long", "Boolean", "Byte" };
    private static final Map<String, String> primMap = new HashMap<String, String>();
    private static final Map<String, String> boxMap = new HashMap<String, String>();
    static {
        for (String k : keys) {
            primMap.put(k, k.toLowerCase());
            boxMap.put(k, k);
        }
        boxMap.put("Int", "Integer");
        boxMap.put("Char", "Character");
    }
    
    public static void main(String[] args) throws IOException {
        String templatePath = args[0];
        File templateFile = new File(templatePath);
        String templateName = templateFile.getName();
        String templateDir = templateFile.getParent();
        File outputDir = new File(args[1], templateDir);
        outputDir.mkdirs();
        StringTemplateGroup stg = new StringTemplateGroup("group");
        if (templateName.contains("Xxx")) {
            for (String k : keys) {
                StringTemplate st = stg.getInstanceOf(templatePath);
                String outName = templateName.replace("Xxx", k);
                st.setAttribute("PREFIX", k);
                st.setAttribute("TEMPLATE_NAME", templateName);
                st.setAttribute("PRIM", primMap.get(k));
                st.setAttribute("BOXED", boxMap.get(k));
                Writer out = new FileWriter(new File(outputDir, outName + ".java"));
                out.write(st.toString());
                out.close();
            }
        }
    }
}
