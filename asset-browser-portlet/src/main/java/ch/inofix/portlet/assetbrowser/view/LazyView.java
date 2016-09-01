package ch.inofix.portlet.assetbrowser.view;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import ch.inofix.portlet.assetbrowser.domain.Car;
import ch.inofix.portlet.assetbrowser.service.CarService;
 
@ManagedBean(name="dtLazyView")
@ViewScoped
public class LazyView implements Serializable {
    
    private static final Log _log = LogFactoryUtil.getLog(LazyView.class);
     
    private LazyDataModel<Car> lazyModel;
     
    private Car selectedCar;
     
    @ManagedProperty("#{carService}")
    private CarService service;
     
    @PostConstruct
    public void init() {
        lazyModel = new LazyCarDataModel(service.createCars(200));
    }
 
    public LazyDataModel<Car> getLazyModel() {
        return lazyModel;
    }
 
    public Car getSelectedCar() {
        return selectedCar;
    }
 
    public void setSelectedCar(Car selectedCar) {
        this.selectedCar = selectedCar;
    }
     
    public void setService(CarService service) {
        this.service = service;
    }
     
    public void onRowSelect(SelectEvent event) {
        
//        _log.info("onRowSelect");
        
        FacesMessage msg = new FacesMessage("Car Selected", ((Car) event.getObject()).getId());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}