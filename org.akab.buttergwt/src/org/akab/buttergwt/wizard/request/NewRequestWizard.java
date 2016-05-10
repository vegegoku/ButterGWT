package org.akab.buttergwt.wizard.request;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.akab.buttergwt.wizard.ClassUtil;
import org.akab.buttergwt.wizard.PluginUtil;
import org.akab.buttergwt.wizard.TemplateUtil;
import org.akab.buttergwt.wizard.plugin.PluginSettings;
import org.akab.engine.core.shared.api.request.EmptyRequestArgs;
import org.akab.engine.core.shared.api.response.EmptyResponse;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.wizard.Wizard;

public class NewRequestWizard
  extends Wizard
{
  private NewRequestItem newRequestItem;
  
  public NewRequestWizard()
  {
    this.newRequestItem = new NewRequestItem();
    addPage(new NewRequestPage(this.newRequestItem));
  }
  
  public boolean performFinish()
  {
    try
    {
      PluginSettings pluginSettings = ClassUtil.getPluginSettings();
      
      Map<String, String> params = new HashMap();
      params.put("NAME", this.newRequestItem.asName());
      params.put("PACKAGE", pluginSettings.asPackage());
      params.put("PRESENTER_IMPORT", this.newRequestItem.getPresenterImport());
      
      String presenterName = ClassUtil.getSimpleClassNameFromFullClassName(this.newRequestItem.getPresenterImport());
      params.put("PRESENTER_NAME", presenterName);
      
      params.put("PRESENTER_CLASS_IMPORT", ((String)params.get("PRESENTER_IMPORT")).replace("ViewPresenter", "Presenter"));
      
      params.put("PRESENTER_CLASS_NAME", ((String)params.get("PRESENTER_NAME")).replace("ViewPresenter", "Presenter"));
      
      IProject project = ClassUtil.getCurrentProject();
      String templateName;
      String requestArgsImport;
      String requestArgsName;
      if (this.newRequestItem.isEmptyRequest())
      {
        requestArgsImport = EmptyRequestArgs.class.getName();
        requestArgsName = "EmptyRequestArgs";
        templateName = "newEmptyServerRequestTemplate.txt";
      }
      else
      {
        requestArgsImport = 
          pluginSettings.asPackage() + ".shared.api.request." + this.newRequestItem.getRequestName() + "RequestArgs";
        requestArgsName = ClassUtil.getSimpleClassNameFromFullClassName(requestArgsImport);
        templateName = "newServerRequestTemplate.txt";
      }
      String responseName;
      String responseImport;
      if (this.newRequestItem.isEmptyResponse())
      {
        responseImport = EmptyResponse.class.getName();
        responseName = "EmptyResponse";
      }
      else
      {
        responseImport = 
          pluginSettings.asPackage() + ".shared.api.response." + this.newRequestItem.getRequestName() + "Response";
        responseName = ClassUtil.getSimpleClassNameFromFullClassName(responseImport);
      }
      params.put("REQUEST_IMPORT", requestArgsImport);
      params.put("REQUEST_NAME", requestArgsName);
      params.put("RESPONSE_IMPORT", responseImport);
      params.put("RESPONSE_NAME", responseName);
      params.put("TEMPLATE_NAME", templateName);
      if (this.newRequestItem.isServerRequest())
      {
        if ((this.newRequestItem.getPath() != null) && (!"".equals(this.newRequestItem.getPath()))) {
          params.put("PATH", this.newRequestItem.getPath());
        }
        if (this.newRequestItem.isHasDownloadController()) {
          createDownloadController(project, pluginSettings, params);
        }
        if (this.newRequestItem.isHasUploadController()) {
          createUploadController(project, pluginSettings, params);
        }
        if (!this.newRequestItem.isEmptyRequest()) {
          createRequestArgs(project, pluginSettings, params);
        }
        if (!this.newRequestItem.isEmptyResponse()) {
          createResponse(project, pluginSettings, params);
        }
        createResponseHandler(project, pluginSettings, params);
        
        createServerRequest(project, pluginSettings, params);
        
        createServerHandler(project, pluginSettings, params);
      }
      else if (this.newRequestItem.isEmptyRequest())
      {
        createEmptyClientRequest(project, pluginSettings, params);
        createEmptyClientRequestHandler(project, pluginSettings, params);
      }
      else
      {
        params.put("REQUEST_ARGS_IMPORT", pluginSettings.asPackage() + ".shared.api.request." + 
          this.newRequestItem.getRequestName() + "RequestArgs");
        createClientRequest(project, pluginSettings, params);
        createClientRequestHandler(project, pluginSettings, params);
        createRequestArgs(project, pluginSettings, params);
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      PluginUtil.showErrorMessage(ex);
    }
    return true;
  }
  
  private void createEmptyClientRequest(IProject project, PluginSettings pluginSettings, Map<String, String> params)
    throws CoreException
  {
    params.put("TEMPLATE_NAME", "request/newEmptyClientRequest.txt");
    params.put("PACKAGE_PATH", "/client/impl/request/");
    params.put("FILE_POSTFIX", "ClientRequest.java");
    
    TemplateUtil.createCompilationUnitFromTemplate(project, pluginSettings, params);
  }
  
  private void createEmptyClientRequestHandler(IProject project, PluginSettings pluginSettings, Map<String, String> params)
    throws CoreException
  {
    params.put("TEMPLATE_NAME", "request/newEmptyClientRequestHandler.txt");
    params.put("PACKAGE_PATH", "/client/impl/request/");
    params.put("FILE_POSTFIX", "ClientRequestHandler.java");
    
    TemplateUtil.createCompilationUnitFromTemplate(project, pluginSettings, params);
  }
  
  private void createClientRequest(IProject project, PluginSettings pluginSettings, Map<String, String> params)
    throws CoreException
  {
    params.put("TEMPLATE_NAME", "request/newClientRequestTemplate.txt");
    params.put("PACKAGE_PATH", "/client/impl/request/");
    params.put("FILE_POSTFIX", "ClientRequest.java");
    
    TemplateUtil.createCompilationUnitFromTemplate(project, pluginSettings, params);
  }
  
  private void createClientRequestHandler(IProject project, PluginSettings pluginSettings, Map<String, String> params)
    throws CoreException
  {
    params.put("TEMPLATE_NAME", "request/newClientRequestHandlerTemplate.txt");
    params.put("PACKAGE_PATH", "/client/impl/request/");
    params.put("FILE_POSTFIX", "ClientRequestHandler.java");
    
    TemplateUtil.createCompilationUnitFromTemplate(project, pluginSettings, params);
  }
  
  private void createRequestArgs(IProject project, PluginSettings pluginSettings, Map<String, String> params)
    throws CoreException
  {
    params.put("TEMPLATE_NAME", "request/requestArgsTemplate.txt");
    params.put("PACKAGE_PATH", "/shared/api/request/");
    params.put("FILE_POSTFIX", ".java");
    
    String template = TemplateUtil.getTemplateString((String)params.get("TEMPLATE_NAME"));
    String templateString = TemplateUtil.replaceTemplateParameters(template, params);
    
    IFile file = project.getFile("src/main/java/" + pluginSettings.asPackagePath() + 
      (String)params.get("PACKAGE_PATH") + (String)params.get("REQUEST_NAME") + ".java");
    
    InputStream source = new ByteArrayInputStream(templateString.getBytes());
    file.create(source, true, null);
  }
  
  private void createResponse(IProject project, PluginSettings pluginSettings, Map<String, String> params)
    throws CoreException
  {
    params.put("TEMPLATE_NAME", "request/responseTemplate.txt");
    params.put("PACKAGE_PATH", "/shared/api/response/");
    params.put("FILE_POSTFIX", "Response.java");
    
    String template = TemplateUtil.getTemplateString((String)params.get("TEMPLATE_NAME"));
    String templateString = TemplateUtil.replaceTemplateParameters(template, params);
    
    IFile file = project.getFile("src/main/java/" + pluginSettings.asPackagePath() + 
      (String)params.get("PACKAGE_PATH") + (String)params.get("RESPONSE_NAME") + ".java");
    
    InputStream source = new ByteArrayInputStream(templateString.getBytes());
    file.create(source, true, null);
  }
  
  private void createResponseHandler(IProject project, PluginSettings pluginSettings, Map<String, String> params)
    throws CoreException
  {
    params.put("TEMPLATE_NAME", "request/newServerRequestHandlerTemplate.txt");
    params.put("PACKAGE_PATH", "/client/impl/request/");
    params.put("FILE_POSTFIX", "ServerResponseHandler.java");
    
    TemplateUtil.createCompilationUnitFromTemplate(project, pluginSettings, params);
  }
  
  private void createServerRequest(IProject project, PluginSettings pluginSettings, Map<String, String> params)
    throws CoreException
  {
    params.put("TEMPLATE_NAME", "request/newServerRequestTemplate.txt");
    params.put("PACKAGE_PATH", "/client/impl/request/");
    params.put("FILE_POSTFIX", "ServerRequest.java");
    
    TemplateUtil.createCompilationUnitFromTemplate(project, pluginSettings, params);
  }
  
  private void createServerHandler(IProject project, PluginSettings pluginSettings, Map<String, String> params)
    throws CoreException
  {
    params.put("TEMPLATE_NAME", "request/serverHandlerTemplate.txt");
    params.put("PACKAGE_PATH", "/server/impl/request/handler/");
    params.put("FILE_POSTFIX", "ServerHanlder.java");
    
    TemplateUtil.createCompilationUnitFromTemplate(project, pluginSettings, params);
  }
  
  private void createDownloadController(IProject project, PluginSettings pluginSettings, Map<String, String> params)
    throws CoreException
  {
    params.put("TEMPLATE_NAME", "request/downloadControllerTemplate.txt");
    params.put("PACKAGE_PATH", "/server/impl/request/download/");
    params.put("FILE_POSTFIX", "DownloadController.java");
    
    TemplateUtil.createCompilationUnitFromTemplate(project, pluginSettings, params);
  }
  
  private void createUploadController(IProject project, PluginSettings pluginSettings, Map<String, String> params)
    throws CoreException
  {
    params.put("TEMPLATE_NAME", "request/uploadControllerTemplate.txt");
    params.put("PACKAGE_PATH", "/server/impl/request/upload/");
    params.put("FILE_POSTFIX", "UploadController.java");
    
    TemplateUtil.createCompilationUnitFromTemplate(project, pluginSettings, params);
  }
}
