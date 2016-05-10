package org.akab.buttergwt.wizard.plugin;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.IBeanValueProperty;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.IWidgetValueProperty;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class NewPluginPage
  extends WizardPage
{
  private DataBindingContext m_bindingContext;
  private PluginSettings newPluginItem;
  private Text artifactId;
  private Text groupId;
  private Text description;
  private Button extensionsPlugin;
  private Label lblExtensionPlugin;
  
  public NewPluginPage(PluginSettings newPluginItem)
  {
    super("wizardPage");
    setTitle("New Plugin");
    setDescription("Create a new plugin project");
    this.newPluginItem = newPluginItem;
  }
  
  public void createControl(Composite parent)
  {
    Composite container = new Composite(parent, 0);
    
    setControl(container);
    container.setLayout(new GridLayout(2, false));
    new Label(container, 0);
    new Label(container, 0);
    
    Label lblGroup = new Label(container, 0);
    lblGroup.setText("Group Id");
    
    this.groupId = new Text(container, 2048);
    this.groupId.setLayoutData(new GridData(4, 16777216, true, false, 1, 1));
    
    Label lblNewLabel = new Label(container, 0);
    lblNewLabel.setText("Artifact Id");
    
    this.artifactId = new Text(container, 2048);
    this.artifactId.setLayoutData(new GridData(4, 16777216, true, false, 1, 1));
    
    Label lblNewLabel_1 = new Label(container, 0);
    lblNewLabel_1.setText("Description");
    
    this.description = new Text(container, 2048);
    this.description.setLayoutData(new GridData(4, 16777216, true, false, 1, 1));
    
    this.lblExtensionPlugin = new Label(container, 0);
    GridData gd_lblExtensionPlugin = new GridData(16384, 16777216, false, false, 1, 1);
    gd_lblExtensionPlugin.widthHint = 135;
    this.lblExtensionPlugin.setLayoutData(gd_lblExtensionPlugin);
    this.lblExtensionPlugin.setText("Extension plugin");
    
    this.extensionsPlugin = new Button(container, 32);
    this.m_bindingContext = initDataBindings();
    
    initControls();
  }
  
  private void initControls()
  {
    throw new Error("Unresolved compilation problems: \n\tLambda expressions are allowed only at source level 1.8 or above\n\tLambda expressions are allowed only at source level 1.8 or above\n\tLambda expressions are allowed only at source level 1.8 or above\n");
  }
  
  protected DataBindingContext initDataBindings()
  {
    DataBindingContext bindingContext = new DataBindingContext();
    
    IObservableValue observeTextGroupIdObserveWidget = WidgetProperties.text(24).observe(this.groupId);
    IObservableValue groupIdNewPluginItemObserveValue = PojoProperties.value("groupId").observe(this.newPluginItem);
    bindingContext.bindValue(observeTextGroupIdObserveWidget, groupIdNewPluginItemObserveValue, null, null);
    
    IObservableValue observeTextArtifactIdObserveWidget = WidgetProperties.text(24).observe(this.artifactId);
    IObservableValue artifactIdNewPluginItemObserveValue = PojoProperties.value("artifactId").observe(this.newPluginItem);
    bindingContext.bindValue(observeTextArtifactIdObserveWidget, artifactIdNewPluginItemObserveValue, null, null);
    
    IObservableValue observeTextDescriptionObserveWidget = WidgetProperties.text(24).observe(this.description);
    IObservableValue descriptionNewPluginItemObserveValue = PojoProperties.value("description").observe(this.newPluginItem);
    bindingContext.bindValue(observeTextDescriptionObserveWidget, descriptionNewPluginItemObserveValue, null, null);
    
    IObservableValue observeSelectionExtensionsPluginObserveWidget = WidgetProperties.selection().observe(this.extensionsPlugin);
    IObservableValue extensionsPluginNewPluginItemObserveValue = PojoProperties.value("extensionsPlugin").observe(this.newPluginItem);
    bindingContext.bindValue(observeSelectionExtensionsPluginObserveWidget, extensionsPluginNewPluginItemObserveValue, null, null);
    
    return bindingContext;
  }
  
  private void validatePage()
  {
    boolean complete = true;
    if (StringUtils.isEmpty(this.groupId.getText()))
    {
      setErrorMessage("Group Id cannot be empty!.");
      complete = false;
    }
    if (StringUtils.isEmpty(this.artifactId.getText()))
    {
      setErrorMessage("Artifcat Id cannot be empty!.");
      complete = false;
    }
    if (StringUtils.isEmpty(this.description.getText()))
    {
      setErrorMessage("Description cannot be empty!.");
      complete = false;
    }
    setPageComplete(complete);
    if (complete) {
      setErrorMessage(null);
    }
  }
  
  public boolean isPageComplete()
  {
    if (StringUtils.isEmpty(this.groupId.getText()))
    {
      setErrorMessage("Group Id cannot be empty!.");
      return false;
    }
    if (StringUtils.isEmpty(this.artifactId.getText()))
    {
      setErrorMessage("Artifcat Id cannot be empty!.");
      return false;
    }
    if (StringUtils.isEmpty(this.description.getText()))
    {
      setErrorMessage("Description cannot be empty!.");
      return false;
    }
    return super.isPageComplete();
  }
}
