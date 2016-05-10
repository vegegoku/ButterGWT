package org.akab.buttergwt.wizard.plugin;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

public class PluginSettings
{
  private String groupId;
  private String artifactId;
  private String description;
  private String packageName;
  private boolean extensionsPlugin;
  
  public String getGroupId()
  {
    return this.groupId;
  }
  
  public void setGroupId(String groupId)
  {
    this.groupId = groupId;
  }
  
  public String getArtifactId()
  {
    return this.artifactId;
  }
  
  public void setArtifactId(String artifactId)
  {
    this.artifactId = artifactId;
  }
  
  public String getDescription()
  {
    return this.description;
  }
  
  public void setDescription(String description)
  {
    this.description = description;
  }
  
  public String getPackageName()
  {
    return this.packageName;
  }
  
  public void setPackageName(String packageName)
  {
    this.packageName = packageName;
  }
  
  public boolean isExtensionsPlugin()
  {
    return this.extensionsPlugin;
  }
  
  public void setExtensionsPlugin(boolean extensionsPlugin)
  {
    this.extensionsPlugin = extensionsPlugin;
  }
  
  public String asName()
  {
    String text = this.artifactId;
    text = text.replaceAll("[^a-zA-Z0-9]", " ");
    
    return StringUtils.remove(WordUtils.capitalizeFully(text), " ");
  }
  
  public String asPackagePath()
  {
    String text = this.groupId + "/" + this.artifactId;
    text = text.replaceAll("\\.", "/");
    
    return text;
  }
  
  public String asPackage()
  {
    return this.groupId + "." + this.artifactId;
  }
  
  public String asConstant()
  {
    String text = this.artifactId;
    text = text.replaceAll("[^a-zA-Z0-9]", "_");
    
    return text.toUpperCase();
  }
}
