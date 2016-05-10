package org.akab.buttergwt.wizard.presenter;

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

public class NewPresenterPage
  extends WizardPage
{
  private DataBindingContext m_bindingContext;
  private Text name;
  private Text description;
  private NewPresenterItem newPresenterItem;
  
  public NewPresenterPage(NewPresenterItem newPresenterItem)
  {
    super("wizardPage");
    this.newPresenterItem = newPresenterItem;
    setTitle("Wizard Page title");
    setDescription("Wizard Page description");
  }
  
  public void createControl(Composite parent)
  {
    Composite container = new Composite(parent, 0);
    
    setControl(container);
    container.setLayout(new GridLayout(3, false));
    
    Label lblNewLabel = new Label(container, 0);
    lblNewLabel.setText("Name");
    
    this.name = new Text(container, 2048);
    this.name.setLayoutData(new GridData(4, 16777216, true, false, 2, 1));
    
    Label lblNewLabel_2 = new Label(container, 0);
    GridData gd_lblNewLabel_2 = new GridData(16384, 16777216, false, false, 1, 1);
    gd_lblNewLabel_2.widthHint = 97;
    lblNewLabel_2.setLayoutData(gd_lblNewLabel_2);
    lblNewLabel_2.setText("Description");
    
    this.description = new Text(container, 2048);
    this.description.setLayoutData(new GridData(4, 16777216, true, false, 2, 1));
    new Label(container, 0);
    new Label(container, 0);
    new Label(container, 0);
    
    initControles();
    this.m_bindingContext = initDataBindings();
  }
  
  private void initControles()
  {
    throw new Error("Unresolved compilation problems: \n\tLambda expressions are allowed only at source level 1.8 or above\n\tLambda expressions are allowed only at source level 1.8 or above\n");
  }
  
  private void validatePage()
  {
    boolean complete = true;
    if (StringUtils.isEmpty(this.name.getText()))
    {
      complete = false;
      setErrorMessage("Presenter name connot be empty!.");
    }
    if (StringUtils.isEmpty(this.description.getText()))
    {
      complete = false;
      setErrorMessage("Presenter description connaot be empty!.");
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
    IObservableValue presenterNameNewPresenterItemObserveValue = PojoProperties.value("presenterName").observe(this.newPresenterItem);
    bindingContext.bindValue(observeTextNameObserveWidget, presenterNameNewPresenterItemObserveValue, null, null);
    
    IObservableValue observeTextDescriptionObserveWidget = WidgetProperties.text(24).observe(this.description);
    IObservableValue presenterDescriptionNewPresenterItemObserveValue = PojoProperties.value("presenterDescription").observe(this.newPresenterItem);
    bindingContext.bindValue(observeTextDescriptionObserveWidget, presenterDescriptionNewPresenterItemObserveValue, null, null);
    
    return bindingContext;
  }
}
