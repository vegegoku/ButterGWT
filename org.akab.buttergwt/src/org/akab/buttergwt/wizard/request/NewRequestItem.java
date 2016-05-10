package org.akab.buttergwt.wizard.request;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

public class NewRequestItem
{
  private String requestName;
  private String presenterImport;
  private String responseName;
  private String presenterName;
  private String presenterNameLowerCase;
  private boolean serverRequest = false;
  private boolean emptyRequest = false;
  private boolean emptyResponse;
  private boolean hasDownloadController;
  private boolean hasUploadController;
  private String path;
  
  public String getRequestName()
  {
    return this.requestName;
  }
  
  public void setRequestName(String requestName)
  {
    this.requestName = requestName;
  }
  
  public String getPresenterImport()
  {
    return this.presenterImport;
  }
  
  public void setPresenterImport(String presenterImport)
  {
    this.presenterImport = presenterImport;
  }
  
  public String getResponseName()
  {
    return this.responseName;
  }
  
  public void setResponseName(String responseName)
  {
    this.responseName = responseName;
  }
  
  public String getPresenterName()
  {
    return this.presenterName;
  }
  
  public void setPresenterName(String presenterName)
  {
    this.presenterName = presenterName;
  }
  
  public String getPresenterNameLowerCase()
  {
    return this.presenterNameLowerCase;
  }
  
  public void setPresenterNameLowerCase(String presenterNameLowerCase)
  {
    this.presenterNameLowerCase = presenterNameLowerCase;
  }
  
  public boolean isServerRequest()
  {
    return this.serverRequest;
  }
  
  public void setServerRequest(boolean serverRequest)
  {
    this.serverRequest = serverRequest;
  }
  
  public boolean isEmptyRequest()
  {
    return this.emptyRequest;
  }
  
  public void setEmptyRequest(boolean emptyRequest)
  {
    this.emptyRequest = emptyRequest;
  }
  
  public boolean isEmptyResponse()
  {
    return this.emptyResponse;
  }
  
  public void setEmptyResponse(boolean emptyResponse)
  {
    this.emptyResponse = emptyResponse;
  }
  
  public String toString()
  {
    return 
    
      "NewRequestItem [requestName=" + this.requestName + ", presenterImport=" + this.presenterImport + ", responseName=" + this.responseName + ", presenterName=" + this.presenterName + ", presenterNameLowerCase=" + this.presenterNameLowerCase + ", serverRequest=" + this.serverRequest + ", emptyRequest=" + this.emptyRequest + ", emptyResponse=" + this.emptyResponse + "]";
  }
  
  public boolean isHasDownloadController()
  {
    return this.hasDownloadController;
  }
  
  public void setHasDownloadController(boolean hasDownloadController)
  {
    this.hasDownloadController = hasDownloadController;
    this.hasUploadController = (!this.hasDownloadController);
  }
  
  public boolean isHasUploadController()
  {
    return this.hasUploadController;
  }
  
  public void setHasUploadController(boolean hasUploadController)
  {
    this.hasUploadController = hasUploadController;
    this.hasDownloadController = (!this.hasUploadController);
  }
  
  public String getPath()
  {
    return this.path;
  }
  
  public void setPath(String path)
  {
    this.path = path;
  }
  
  public String asName()
  {
    String text = this.requestName;
    text = text.replaceAll("[^a-zA-Z0-9]", " ");
    
    return StringUtils.remove(WordUtils.capitalizeFully(text), " ");
  }
}
