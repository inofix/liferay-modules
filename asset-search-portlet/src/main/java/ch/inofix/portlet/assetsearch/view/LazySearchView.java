package ch.inofix.portlet.assetsearch.view;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
//import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Document;

@ManagedBean(name="dtLazySearchView")
@ViewScoped
public class LazySearchView implements Serializable {
    
    private static final Log _log = LogFactoryUtil.getLog(LazySearchView.class);
     
    private LazyDataModel<Document> lazyModel;
     
    private Document selectedDocument;
     
//    @ManagedProperty("#{carService}")
//    private DocumentService service;
     
    @PostConstruct
    public void init() {
        lazyModel = new LazySearchDataModel(); 
//        lazyModel = new LazySearchDataModel(service.createDocuments(200));
    }
 
    public LazyDataModel<Document> getLazyModel() {
        return lazyModel;
    }
 
    public Document getSelectedDocument() {
        return selectedDocument;
    }
 
    public void setSelectedDocument(Document selectedDocument) {
        this.selectedDocument = selectedDocument;
    }
     
    public void onRowSelect(SelectEvent event) {
        
        _log.info("onRowSelect");
        
        FacesMessage msg = new FacesMessage("Document Selected",
                ((Document) event.getObject()).getUID());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}