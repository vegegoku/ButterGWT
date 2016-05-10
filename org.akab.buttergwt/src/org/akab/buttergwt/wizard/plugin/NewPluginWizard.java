package org.akab.buttergwt.wizard.plugin;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.akab.buttergwt.wizard.ClassUtil;
import org.akab.buttergwt.wizard.TemplateUtil;
import org.eclipse.core.internal.events.BuildCommand;
import org.eclipse.core.resources.ICommand;
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
import org.eclipse.jdt.core.IAccessRule;
import org.eclipse.jdt.core.IClasspathAttribute;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.ClasspathAttribute;
import org.eclipse.jface.wizard.Wizard;
import org.osgi.framework.Bundle;

public class NewPluginWizard
  extends Wizard
{
  public static final String SRC_MAIN_RESOURCES = "src/main/resources";
  public static final String SRC_MAIN_JAVA = "src/main/java";
  private PluginSettings pluginSettings;
  
  public NewPluginWizard()
  {
    this.pluginSettings = new PluginSettings();
    addPage(new NewPluginPage(this.pluginSettings));
  }
  
  public boolean performFinish()
  {
    try
    {
      IWorkspaceRoot rootWs = ResourcesPlugin.getWorkspace().getRoot();
      IProject project = rootWs.getProject(this.pluginSettings.getArtifactId());
      project.create(null);
      project.open(null);
      
      IProjectDescription description = createProjectDescription(project);
      
      project.setDescription(description, null);
      
      IJavaProject javaProject = JavaCore.create(project);
      
      IFolder sourceFolder = createBaseFolders(project, javaProject);
      
      createClassPath(javaProject, sourceFolder);
      
      createLibs(project);
      
      createBasePackages();
      
      Map<String, String> pomParameters = new HashMap();
      
      pomParameters.put("GROUP_ID", this.pluginSettings.getGroupId());
      pomParameters.put("ARTIFACT_ID", this.pluginSettings.getArtifactId());
      pomParameters.put("NAME", this.pluginSettings.getGroupId() + "-" + this.pluginSettings.getArtifactId());
      pomParameters.put("ACTIVATOR_PACKAGE", 
        this.pluginSettings.getGroupId() + "." + this.pluginSettings.getArtifactId() + ".server.impl");
      pomParameters.put("BASE_PACKAGE", this.pluginSettings.getGroupId() + "." + this.pluginSettings.getArtifactId());
      pomParameters.put("DESCRIPTION", this.pluginSettings.getDescription());
      
      createPomXml(project, pomParameters);
      
      Map<String, String> params = new HashMap();
      
      params.put("NAME", this.pluginSettings.asName());
      params.put("PACKAGE", this.pluginSettings.asPackage());
      params.put("NAME_CONSTANT", this.pluginSettings.asConstant());
      params.put("DESCRIPTION", this.pluginSettings.getDescription());
      params.put("PRIORITY", "(priority=2147483647)");
      params.put("CONTROLLER_IMPORT", this.pluginSettings.asPackage() + ".client.impl.mvp.controller." + 
        this.pluginSettings.asName() + "Controller;");
      params.put("CONTROLLER_CLASS", this.pluginSettings.asName() + "Controller");
      
      createGwtXml(project, params);
      
      createManifest(project, params);
      
      createEntryPoint(project, params);
      createSettingsProperties(project);
      
      createActivator(project, params);
      if (this.pluginSettings.isExtensionsPlugin())
      {
        ExtensionPoint extensionPoint = new ExtensionPoint(this.pluginSettings, project);
        
        extensionPoint.createExtensionPoint(this.pluginSettings.asName());
      }
      else
      {
        createClientBundle();
        createCssResource(project);
        
        createEventsClass(project, params);
        
        createEventItemClass(project, params);
        
        createController(project, params);
        
        Presenter presenter = new Presenter(this.pluginSettings, project);
        presenter.createPresenter(this.pluginSettings.asName(), this.pluginSettings.getDescription());
      }
    }
    catch (CoreException|IOException e)
    {
      e.printStackTrace();
    }
    return true;
  }
  
  private void createClientBundle()
  {
    Map<String, String> params = new HashMap();
    
    params.put("PACKAGE", this.pluginSettings.asPackage());
    params.put("NAME", this.pluginSettings.asName());
    
    String templateName = "plugins/clientBundle.txt";
    
    ClassUtil.createCompilationUnitFromTemplate(this.pluginSettings.asName() + "Bundle.java", templateName, params, 
      this.pluginSettings.getArtifactId(), "src/main/java", this.pluginSettings.asPackage() + ".client.impl.resources");
  }
  
  private void createCssResource(IProject project)
    throws CoreException
  {
    String bundleCssTemplate = TemplateUtil.getTemplateString("plugins/bundle.css");
    
    IFile cssFile = project.getFile("src/main/java/" + this.pluginSettings.asPackagePath() + "/client/impl/resources/" + 
      this.pluginSettings.asName() + ".css");
    
    InputStream cssSource = new ByteArrayInputStream(bundleCssTemplate.getBytes());
    cssFile.create(cssSource, true, null);
  }
  
  private void createBasePackages()
    throws JavaModelException
  {
    createSourcePackage(this.pluginSettings.getArtifactId(), this.pluginSettings.asPackage());
    createSourcePackage(this.pluginSettings.getArtifactId(), this.pluginSettings.asPackage() + ".client");
    createSourcePackage(this.pluginSettings.getArtifactId(), this.pluginSettings.asPackage() + ".client.api");
    createSourcePackage(this.pluginSettings.getArtifactId(), this.pluginSettings.asPackage() + ".shared");
    createSourcePackage(this.pluginSettings.getArtifactId(), this.pluginSettings.asPackage() + ".server");
    
    createSourcePackage(this.pluginSettings.getArtifactId(), this.pluginSettings.asPackage() + ".shared.api");
    createSourcePackage(this.pluginSettings.getArtifactId(), this.pluginSettings.asPackage() + ".client.impl");
    createSourcePackage(this.pluginSettings.getArtifactId(), this.pluginSettings.asPackage() + ".server.impl");
    if (this.pluginSettings.isExtensionsPlugin())
    {
      createSourcePackage(this.pluginSettings.getArtifactId(), this.pluginSettings.asPackage() + ".client.api.context");
      createSourcePackage(this.pluginSettings.getArtifactId(), this.pluginSettings.asPackage() + ".client.api.extension");
    }
    else
    {
      createSourcePackage(this.pluginSettings.getArtifactId(), this.pluginSettings.asPackage() + ".client.api.mvp");
      createSourcePackage(this.pluginSettings.getArtifactId(), this.pluginSettings.asPackage() + ".client.api.mvp.view");
      createSourcePackage(this.pluginSettings.getArtifactId(), this.pluginSettings.asPackage() + ".client.api.mvp.presenter");
      
      createSourcePackage(this.pluginSettings.getArtifactId(), this.pluginSettings.asPackage() + ".client.impl.events");
      createSourcePackage(this.pluginSettings.getArtifactId(), this.pluginSettings.asPackage() + ".client.impl.request");
      createSourcePackage(this.pluginSettings.getArtifactId(), 
        this.pluginSettings.asPackage() + ".client.impl.contribution");
      createSourcePackage(this.pluginSettings.getArtifactId(), this.pluginSettings.asPackage() + ".client.impl.mvp");
      createSourcePackage(this.pluginSettings.getArtifactId(), 
        this.pluginSettings.asPackage() + ".client.impl.mvp.controller");
      createSourcePackage(this.pluginSettings.getArtifactId(), 
        this.pluginSettings.asPackage() + ".client.impl.mvp.presenter");
      createSourcePackage(this.pluginSettings.getArtifactId(), this.pluginSettings.asPackage() + ".client.impl.mvp.view");
      createSourcePackage(this.pluginSettings.getArtifactId(), this.pluginSettings.asPackage() + ".client.impl.mvp.view");
      createSourcePackage(this.pluginSettings.getArtifactId(), this.pluginSettings.asPackage() + ".client.impl.resources");
      
      createSourcePackage(this.pluginSettings.getArtifactId(), this.pluginSettings.asPackage() + ".server.api");
      createSourcePackage(this.pluginSettings.getArtifactId(), this.pluginSettings.asPackage() + ".server.impl.request");
      createSourcePackage(this.pluginSettings.getArtifactId(), this.pluginSettings.asPackage() + "server.impl.response");
      createSourcePackage(this.pluginSettings.getArtifactId(), 
        this.pluginSettings.asPackage() + ".server.impl.request.handler");
      createSourcePackage(this.pluginSettings.getArtifactId(), 
        this.pluginSettings.asPackage() + ".server.impl.request.download");
      createSourcePackage(this.pluginSettings.getArtifactId(), 
        this.pluginSettings.asPackage() + ".server.impl.request.upload");
      createSourcePackage(this.pluginSettings.getArtifactId(), this.pluginSettings.asPackage() + ".shared.api.request");
      createSourcePackage(this.pluginSettings.getArtifactId(), this.pluginSettings.asPackage() + ".shared.api.response");
      createSourcePackage(this.pluginSettings.getArtifactId(), this.pluginSettings.asPackage() + ".shared.impl");
    }
  }
  
  private IPackageFragment createSourcePackage(String projectName, String packageName)
    throws JavaModelException
  {
    return ClassUtil.createPackage(projectName, "src/main/java", packageName);
  }
  
  private IProjectDescription createProjectDescription(IProject project)
    throws CoreException
  {
    IProjectDescription description = project.getDescription();
    
    description.setNatureIds(new String[] { "org.eclipse.jdt.core.javanature", 
      "com.google.gwt.eclipse.core.gwtNature", "org.eclipse.wst.common.project.facet.core.nature", 
      "org.akab.buttergwt.enginenature", "org.eclipse.pde.PluginNature", 
      "org.eclipse.wst.common.modulecore.ModuleCoreNature", "org.eclipse.m2e.core.maven2Nature" });
    
    ICommand webAppProjectValidatorCommand = new BuildCommand();
    webAppProjectValidatorCommand.setBuilderName("com.google.gdt.eclipse.core.webAppProjectValidator");
    
    ICommand gwtProjectValidatorCommand = new BuildCommand();
    gwtProjectValidatorCommand.setBuilderName("com.google.gwt.eclipse.core.gwtProjectValidator");
    
    ICommand manifestBuilderCommand = new BuildCommand();
    manifestBuilderCommand.setBuilderName("org.eclipse.pde.ManifestBuilder");
    
    ICommand schemaBuilderCommand = new BuildCommand();
    schemaBuilderCommand.setBuilderName("org.eclipse.pde.SchemaBuilder");
    
    ICommand javascriptValidatorCommand = new BuildCommand();
    javascriptValidatorCommand.setBuilderName("org.eclipse.wst.jsdt.core.javascriptValidator");
    
    ICommand javabuilderCommand = new BuildCommand();
    javabuilderCommand.setBuilderName("org.eclipse.jdt.core.javabuilder");
    
    ICommand facetCoreBuilderCommand = new BuildCommand();
    facetCoreBuilderCommand.setBuilderName("org.eclipse.wst.common.project.facet.core.builder");
    
    ICommand validationbuilderCommand = new BuildCommand();
    validationbuilderCommand.setBuilderName("org.eclipse.wst.validation.validationbuilder");
    
    ICommand maven2BuilderCommand = new BuildCommand();
    maven2BuilderCommand.setBuilderName("org.eclipse.m2e.core.maven2Builder");
    
    description.setBuildSpec(new ICommand[] { webAppProjectValidatorCommand, gwtProjectValidatorCommand, 
      manifestBuilderCommand, schemaBuilderCommand, javascriptValidatorCommand, javabuilderCommand, 
      facetCoreBuilderCommand, validationbuilderCommand, maven2BuilderCommand });
    return description;
  }
  
  private IFolder createBaseFolders(IProject project, IJavaProject javaProject)
    throws CoreException, JavaModelException
  {
    IFolder sourceFolder = createFolder(project, "src");
    createFolder(project, "src/main");
    createFolder(project, "src/main/java");
    createFolder(project, "src/main/resources");
    createFolder(project, "src/main/webapp");
    createFolder(project, "src/main/webapp/WEB-INF");
    createFolder(project, "src/main/webapp/lib");
    createFolder(project, "src/test");
    createFolder(project, "src/test/java");
    createFolder(project, "src/test/resources");
    createFolder(project, "target");
    createFolder(project, "META-INF");
    IFolder binFolder = createFolder(project, "target/classes");
    
    javaProject.setOutputLocation(binFolder.getFullPath(), null);
    return sourceFolder;
  }
  
  private IFolder createFolder(IProject project, String folderPath)
    throws CoreException
  {
    IFolder folder = project.getFolder(folderPath);
    folder.create(false, true, null);
    return folder;
  }
  
  private void createClassPath(IJavaProject javaProject, IFolder sourceFolder)
    throws JavaModelException
  {
    List<IClasspathEntry> entries = new ArrayList();
    
    IClasspathEntry sourceMainJavaEntry = JavaCore.newSourceEntry(getFullPath(javaProject, "src/main/java"), 
      new IPath[] { new Path("**/*.java") }, new IPath[0], getFullPath(javaProject, "target/classes"), 
      new IClasspathAttribute[] { new ClasspathAttribute("optional", "true"), 
      new ClasspathAttribute("maven.pomderived", "true") });
    
    IClasspathEntry sourceResourcesEntry = JavaCore.newSourceEntry(getFullPath(javaProject, "src/main/resources"), 
      new IPath[] { new Path("**/*.java") }, new IPath[] { new Path("**") }, 
      getFullPath(javaProject, "target/classes"), 
      new IClasspathAttribute[] { new ClasspathAttribute("maven.pomderived", "true") });
    
    IClasspathEntry sourceTestJavaEntry = JavaCore.newSourceEntry(getFullPath(javaProject, "src/test/java"), 
      new IPath[0], new IPath[0], getFullPath(javaProject, "target/test-classes"), 
      new IClasspathAttribute[] { new ClasspathAttribute("optional", "true"), 
      new ClasspathAttribute("maven.pomderived", "true") });
    
    IClasspathEntry jreContainerJavaEntry = JavaCore.newContainerEntry(
      new Path(
      "org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/JavaSE-1.7"), 
      new IAccessRule[0], new IClasspathAttribute[] { new ClasspathAttribute("maven.pomderived", "true") }, 
      false);
    
    IClasspathEntry mavenClassPathContainerJavaEntry = JavaCore.newContainerEntry(
      new Path("org.eclipse.m2e.MAVEN2_CLASSPATH_CONTAINER"), new IAccessRule[0], 
      new IClasspathAttribute[] { new ClasspathAttribute("maven.pomderived", "true") }, false);
    
    IClasspathEntry iClasspathEntryGwt = 
      JavaCore.newContainerEntry(new Path("com.google.gwt.eclipse.core.GWT_CONTAINER"));
    IClasspathEntry iClasspathEntryPlugins = 
      JavaCore.newContainerEntry(new Path("org.eclipse.pde.core.requiredPlugins"));
    
    entries.add(sourceMainJavaEntry);
    entries.add(sourceResourcesEntry);
    entries.add(sourceTestJavaEntry);
    entries.add(jreContainerJavaEntry);
    entries.add(mavenClassPathContainerJavaEntry);
    entries.add(iClasspathEntryGwt);
    entries.add(iClasspathEntryPlugins);
    
    javaProject.setRawClasspath((IClasspathEntry[])entries.toArray(new IClasspathEntry[entries.size()]), null);
  }
  
  private IPath getFullPath(IJavaProject javaProject, String folderPath)
  {
    return javaProject.getProject().getFolder(folderPath).getFullPath();
  }
  
  private void createLibs(IProject project)
    throws IOException, CoreException
  {
    Bundle bundle = Platform.getBundle("org.akab.buttergwt");
    addFile(project, bundle, "resources/extendedResources/", "web.xml", "src/main/webapp/WEB-INF/");
  }
  
  private void createPomXml(IProject project, Map<String, String> pomParameters)
    throws CoreException
  {
    String templateName = this.pluginSettings.isExtensionsPlugin() ? "plugins/extensionsPom.xml" : "plugins/pom.xml";
    String pomTemplate = TemplateUtil.getTemplateString(templateName);
    String pomTemplateString = TemplateUtil.replaceTemplateParameters(pomTemplate, pomParameters);
    
    IFile pomFile = project.getFile("pom.xml");
    
    InputStream pomSource = new ByteArrayInputStream(pomTemplateString.getBytes());
    pomFile.create(pomSource, true, null);
  }
  
  private void createGwtXml(IProject project, Map<String, String> pluginXmlParameters)
    throws CoreException
  {
    String pluginXmlTemplate = TemplateUtil.getTemplateString("plugins/plugin.gwt.xml");
    String pluginXmlString = TemplateUtil.replaceTemplateParameters(pluginXmlTemplate, pluginXmlParameters);
    
    IFile pluginXmlFile = project.getFile("src/main/resources/" + this.pluginSettings.asPackagePath() + "/client/" + 
      this.pluginSettings.asName() + ".gwt.xml");
    
    InputStream pluginXmlSource = new ByteArrayInputStream(pluginXmlString.getBytes());
    pluginXmlFile.create(pluginXmlSource, true, null);
  }
  
  private void createManifest(IProject project, Map<String, String> params)
    throws CoreException
  {
    String manifestTemplate = TemplateUtil.getTemplateString("plugins/MANIFEST.MF");
    String manifestString = TemplateUtil.replaceTemplateParameters(manifestTemplate, params);
    
    IFile manifestFile = project.getFile("META-INF/MANIFEST.MF");
    
    InputStream manifestSource = new ByteArrayInputStream(manifestString.getBytes());
    manifestFile.create(manifestSource, true, null);
  }
  
  private void createEntryPoint(IProject project, Map<String, String> pluginXmlParameters)
    throws CoreException
  {
    String templateName = this.pluginSettings.isExtensionsPlugin() ? "plugins/extensionsEntrypoint.txt" : "plugins/moduleEntrypoint.txt";
    String pluginentryPointTemplate = TemplateUtil.getTemplateString(templateName);
    String pluginentryPointString = TemplateUtil.replaceTemplateParameters(pluginentryPointTemplate, 
      pluginXmlParameters);
    
    IFile pluginentryPointFile = project.getFile("src/main/java/" + this.pluginSettings.asPackagePath() + 
      "/client/impl/" + this.pluginSettings.asName() + ".java");
    
    InputStream pluginentryPointSource = new ByteArrayInputStream(pluginentryPointString.getBytes());
    pluginentryPointFile.create(pluginentryPointSource, true, null);
  }
  
  private void createActivator(IProject project, Map<String, String> params)
    throws CoreException
  {
    String template = TemplateUtil.getTemplateString("plugins/activator.txt");
    String templateString = TemplateUtil.replaceTemplateParameters(template, 
      params);
    
    IFile file = project.getFile("src/main/java/" + this.pluginSettings.asPackagePath() + 
      "/server/impl/" + "Activator.java");
    
    InputStream source = new ByteArrayInputStream(templateString.getBytes());
    file.create(source, true, null);
  }
  
  private void createEventsClass(IProject project, Map<String, String> eventsParameters)
    throws CoreException
  {
    String eventsTemplate = TemplateUtil.getTemplateString("controller/eventsTemplate.txt");
    String eventsString = TemplateUtil.replaceTemplateParameters(eventsTemplate, eventsParameters);
    
    IFile eventsFile = project
      .getFile("src/main/java/" + this.pluginSettings.asPackagePath() + "/client/impl/events/" + "Events.java");
    
    InputStream eventsSource = new ByteArrayInputStream(eventsString.getBytes());
    eventsFile.create(eventsSource, true, null);
  }
  
  private void createEventItemClass(IProject project, Map<String, String> params)
    throws CoreException
  {
    params.put("TEMPLATE_NAME", "controller/controllerEventTemplate.txt");
    params.put("PACKAGE_PATH", "/client/impl/events/");
    params.put("FILE_POSTFIX", "ControllerEvent.java");
    
    TemplateUtil.createCompilationUnitFromTemplate(project, this.pluginSettings, params);
  }
  
  private void createController(IProject project, Map<String, String> params)
    throws CoreException
  {
    params.put("TEMPLATE_NAME", "controller/controllerTemplate.txt");
    params.put("PACKAGE_PATH", "/client/impl/mvp/controller/");
    params.put("FILE_POSTFIX", "Controller.java");
    
    TemplateUtil.createCompilationUnitFromTemplate(project, this.pluginSettings, params);
  }
  
  private void addFile(IProject project, Bundle bundle, String sourceFolder, String fileName, String destinationPath)
    throws IOException, CoreException
  {
    InputStream stream = FileLocator.openStream(bundle, new Path(sourceFolder + fileName), false);
    IFile file = project.getFile(destinationPath + fileName);
    file.create(stream, true, null);
  }
  
  private void createSettingsProperties(IProject project)
    throws CoreException
  {
    IFile clientConfigFile = project.getFile("settings.properties");
    StringBuffer csb = new StringBuffer();
    csb.append("groupId=" + this.pluginSettings.getGroupId() + "\n");
    csb.append("artifactId=" + this.pluginSettings.getArtifactId() + "\n");
    csb.append("description=" + this.pluginSettings.getDescription());
    
    InputStream configsource = new ByteArrayInputStream(csb.toString().getBytes());
    clientConfigFile.create(configsource, true, null);
  }
}
