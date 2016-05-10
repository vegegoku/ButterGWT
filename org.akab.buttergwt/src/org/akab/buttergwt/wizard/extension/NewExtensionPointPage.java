package org.akab.buttergwt.wizard.extension;

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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class NewExtensionPointPage
  extends WizardPage
{
  private DataBindingContext m_bindingContext;
  private Text name;
  private NewExtensionPointItem newExtensionPoint;
  
  public NewExtensionPointPage(NewExtensionPointItem newContributionItem)
  {
    super("wizardPage");
    this.newExtensionPoint = newContributionItem;
    setTitle("New extension point");
    setDescription("Create a new extension point");
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
    
    initControls();
    this.m_bindingContext = initDataBindings();
  }
  
  private void initControls()
  {
    throw new Error("Unresolved compilation problem: \n\tLambda expressions are allowed only at source level 1.8 or above\n");
  }
  
  private void validatePage()
  {
    boolean complete = true;
    if (StringUtils.isEmpty(this.name.getText()))
    {
      complete = false;
      setErrorMessage("Extension point name connot be empty!.");
    }
    setPageComplete(complete);
    if (complete) {
      setErrorMessage(null);
    }
  }
  
  protected DataBindingContext initDataBindings()
  {
    DataBindingContext bindingContext = new DataBindingContext();
    
    IObservableValue observeTextNameObserveWidget = WidgetProperties.text(24).observe(this.name);
    IObservableValue nameNewExtensionPointObserveValue = PojoProperties.value("name").observe(this.newExtensionPoint);
    bindingContext.bindValue(observeTextNameObserveWidget, nameNewExtensionPointObserveValue, null, null);
    
    return bindingContext;
  }
}
