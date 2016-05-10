package org.akab.buttergwt.wizard;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import org.akab.buttergwt.wizard.plugin.PluginSettings;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.internal.ui.dialogs.FilteredTypesSelectionDialog;
import org.eclipse.jdt.internal.ui.packageview.PackageFragmentRootContainer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.internal.Workbench;

public class ClassUtil
{
  public static Shell SHELL = null;
  
  public static ICompilationUnit createCompilationUnitFromTemplate(String compilationUnitName, String templateName, Map<String, String> parameters, String projectName, String sourceFolder, String fullPackageName)
  {
    String template = TemplateUtil.getTemplateString(templateName);
    
    template = TemplateUtil.replaceTemplateParameters(template, parameters);
    IPackageFragmentRoot rootPackage = getRootpackage(projectName, sourceFolder);
    
    ICompilationUnit compilationUnit = null;
    try
    {
      IPackageFragment packageFragment = rootPackage.createPackageFragment(fullPackageName, true, null);
      compilationUnit = packageFragment.createCompilationUnit(compilationUnitName, template, false, null);
      openCompilationUnitInEditor(compilationUnit);
    }
    catch (JavaModelException e)
    {
      e.printStackTrace();
      PluginUtil.showErrorMessage(e);
    }
    return compilationUnit;
  }
  
  public static void openCompilationUnitInEditor(ICompilationUnit compilationUnit) {}
  
  public static ICompilationUnit createCompilationUnit(String compilationUnitName, String source, IPackageFragment packageFragment, boolean forceCreation)
  {
    ICompilationUnit compilationUnit = null;
    try
    {
      packageFragment.createCompilationUnit(compilationUnitName, source, forceCreation, null);
    }
    catch (JavaModelException e)
    {
      e.printStackTrace();
      PluginUtil.showErrorMessage(e);
    }
    return compilationUnit;
  }
  
  public static ICompilationUnit getCompilationUnit(IPackageFragment packageFragment, String name)
  {
    return packageFragment.getCompilationUnit(name);
  }
  
  public static IPackageFragment createPackage(String projectName, String sourceFolderName, String fullPackageName)
    throws JavaModelException
  {
    IPackageFragmentRoot rootPackage = getRootpackage(projectName, sourceFolderName);
    
    IPackageFragment createdpackageFragment = rootPackage.createPackageFragment(fullPackageName, false, null);
    return createdpackageFragment;
  }
  
  public static IPackageFragmentRoot getRootpackage(String projectName, String sourceFolderName)
  {
    IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
    IJavaProject project = JavaCore.create(root.getProject(projectName));
    IFolder sourceFolder = project.getProject().getFolder(sourceFolderName);
    
    IPackageFragmentRoot rootPackageFragment = project.getPackageFragmentRoot(sourceFolder);
    return rootPackageFragment;
  }
  
  public static IJavaProject getJavaProject(String projectName)
  {
    IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
    IJavaProject project = JavaCore.create(root.getProject(projectName));
    
    return project;
  }
  
  public static String getSimpleClassNameFromFullClassName(String fullClassName)
  {
    return fullClassName.substring(fullClassName.lastIndexOf(".") + 1, fullClassName.length());
  }
  
  public static IType browseClass(Shell shell, IWizard wizard, String baseType, String initialFilter, String title)
    throws JavaModelException
  {
    IJavaProject project = getJavaProject(getSelectdProjectName());
    IType superType = project.findType(baseType);
    if (superType != null)
    {
      IJavaSearchScope searchScope = SearchEngine.createStrictHierarchyScope(project, superType, true, false, 
        null);
      
      FilteredTypesSelectionDialog dialog = new FilteredTypesSelectionDialog(shell, false, wizard.getContainer(), 
        searchScope, 5);
      dialog.setTitle(title);
      dialog.setMessage(title);
      dialog.setInitialPattern(initialFilter);
      if (dialog.open() == 0)
      {
        IType type = (IType)dialog.getResult()[0];
        return type;
      }
    }
    return null;
  }
  
  public static IProject getCurrentProject()
  {
    ISelectionService selectionService = Workbench.getInstance().getActiveWorkbenchWindow().getSelectionService();
    
    ISelection selection = selectionService.getSelection();
    
    IProject project = null;
    if ((selection instanceof IStructuredSelection))
    {
      Object element = ((IStructuredSelection)selection).getFirstElement();
      if ((element instanceof IResource))
      {
        project = ((IResource)element).getProject();
      }
      else if ((element instanceof PackageFragmentRootContainer))
      {
        IJavaProject jProject = ((PackageFragmentRootContainer)element).getJavaProject();
        project = jProject.getProject();
      }
      else if ((element instanceof IJavaElement))
      {
        IJavaProject jProject = ((IJavaElement)element).getJavaProject();
        project = jProject.getProject();
      }
    }
    return project;
  }
  
  public static boolean isSelectedProjectHasEngineNature()
  {
    return isProjectHasEngineNature(getCurrentProject());
  }
  
  public static boolean isProjectHasEngineNature(IProject project)
  {
    try
    {
      if (project != null)
      {
        IProjectDescription description = project.getDescription();
        String[] natures = description.getNatureIds();
        String[] arrayOfString1;
        int j = (arrayOfString1 = natures).length;
        for (int i = 0; i < j; i++)
        {
          String nature = arrayOfString1[i];
          if ("org.akab.buttergwt.enginenature".equals(nature)) {
            return true;
          }
        }
      }
      return false;
    }
    catch (CoreException e)
    {
      e.printStackTrace();
      PluginUtil.showErrorMessage(e);
    }
    return false;
  }
  
  public static String getSelectdProjectName()
  {
    IProject project = getCurrentProject();
    if (project != null) {
      return project.getName();
    }
    return "No Project found";
  }
  
  public static ProjectInfo getSelectedProjectInfo()
  {
    IProject project = getCurrentProject();
    if (project != null)
    {
      ProjectInfo projectInfo = new ProjectInfo();
      try
      {
        projectInfo.setName(getSelectdProjectName());
        
        IFile ifile = project.getFile("settings.properties");
        
        Properties p = new Properties();
        p.load(ifile.getContents());
        
        projectInfo.setBasePackage(p.getProperty("base.package"));
        projectInfo.setSubPackage(p.getProperty("sub.package"));
      }
      catch (IOException e)
      {
        e.printStackTrace();
        PluginUtil.showErrorMessage(e);
      }
      catch (CoreException e)
      {
        e.printStackTrace();
        PluginUtil.showErrorMessage(e);
      }
      return projectInfo;
    }
    return null;
  }
  
  public static PluginSettings getPluginSettings()
  {
    IProject project = getCurrentProject();
    PluginSettings pluginSettings = new PluginSettings();
    if (project != null)
    {
      try
      {
        IFile ifile = project.getFile("settings.properties");
        
        Properties p = new Properties();
        p.load(ifile.getContents());
        
        pluginSettings.setGroupId(p.getProperty("groupId"));
        pluginSettings.setArtifactId(p.getProperty("artifactId"));
        pluginSettings.setDescription(p.getProperty("description"));
      }
      catch (IOException e)
      {
        e.printStackTrace();
        PluginUtil.showErrorMessage(e);
      }
      catch (CoreException e)
      {
        e.printStackTrace();
        PluginUtil.showErrorMessage(e);
      }
      return pluginSettings;
    }
    return pluginSettings;
  }
}
