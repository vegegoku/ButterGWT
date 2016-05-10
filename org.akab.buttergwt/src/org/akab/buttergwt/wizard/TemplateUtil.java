package org.akab.buttergwt.wizard;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.akab.buttergwt.wizard.plugin.PluginSettings;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

public class TemplateUtil
{
  public static final String TEMPLATE_NAME = "TEMPLATE_NAME";
  public static final String PACKAGE_PATH = "PACKAGE_PATH";
  public static final String FILE_POSTFIX = "FILE_POSTFIX";
  
  public static void createCompilationUnitFromTemplate(IProject project, PluginSettings pluginSettings, Map<String, String> params)
    throws CoreException
  {
    String template = getTemplateString((String)params.get("TEMPLATE_NAME"));
    String templateString = replaceTemplateParameters(template, params);
    
    IFile file = project.getFile("src/main/java/" + pluginSettings.asPackagePath() + 
      (String)params.get("PACKAGE_PATH") + (String)params.get("NAME") + (String)params.get("FILE_POSTFIX"));
    
    InputStream source = new ByteArrayInputStream(templateString.getBytes());
    file.create(source, true, null);
  }
  
  public static String replaceTemplateParameters(String template, Map<String, String> parameters)
  {
    Iterator<String> paramsIterator = parameters.keySet().iterator();
    while (paramsIterator.hasNext())
    {
      String key = (String)paramsIterator.next();
      template = replaceParam(template, key, (String)parameters.get(key));
    }
    return template;
  }
  
  public static String replaceParam(String template, String key, String value)
  {
    while (template.contains("${" + key + "}"))
    {
      if (value == null) {
        value = "";
      }
      template = template.replace("${" + key + "}", value);
    }
    return template;
  }
  
  public static String getTemplateString(String templateName)
  {
    InputStream is = TemplateUtil.class.getResourceAsStream("/org/akab/buttergwt/templates/" + templateName);
    
    String template = getStringFromInputStream(is);
    return template;
  }
  
  /* Error */
  private static String getStringFromInputStream(InputStream is)
  {
	  BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

		    br = new BufferedReader(new InputStreamReader(is));
		    while ((line = br.readLine()) != null) {
			sb.append(line);
			sb.append("\n");
		    }

		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    if (br != null) {
			try {
			    br.close();
			} catch (IOException e) {
			    e.printStackTrace();
			}
		    }
		}

		return sb.toString();
  }
}
