package org.akab.buttergwt.wizard;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

public class PluginUtil
{
  private static final Logger logger = Logger.getLogger(PluginUtil.class.getName());
  
  public static void showErrorMessage(Exception ex)
  {
    logger.log(Level.SEVERE, "error : ", ex);
    logMessage(ExceptionUtils.getStackTrace(ex));
  }
  
  public static void logMessage(String message)
  {
    MessageConsole messageConsole = findConsole("org.akab.buttergwt");
    MessageConsoleStream out = messageConsole.newMessageStream();
    
    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(out);
    try
    {
      outputStreamWriter.write(message);
    }
    catch (IOException localIOException1)
    {
      try
      {
        outputStreamWriter.close();
      }
      catch (IOException localIOException2) {}
    }
  }
  
  public static MessageConsole findConsole(String name)
  {
    ConsolePlugin plugin = ConsolePlugin.getDefault();
    IConsoleManager conMan = plugin.getConsoleManager();
    IConsole[] existing = conMan.getConsoles();
    for (int i = 0; i < existing.length; i++) {
      if (name.equals(existing[i].getName())) {
        return (MessageConsole)existing[i];
      }
    }
    MessageConsole myConsole = new MessageConsole(name, null);
    conMan.addConsoles(new IConsole[] { myConsole });
    return myConsole;
  }
  
  public static void exportPlugin()
  {
    IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
    IProject project = root.getProject(ClassUtil.getSelectdProjectName());
    exportPlugin(project);
  }
  
  public static void exportPlugin(IProject project)
  {
    try
    {
      logMessage(">>> Exporting plugin ..... ");
      
      IFile ifile = project.getFile("jar.xml");
      ifile.getProjectRelativePath();
      File file = new File(ifile.getRawLocationURI());
      
      Project p = new Project();
      p.setUserProperty("ant.file", file.getAbsolutePath());
      p.init();
      ProjectHelper helper = ProjectHelper.getProjectHelper();
      p.addReference("ant.projectHelper", helper);
      helper.parse(p, file);
      p.executeTarget(p.getDefaultTarget());
      
      project.refreshLocal(2, null);
      logMessage(">>> Plugin Export completed. ");
    }
    catch (Exception ex)
    {
      showErrorMessage(ex);
    }
  }
}
