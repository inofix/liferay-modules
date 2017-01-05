package ch.inofix.referencemanager.jsf.view;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.jbibtex.BibTeXEntry;
import org.jbibtex.Key;
import org.jbibtex.StringValue;
import org.jbibtex.StringValue.Style;
import org.jbibtex.Value;
import org.primefaces.event.TabChangeEvent;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Validator;

import ch.inofix.referencemanager.service.util.BibTeXUtil;

/**
 * 
 * @author Christian Berndt
 * @created 2017-01-03 14:34
 * @modified 2017-01-05 00:34
 * @version 1.0.2
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

    public void saveReference() {

        _log.info("saveReference()");
        // _log.info("_entryType = " + _entryType);

        updateBibTeX();

        FacesMessage msg = new FacesMessage("Saved Reference");
        FacesContext.getCurrentInstance().addMessage(null, msg);

    }

    public void onTabChange(TabChangeEvent event) {

        _log.info("onTabChange()");

        // updateBibTeX();

    }

    @PostConstruct
    public void init() {

        try {
            _entryFields = JSONFactoryUtil.createJSONObject(BibTeXUtil.getProperty("entry.type." + _entryType));
            _entryTypes = new ArrayList<String>();

            for (String type : BibTeXUtil.ENTRY_TYPES) {
                _entryTypes.add(type);
            }

            _optionalValues = new String[getOptionalFields().size()];
            _requiredValues = new String[getRequiredFields().size()];

        } catch (JSONException e) {
            _log.error(e);
        }
    }

    public String getBibTeX() {
        return _bibTeX;
    }

    public void setBibTeX(String bibTeX) {
        this._bibTeX = bibTeX;
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

    public String getLabel() {
        return _label;
    }

    public void setLabel(String label) {
        this._label = label;
    }

    public List<JSONObject> getOptionalFields() {
        return getFields("optional");
    }

    public List<JSONObject> getRequiredFields() {
        return getFields("required");
    }

    public String[] getOptionalValues() {
        return _optionalValues;
    }

    public void setOptionalValues(String[] optionalValues) {
        this._optionalValues = optionalValues;
    }

    public String[] getRequiredValues() {
        return _requiredValues;
    }

    public void setRequiredValues(String[] requiredValues) {
        this._requiredValues = requiredValues;
    }

    private void updateBibTeX() {

        _log.info("updateBibTeX()");

        _log.info("bibTeX = " + _bibTeX);
        _log.info("entryType = " + _entryType);
        _log.info("label = " + _label);

        Key type = new Key(_entryType);
        Key key = new Key(_label);

        BibTeXEntry bibTeXEntry = new BibTeXEntry(type, key);

        for (int i = 0; i < _optionalValues.length; i++) {

            String str = _optionalValues[i];
            Value value = new StringValue(str, Style.BRACED);
            String name = getOptionalFields().get(i).getString("name");
            Key field = new Key(name);

            _log.info(name + " = " + str);

            if (Validator.isNotNull(str)) {
                bibTeXEntry.addField(field, value);
            }
        }

        for (int i = 0; i < _requiredValues.length; i++) {

            String str = _requiredValues[i];
            Value value = new StringValue(str, Style.BRACED);
            String name = getRequiredFields().get(i).getString("name");
            Key field = new Key(name);

            _log.info(name + " = " + str);

            if (Validator.isNotNull(str)) {
                bibTeXEntry.addField(field, value);
            }
        }

        _bibTeX = BibTeXUtil.format(bibTeXEntry);

    }

    private String _bibTeX;
    private String _citation = "Add a new reference";
    private JSONObject _entryFields;
    private String _entryType = "article";
    private List<String> _entryTypes;
    private String _label;
    private String[] _optionalValues;
    private String[] _requiredValues;

    private static final Log _log = LogFactoryUtil.getLog(TabbedView.class);

}
