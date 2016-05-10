package org.akab.buttergwt.wizard.download;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

public class NewDownloadControllerItem
{
  private String name;
  private String requestImport;
  private String subPackage;
  
  public String getName()
  {
    return StringUtils.remove(WordUtils.capitalizeFully(this.name), " ");
  }
  
  public void setName(String name)
  {
    this.name = name;
  }
  
  public String getRequestImport()
  {
    return this.requestImport;
  }
  
  public void setRequestImport(String requestImport)
  {
    this.requestImport = requestImport;
  }
  
  public String getSubPackage()
  {
    return this.subPackage;
  }
  
  public void setSubPackage(String subPackage)
  {
    this.subPackage = subPackage;
  }
}
