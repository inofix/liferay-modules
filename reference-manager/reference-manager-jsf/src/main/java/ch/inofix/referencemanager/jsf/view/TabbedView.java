package ch.inofix.referencemanager.jsf.view;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import org.primefaces.event.TabChangeEvent;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

/**
 * 
 * @author Christian Berndt
 * @created 2017-01-03 14:34
 * @modified 2017-01-03 14:34
 * @version 1.0.0
 *
 */
@ManagedBean
public class TabbedView {

    public void onTabChange(TabChangeEvent event) {
        _log.info("onTabChange()");
        FacesMessage msg = new FacesMessage("Tab Changed", "Active Tab: " + event.getTab().getTitle());
        _message = msg.getDetail();
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    private List<String> fields;

    @PostConstruct
    public void init() {
        fields = new ArrayList<String>();
        fields.add("author");
        fields.add("title");
        fields.add("year");
    }

    public List<String> getFields() {
        return fields;
    }

    public String getMessage() {
        _log.info("getMessage()");
        return _message;
    }

    private String _message = "Active Tab: Required Fields";

    private static final Log _log = LogFactoryUtil.getLog(TabbedView.class);

}
