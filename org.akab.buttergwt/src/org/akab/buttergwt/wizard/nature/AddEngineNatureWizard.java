package org.akab.buttergwt.wizard.nature;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.akab.buttergwt.wizard.ClassUtil;
import org.akab.buttergwt.wizard.PluginUtil;
import org.akab.buttergwt.wizard.ProjectInfo;
import org.akab.buttergwt.wizard.TemplateUtil;
import org.akab.buttergwt.wizard.UnzipUtility;
import org.akab.buttergwt.wizard.controller.ControllerCreator;
import org.akab.buttergwt.wizard.controller.NewControllerItem;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.wizard.Wizard;
import org.osgi.framework.Bundle;

public class AddEngineNatureWizard
  extends Wizard
{
  private ProjectInfo projectInfo;
  
  public AddEngineNatureWizard()
  {
    this.projectInfo = ClassUtil.getSelectedProjectInfo();
    addPage(new AddEngineNatureWizardPage("Convert to engine project", this.projectInfo));
  }
  
  public boolean performFinish()
  {
    try
    {
      IProject project = ClassUtil.getCurrentProject();
      
      updateProjectNature(project);
      
      createEngineProperties(project);
      
      createClientConfigProperties(project);
      
      updateGwtXml(project);
      updateWebXml(project);
      
      updateLibs(project);
      if (this.projectInfo.isGenerateSampleCode()) {
        generateSampleCode(project);
      }
      project.refreshLocal(2, null);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      PluginUtil.showErrorMessage(ex);
    }
    return true;
  }
  
  private void generateSampleCode(IProject project)
    throws CoreException, IOException
  {
    generateSampleHtmlPage(project);
    addResources(project);
    createSampleController();
    createShowWelcomeSampleRequest();
    createSayHelloSampleRequest();
    createSamplePresenter();
    createSampleViewInterface();
    createSampleViewImpl();
    createSampleServerHandler();
    createSampleRequestArgs();
    createSampleResponse();
  }
  
  private void createSampleResponse()
  {
    String packageName = this.projectInfo.getBasePackage() + ".shared." + this.projectInfo.getSubPackage() + 
      ".response.welcome";
    
    Map<String, String> parameters = new HashMap();
    parameters.put("BASE_PACKAGE", this.projectInfo.getBasePackage());
    parameters.put("SUB_PACKAGE", this.projectInfo.getSubPackage());
    
    ClassUtil.createCompilationUnitFromTemplate("SayHelloResponse.java", "sample/sampleResponseTemplate.txt", 
      parameters, this.projectInfo.getName(), "src", packageName);
  }
  
  private void createSampleRequestArgs()
  {
    String packageName = this.projectInfo.getBasePackage() + ".shared." + this.projectInfo.getSubPackage() + 
      ".request.welcome";
    
    Map<String, String> parameters = new HashMap();
    parameters.put("BASE_PACKAGE", this.projectInfo.getBasePackage());
    parameters.put("SUB_PACKAGE", this.projectInfo.getSubPackage());
    
    ClassUtil.createCompilationUnitFromTemplate("SayHelloRequestArgs.java", "sample/sampleRequestArgsTemplate.txt", 
      parameters, this.projectInfo.getName(), "src", packageName);
  }
  
  private void createSampleServerHandler()
  {
    String packageName = this.projectInfo.getBasePackage() + ".server." + this.projectInfo.getSubPackage() + 
      ".request.handler.welcome";
    
    Map<String, String> parameters = new HashMap();
    parameters.put("BASE_PACKAGE", this.projectInfo.getBasePackage());
    parameters.put("SUB_PACKAGE", this.projectInfo.getSubPackage());
    
    ClassUtil.createCompilationUnitFromTemplate("SayHelloServerHanlder.java", 
      "sample/sampleRequestServerHandlerTemplate.txt", parameters, this.projectInfo.getName(), "src", packageName);
  }
  
  private void createSampleViewImpl()
  {
    String packageName = this.projectInfo.getBasePackage() + ".client." + this.projectInfo.getSubPackage() + 
      ".mvp.view.welcome";
    
    Map<String, String> parameters = new HashMap();
    parameters.put("BASE_PACKAGE", this.projectInfo.getBasePackage());
    parameters.put("SUB_PACKAGE", this.projectInfo.getSubPackage());
    
    ClassUtil.createCompilationUnitFromTemplate("WelcomePanelViewImpl.java", "sample/sampleViewImplTemplate.txt", 
      parameters, this.projectInfo.getName(), "src", packageName);
  }
  
  private void createSampleViewInterface()
  {
    String packageName = this.projectInfo.getBasePackage() + ".client." + this.projectInfo.getSubPackage() + 
      ".mvp.view.welcome";
    
    Map<String, String> parameters = new HashMap();
    parameters.put("BASE_PACKAGE", this.projectInfo.getBasePackage());
    parameters.put("SUB_PACKAGE", this.projectInfo.getSubPackage());
    
    ClassUtil.createCompilationUnitFromTemplate("WelcomePanelView.java", "sample/sampleViewInterfaceTemplate.txt", 
      parameters, this.projectInfo.getName(), "src", packageName);
  }
  
  private void createSamplePresenter()
  {
    String packageName = this.projectInfo.getBasePackage() + ".client." + this.projectInfo.getSubPackage() + 
      ".mvp.presenter.welcome";
    
    Map<String, String> parameters = new HashMap();
    parameters.put("BASE_PACKAGE", this.projectInfo.getBasePackage());
    parameters.put("SUB_PACKAGE", this.projectInfo.getSubPackage());
    
    ClassUtil.createCompilationUnitFromTemplate("WelcomePresenter.java", "sample/samplePrsenterTemplate.txt", 
      parameters, this.projectInfo.getName(), "src", packageName);
    
    ClassUtil.createCompilationUnitFromTemplate("WelcomeContributionContext.java", 
      "sample/sampleContributionContextTemplate.txt", parameters, this.projectInfo.getName(), "src", packageName);
    
    ClassUtil.createCompilationUnitFromTemplate("WelcomeExtensionPoint.java", 
      "sample/sampleExtensionPointTemplate.txt", parameters, this.projectInfo.getName(), "src", packageName);
  }
  
  private void createSayHelloSampleRequest()
  {
    String packageName = this.projectInfo.getBasePackage() + ".client." + this.projectInfo.getSubPackage() + 
      ".request.welcome";
    
    Map<String, String> parameters = new HashMap();
    parameters.put("BASE_PACKAGE", this.projectInfo.getBasePackage());
    parameters.put("SUB_PACKAGE", this.projectInfo.getSubPackage());
    
    ClassUtil.createCompilationUnitFromTemplate("SayHelloServerRequest.java", 
      "sample/sayHelloSampleRequestTemplate.txt", parameters, this.projectInfo.getName(), "src", packageName);
  }
  
  private void createShowWelcomeSampleRequest()
  {
    String packageName = this.projectInfo.getBasePackage() + ".client." + this.projectInfo.getSubPackage() + 
      ".request.welcome";
    
    Map<String, String> parameters = new HashMap();
    parameters.put("BASE_PACKAGE", this.projectInfo.getBasePackage());
    parameters.put("SUB_PACKAGE", this.projectInfo.getSubPackage());
    
    ClassUtil.createCompilationUnitFromTemplate("ShowNameInputEmptyClientRequest.java", 
      "sample/showWelcomeSampleRequest.txt", parameters, this.projectInfo.getName(), "src", packageName);
  }
  
  private void createSampleController()
  {
    NewControllerItem newControllerItem = new NewControllerItem();
    newControllerItem.setControllerName("Welcome");
    newControllerItem.setControllerPackage(".welcome");
    newControllerItem.setControllerConstantName("WELCOME");
    newControllerItem.setPriority("1");
    newControllerItem.setControllerDescription("Sample welcome controller");
    newControllerItem.setHandlesHistory(true);
    newControllerItem.setExtraImports("import " + this.projectInfo.getBasePackage() + ".client." + 
      this.projectInfo.getSubPackage() + ".request.welcome.ShowNameInputEmptyClientRequest;");
    newControllerItem.setInitialContent(
      "ShowNameInputEmptyClientRequest showNameInputEmptyClientRequest=new ShowNameInputEmptyClientRequest();\nshowNameInputEmptyClientRequest.send();");
    
    ProjectInfo projectInfo = ClassUtil.getSelectedProjectInfo();
    ControllerCreator.createController(newControllerItem, projectInfo);
  }
  
  private void addResources(IProject project)
    throws IOException, CoreException
  {
    Bundle bundle = Platform.getBundle("org.akab.buttergwt");
    
    InputStream stream = FileLocator.openStream(bundle, new Path("resources/extendedResources/resources.zip"), 
      false);
    IFolder folder = project.getFolder("war");
    
    String path = folder.getRawLocation().toFile().getAbsolutePath();
    
    UnzipUtility.unzip(stream, path);
  }
  
  private void generateSampleHtmlPage(IProject project)
    throws CoreException
  {
    Map<String, String> paramsMap = new HashMap();
    paramsMap.put("appTitle", this.projectInfo.getName());
    paramsMap.put("appModule", this.projectInfo.getName().toLowerCase());
    
    String pageTemplate = TemplateUtil.getTemplateString("sample/sampleHtmlTemplate.txt");
    String templateResult = TemplateUtil.replaceTemplateParameters(pageTemplate, paramsMap);
    
    IFile samplePageFile = project.getFile("war/SamplePage.html");
    
    InputStream pageSource = new ByteArrayInputStream(templateResult.getBytes());
    samplePageFile.create(pageSource, true, null);
  }
  
  private void updateLibs(IProject project)
    throws IOException, CoreException
  {
    Bundle bundle = Platform.getBundle("org.akab.buttergwt");
    
    IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
    IJavaProject javaProject = JavaCore.create(root.getProject(this.projectInfo.getName()));
    
    IClasspathEntry[] entries = javaProject.getRawClasspath();
    
    String baseLibsPath = "war/WEB-INF/lib/";
    
    IClasspathEntry aopallianceCp = addJar(project, bundle, "aopalliance.jar", baseLibsPath);
    IClasspathEntry bo3CoreCp = addJar(project, bundle, "bo3-core.jar", baseLibsPath);
    IClasspathEntry bo3DbqCp = addJar(project, bundle, "bo3-dbq.jar", baseLibsPath);
    IClasspathEntry bo3ModelCp = addJar(project, bundle, "bo3-model.jar", baseLibsPath);
    IClasspathEntry bo3UtilsCp = addJar(project, bundle, "bo3-utils.jar", baseLibsPath);
    IClasspathEntry bo3WidgetsCp = addJar(project, bundle, "bo3-widgets.jar", baseLibsPath);
    IClasspathEntry ginCp = addJar(project, bundle, "gin-1.5-post-gwt-2.2.jar", baseLibsPath);
    IClasspathEntry collectionsCp = addJar(project, bundle, "google-collections-1.0-rc2.jar", baseLibsPath);
    IClasspathEntry assistedinjectCp = addJar(project, bundle, "guice-assistedinject-snapshot.jar", baseLibsPath);
    IClasspathEntry snapshotCp = addJar(project, bundle, "guice-snapshot.jar", baseLibsPath);
    IClasspathEntry dndCp = addJar(project, bundle, "gwt-dnd-3.3.0.jar", baseLibsPath);
    IClasspathEntry injectCp = addJar(project, bundle, "javax.inject.jar", baseLibsPath);
    IClasspathEntry accessCp = addJar(project, bundle, "logback-access-0.9.29.jar", baseLibsPath);
    IClasspathEntry classicCp = addJar(project, bundle, "logback-classic-0.9.29.jar", baseLibsPath);
    IClasspathEntry logbackCp = addJar(project, bundle, "logback-core-0.9.29.jar", baseLibsPath);
    IClasspathEntry slf4jCp = addJar(project, bundle, "slf4j-api-1.7.5.jar", baseLibsPath);
    
    IClasspathEntry[] newEntries = new IClasspathEntry[entries.length + 16];
    
    System.arraycopy(entries, 0, newEntries, 0, entries.length);
    
    newEntries[entries.length] = aopallianceCp;
    newEntries[(entries.length + 1)] = bo3CoreCp;
    newEntries[(entries.length + 2)] = bo3DbqCp;
    newEntries[(entries.length + 3)] = bo3ModelCp;
    newEntries[(entries.length + 4)] = bo3UtilsCp;
    newEntries[(entries.length + 5)] = bo3WidgetsCp;
    newEntries[(entries.length + 6)] = ginCp;
    newEntries[(entries.length + 7)] = collectionsCp;
    newEntries[(entries.length + 8)] = assistedinjectCp;
    newEntries[(entries.length + 9)] = snapshotCp;
    newEntries[(entries.length + 10)] = dndCp;
    newEntries[(entries.length + 11)] = injectCp;
    newEntries[(entries.length + 12)] = accessCp;
    newEntries[(entries.length + 13)] = classicCp;
    newEntries[(entries.length + 14)] = logbackCp;
    newEntries[(entries.length + 15)] = slf4jCp;
    
    javaProject.setRawClasspath(newEntries, null);
  }
  
  private IClasspathEntry addJar(IProject project, Bundle bundle, String jarName, String baseLibsPath)
    throws IOException, CoreException
  {
    InputStream stream = FileLocator.openStream(bundle, new Path("resources/dependencies/" + jarName), false);
    IFile file = project.getFile(baseLibsPath + jarName);
    file.create(stream, true, null);
    return JavaCore.newLibraryEntry(file.getFullPath(), null, null);
  }
  
  private void updateGwtXml(IProject project)
    throws FileNotFoundException, IOException, UnsupportedEncodingException, CoreException
  {
    IFile gwtXmlFile = project.getFile(
      "src/" + this.projectInfo.getBasePackage().replaceAll("\\.", "\\/") + "/" + project.getName() + ".gwt.xml");
    if (gwtXmlFile.exists())
    {
      IPath path = gwtXmlFile.getRawLocation();
      File file = path.toFile();
      
      FileInputStream fis = new FileInputStream(file);
      
      byte[] fileBytes = new byte[(int)file.length()];
      fis.read(fileBytes);
      String fileContent = new String(fileBytes, "UTF8");
      String iharitence = "\n<inherits name=\"akab.bo3.dqb.query.DynamicQueryBuilder\" />\n<inherits name=\"org.akab.engine.widgets.Widgets\"/>\n<inherits name=\"org.akab.engine.Core\"/>\n\n<inherits name=\"com.google.gwt.i18n.I18N\" />\n<inherits name=\"com.google.gwt.inject.Inject\" />\n<inherits name=\"com.google.gwt.xml.XML\" />\n</module>";
      
      fileContent = fileContent.replace("</module>", iharitence);
      FileOutputStream fos = new FileOutputStream(file);
      fos.write(fileContent.getBytes("UTF8"));
      fos.flush();
      fos.close();
      
      fis.close();
    }
    else
    {
      ClassUtil.createPackage(this.projectInfo.getName(), "src", this.projectInfo.getBasePackage() + ".server");
      ClassUtil.createPackage(this.projectInfo.getName(), "src", this.projectInfo.getBasePackage() + ".shared");
      ClassUtil.createPackage(this.projectInfo.getName(), "src", this.projectInfo.getBasePackage() + ".client");
      
      Map<String, String> paramsMap = new HashMap();
      paramsMap.put("appName", this.projectInfo.getName());
      paramsMap.put("appModule", this.projectInfo.getName().toLowerCase());
      paramsMap.put("BASE_PACKAGE", this.projectInfo.getBasePackage());
      
      String pageTemplate = TemplateUtil.getTemplateString("app/gwtXmlTemplate.txt");
      String templateResult = TemplateUtil.replaceTemplateParameters(pageTemplate, paramsMap);
      
      InputStream pageSource = new ByteArrayInputStream(templateResult.getBytes());
      gwtXmlFile.create(pageSource, true, null);
      
      Map<String, String> entryPointParams = new HashMap();
      entryPointParams.put("BASE_PACKAGE", this.projectInfo.getBasePackage());
      entryPointParams.put("APP_NAME", this.projectInfo.getName());
      
      String entryPointTemplate = TemplateUtil.getTemplateString("app/appEntryPointTemplate.txt");
      String entryPointTemplateResult = TemplateUtil.replaceTemplateParameters(entryPointTemplate, 
        entryPointParams);
      
      InputStream entryPointSurce = new ByteArrayInputStream(entryPointTemplateResult.getBytes());
      
      IFile entryPointFile = project.getFile("src/" + this.projectInfo.getBasePackage().replaceAll("\\.", "\\/") + "/" + 
        "/client/" + project.getName() + ".java");
      
      entryPointFile.create(entryPointSurce, true, null);
    }
  }
  
  private void updateWebXml(IProject project)
    throws FileNotFoundException, IOException, UnsupportedEncodingException
  {
    IFile webXmlFile = project.getFile("war/WEB-INF/web.xml");
    IPath path = webXmlFile.getRawLocation();
    File file = path.toFile();
    FileInputStream fis = new FileInputStream(file);
    
    byte[] fileBytes = new byte[(int)file.length()];
    fis.read(fileBytes);
    String fileContent = new String(fileBytes, "UTF8");
    
    String newContent = "\n<context-param>\n<param-name>BASE_SCAN_PACKAGE</param-name>\n<param-value>" + 
      this.projectInfo.getBasePackage() + "</param-value>\n" + "</context-param>\n" + "\n<listener>" + 
      "\n<listener-class>" + "\norg.akab.engine.server.ReflectionWireListener" + "\n</listener-class>" + 
      "\n</listener>\n" + 
      
      "\n<servlet>" + "\n<servlet-name>AppService</servlet-name>" + 
      "\n<servlet-class>org.akab.engine.server.AppServiceImpl</servlet-class>" + "\n</servlet>" + 
      
      "\n<servlet-mapping>" + "\n<servlet-name>AppService</servlet-name>" + "\n<url-pattern>/" + 
      this.projectInfo.getName().toLowerCase() + "/appService.rpc</url-pattern>" + "\n</servlet-mapping>" + 
      
      "\n<servlet>" + "\n<description></description>" + "\n<display-name>downloadPath</display-name>" + 
      "\n<servlet-name>downloadPath</servlet-name>" + 
      "\n<servlet-class>org.akab.engine.server.core.servlet.DownloadServlet</servlet-class>" + 
      "\n</servlet>" + "\n<servlet-mapping>" + "\n<servlet-name>downloadPath</servlet-name>" + 
      "\n<url-pattern>/" + this.projectInfo.getName().toLowerCase() + "/downloadPath/*</url-pattern>" + 
      "\n</servlet-mapping>" + 
      
      "\n<servlet>" + "\n<description></description>" + "\n<display-name>uploadPath</display-name>" + 
      "\n<servlet-name>uploadPath</servlet-name>" + 
      "\n<servlet-class>org.akab.engine.server.core.servlet.UploadServlet</servlet-class>" + "\n</servlet>" + 
      "\n<servlet-mapping>" + "\n<servlet-name>uploadPath</servlet-name>" + "\n<url-pattern>/" + 
      this.projectInfo.getName().toLowerCase() + "/uploadPath/*</url-pattern>" + "\n</servlet-mapping>";
    
    fileContent = updateWebXmlContent(fileContent, newContent, "\n</web-app>");
    
    FileOutputStream fos = new FileOutputStream(file);
    fos.write(fileContent.getBytes("UTF8"));
    fos.flush();
    fos.close();
    
    fis.close();
  }
  
  private String updateWebXmlContent(String fileContent, String contentToAppend, String before)
  {
    return 
      fileContent.substring(0, fileContent.lastIndexOf(before)) + contentToAppend + fileContent.substring(fileContent.lastIndexOf(before), fileContent.lastIndexOf(before) + before.length());
  }
  
  private void createClientConfigProperties(IProject project)
    throws CoreException
  {
    IFile clientConfigFile = project.getFile("src/clientConfig.properties");
    StringBuffer csb = new StringBuffer();
    csb.append("client.package=" + this.projectInfo.getBasePackage() + ".client");
    
    InputStream configsource = new ByteArrayInputStream(csb.toString().getBytes());
    clientConfigFile.create(configsource, true, null);
  }
  
  private void createEngineProperties(IProject project)
    throws CoreException
  {
    IFile engineProjectFile = project.getFile("engineproject.properties");
    StringBuffer esb = new StringBuffer();
    
    esb.append("base.package=" + this.projectInfo.getBasePackage() + "\n");
    esb.append("sub.package=" + this.projectInfo.getSubPackage());
    
    InputStream source = new ByteArrayInputStream(esb.toString().getBytes());
    engineProjectFile.create(source, true, null);
  }
  
  private void updateProjectNature(IProject project)
    throws CoreException
  {
    IProjectDescription description = project.getDescription();
    String[] natures = description.getNatureIds();
    String[] newNatures = new String[natures.length + 1];
    System.arraycopy(natures, 0, newNatures, 0, natures.length);
    newNatures[natures.length] = "org.akab.buttergwt.enginenature";
    
    description.setNatureIds(newNatures);
    project.setDescription(description, null);
  }
}
