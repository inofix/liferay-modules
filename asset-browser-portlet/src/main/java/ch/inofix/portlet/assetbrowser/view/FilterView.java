package ch.inofix.portlet.assetbrowser.view;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.SelectableDataModel;

import ch.inofix.portlet.assetbrowser.domain.Car;
import ch.inofix.portlet.assetbrowser.service.CarService;

@ManagedBean(name = "dtFilterView")
@ViewScoped
public class FilterView implements Serializable, SelectableDataModel<Car> {

    private List<Car> cars;

    private List<Car> filteredCars;

    private Car selectedCar;

    private Car[] selectedCars;

    @ManagedProperty("#{carService}")
    private CarService service;

    @PostConstruct
    public void init() {

        cars = service.createCars(10);
    }

    public boolean filterByPrice(Object value, Object filter, Locale locale) {

        String filterText = (filter == null) ? null : filter.toString().trim();
        if (filterText == null || filterText.equals("")) {
            return true;
        }

        if (value == null) {
            return false;
        }

        return ((Comparable) value).compareTo(Integer.valueOf(filterText)) > 0;
    }

    public List<String> getBrands() {

        return service.getBrands();
    }

    public List<String> getColors() {

        return service.getColors();
    }

    public List<Car> getCars() {

        return cars;
    }

    public List<Car> getFilteredCars() {

        return filteredCars;
    }

    public void setFilteredCars(List<Car> filteredCars) {

        this.filteredCars = filteredCars;
    }

    public Car getSelectedCar() {
        return selectedCar;
    }

    public void setSelectedCar(Car selectedCar) {
        this.selectedCar = selectedCar;
    }

    public Car[] getSelectedCars() {
        return selectedCars;
    }

    public void setSelectedCars(Car[] selectedCars) {
        this.selectedCars = selectedCars;
    }

    public void setService(CarService service) {

        this.service = service;
    }

    @Override
    public Car getRowData(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getRowKey(Car arg0) {
        // TODO Auto-generated method stub
        return null;
    }
}
