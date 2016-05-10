package org.akab.buttergwt.wizard.contribution;

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

public class NewContributionPage
  extends WizardPage
{
  private DataBindingContext m_bindingContext;
  private Text name;
  private Text context;
  private Text extension;
  private Text description;
  private Button browseContext;
  private Button browseExtensions;
  private NewContributionItem newContributionItem;
  
  public NewContributionPage(NewContributionItem newContributionItem)
  {
    super("wizardPage");
    this.newContributionItem = newContributionItem;
    setTitle("New contribution");
    setDescription("Create a new contribution to an existing extension point");
  }
  
  public void createControl(Composite parent)
  {
    Composite container = new Composite(parent, 0);
    
    setControl(container);
    container.setLayout(new GridLayout(3, false));
    
    Label lblName = new Label(container, 0);
    GridData gd_lblName = new GridData(16384, 1024, false, false, 1, 1);
    gd_lblName.heightHint = 19;
    gd_lblName.widthHint = 95;
    lblName.setLayoutData(gd_lblName);
    lblName.setText("Name");
    
    this.name = new Text(container, 2048);
    this.name.setLayoutData(new GridData(4, 16777216, true, false, 2, 1));
    
    Label lblContext = new Label(container, 0);
    GridData gd_lblContext = new GridData(16384, 1024, false, false, 1, 1);
    gd_lblContext.widthHint = 95;
    gd_lblContext.heightHint = 19;
    lblContext.setLayoutData(gd_lblContext);
    lblContext.setText("Context");
    
    this.context = new Text(container, 2048);
    this.context.setLayoutData(new GridData(4, 16777216, true, false, 1, 1));
    
    this.browseContext = new Button(container, 0);
    GridData gd_browseContext = new GridData(16384, 16777216, false, false, 1, 1);
    gd_browseContext.widthHint = 126;
    this.browseContext.setLayoutData(gd_browseContext);
    this.browseContext.setText("Browse context");
    
    Label lblNewLabel = new Label(container, 0);
    lblNewLabel.setText("Extension point");
    
    this.extension = new Text(container, 2048);
    this.extension.setLayoutData(new GridData(4, 16777216, true, false, 1, 1));
    
    this.browseExtensions = new Button(container, 0);
    this.browseExtensions.setText("Browse extensions");
    
    Label lblNewLabel_1 = new Label(container, 0);
    lblNewLabel_1.setText("Description");
    
    this.description = new Text(container, 2048);
    this.description.setLayoutData(new GridData(4, 16777216, true, false, 2, 1));
    new Label(container, 0);
    new Label(container, 0);
    new Label(container, 0);
    this.m_bindingContext = initDataBindings();
    
    initControls();
  }
  
  private void initControls()
  {
    throw new Error("Unresolved compilation problems: \n\tLambda expressions are allowed only at source level 1.8 or above\n\tLambda expressions are allowed only at source level 1.8 or above\n\tLambda expressions are allowed only at source level 1.8 or above\n\tLambda expressions are allowed only at source level 1.8 or above\n");
  }
  
  protected Button getBtnNewButton()
  {
    return this.browseContext;
  }
  
  protected Button getBtnNewButton_1()
  {
    return this.browseExtensions;
  }
  
  protected DataBindingContext initDataBindings()
  {
    DataBindingContext bindingContext = new DataBindingContext();
    
    IObservableValue observeTextNameObserveWidget = WidgetProperties.text(24).observe(this.name);
    IObservableValue contributionNameNewContributionItemObserveValue = PojoProperties.value("contributionName").observe(this.newContributionItem);
    bindingContext.bindValue(observeTextNameObserveWidget, contributionNameNewContributionItemObserveValue, null, null);
    
    IObservableValue observeTextContextObserveWidget = WidgetProperties.text(24).observe(this.context);
    IObservableValue contextImportNewContributionItemObserveValue = PojoProperties.value("contextImport").observe(this.newContributionItem);
    bindingContext.bindValue(observeTextContextObserveWidget, contextImportNewContributionItemObserveValue, null, null);
    
    IObservableValue observeTextExtensionObserveWidget = WidgetProperties.text(24).observe(this.extension);
    IObservableValue extensionPointImportNewContributionItemObserveValue = PojoProperties.value("extensionPointImport").observe(this.newContributionItem);
    bindingContext.bindValue(observeTextExtensionObserveWidget, extensionPointImportNewContributionItemObserveValue, null, null);
    
    IObservableValue observeTextDescriptionObserveWidget = WidgetProperties.text(24).observe(this.description);
    IObservableValue contributionDescriptionNewContributionItemObserveValue = PojoProperties.value("contributionDescription").observe(this.newContributionItem);
    bindingContext.bindValue(observeTextDescriptionObserveWidget, contributionDescriptionNewContributionItemObserveValue, null, null);
    
    return bindingContext;
  }
  
  private void validatePage()
  {
    boolean complete = true;
    if (StringUtils.isEmpty(this.name.getText()))
    {
      complete = false;
      setErrorMessage("Contribution name connot be empty!.");
    }
    if (StringUtils.isEmpty(this.context.getText()))
    {
      complete = false;
      setErrorMessage("Contribution context connaot be empty!.");
    }
    if (StringUtils.isEmpty(this.extension.getText()))
    {
      complete = false;
      setErrorMessage("Contribution extension point connaot be empty!.");
    }
    if (StringUtils.isEmpty(this.description.getText()))
    {
      complete = false;
      setErrorMessage("Contribution description connaot be empty!.");
    }
    setPageComplete(complete);
    if (complete) {
      setErrorMessage(null);
    }
  }
}
