package ch.inofix.referencemanager.jsf.view;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.TabChangeEvent;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import ch.inofix.referencemanager.service.util.BibTeXUtil;

/**
 * 
 * @author Christian Berndt
 * @created 2017-01-03 14:34
 * @modified 2017-01-05 16:24
 * @version 1.0.1
 *
 */
@ManagedBean
@ViewScoped
public class TabbedView {

    public void onEntryTypeChange() {
        _log.info("onEntryTypeChange()");
        _log.info("_entryType = " + _entryType);

        try {
            _entryFields = JSONFactoryUtil.createJSONObject(BibTeXUtil.getProperty("entry.type." + _entryType));
        } catch (JSONException e) {
            _log.error(e);
        }

    }

    public void onTabChange(TabChangeEvent event) {
        // TODO: update the reference
        // _log.info("onTabChange()");
        // FacesMessage msg = new FacesMessage("Tab Changed", "Active Tab: " +
        // event.getTab().getTitle());
        // FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    @PostConstruct
    public void init() {

        try {
            _entryFields = JSONFactoryUtil.createJSONObject(BibTeXUtil.getProperty("entry.type." + _entryType));
            _entryTypes = new ArrayList<String>();

            for (String type : BibTeXUtil.ENTRY_TYPES) {
                _entryTypes.add(type);
            }

        } catch (JSONException e) {
            _log.error(e);
        }
    }

    public String getCitation() {
        return _citation;
    }

    public String getEntryType() {
        return _entryType;
    }

    public void setEntryType(String entryType) {
        this._entryType = entryType;
    }

    public List<String> getEntryTypes() {
        return _entryTypes;
    }

    public List<JSONObject> getFields(String name) {

        List<JSONObject> fields = new ArrayList<JSONObject>();
        JSONArray jSONArray = _entryFields.getJSONArray(name);

        if (jSONArray != null) {
            for (int i = 0; i < jSONArray.length(); i++) {
                fields.add(jSONArray.getJSONObject(i));
            }
        }

        return fields;
    }

    public List<JSONObject> getOptionalFields() {
        return getFields("optional");
    }

    public List<JSONObject> getRequiredFields() {
        return getFields("required");
    }

    private String _citation = "Add a new reference";
    private JSONObject _entryFields;
    private String _entryType = "article"; 
    private List<String> _entryTypes;

    private static final Log _log = LogFactoryUtil.getLog(TabbedView.class);

}
