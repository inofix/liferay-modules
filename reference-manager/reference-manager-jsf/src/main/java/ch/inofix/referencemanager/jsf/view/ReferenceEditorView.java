package ch.inofix.referencemanager.jsf.view;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;

import org.jbibtex.BibTeXEntry;
import org.jbibtex.Key;
import org.jbibtex.StringValue;
import org.jbibtex.StringValue.Style;
import org.jbibtex.Value;
import org.primefaces.event.TabChangeEvent;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import ch.inofix.referencemanager.model.Reference;
import ch.inofix.referencemanager.service.ReferenceLocalServiceUtil;
import ch.inofix.referencemanager.service.ReferenceServiceUtil;
import ch.inofix.referencemanager.service.util.BibTeXUtil;

/**
 * 
 * @author Christian Berndt
 * @created 2017-01-03 14:34
 * @modified 2017-01-07 21:51
 * @version 1.0.7
 *
 */
@ManagedBean
@ViewScoped
public class ReferenceEditorView {

    @PostConstruct
    public void init() {

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        PortletRequest portletRequest = (PortletRequest) externalContext.getRequest();
        _bibliographyId = ParamUtil.getLong(portletRequest, "bibliographyId");
        long referenceId = ParamUtil.getLong(portletRequest, "referenceId");
        _redirect = ParamUtil.getString(portletRequest, "redirect");
        
        _log.info("bibliographyId = " + _bibliographyId);

        try {
            if (referenceId > 0) {
                _reference = ReferenceServiceUtil.getReference(referenceId);
            }
        } catch (PortalException e) {
            FacesMessage msg = new FacesMessage("An error occured while loading the requested reference.");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

        if (_reference != null) {
            _bibTeX = _reference.getBibTeX();
            _citation = _reference.getCitation();
            _entryType = _reference.getType();
            _label = _reference.getLabel();
        }

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

        if (_reference != null) {
            updateFields();
        }
    }

    public void onEntryTypeChange() {

        _log.info("onEntryTypeChange()");
        _log.info("_entryType = " + _entryType);

        try {

            _entryFields = JSONFactoryUtil.createJSONObject(BibTeXUtil.getProperty("entry.type." + _entryType));
            _optionalValues = new String[getOptionalFields().size()];
            _requiredValues = new String[getRequiredFields().size()];

        } catch (JSONException e) {
            _log.error(e);
        }
    }

    public void onFieldChange() {
        updateBibTeX();
    }

    public void onBibTeXChange() {
        updateFields();
    }

    public void onTabChange(TabChangeEvent event) {
        _log.info("onTabChange()");
    }

    public void saveReference() {

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        PortletRequest portletRequest = (PortletRequest) externalContext.getRequest();
        HttpServletRequest request = PortalUtil.getHttpServletRequest(portletRequest);
        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long userId = themeDisplay.getUserId();
        ServiceContext serviceContext = new ServiceContext();
        serviceContext.setScopeGroupId(themeDisplay.getScopeGroupId());

        try {
            if (_reference != null) {
                _reference = ReferenceLocalServiceUtil.updateReference(_reference.getReferenceId(), userId, _bibTeX,
                        serviceContext);
            } else {
                _reference = ReferenceServiceUtil.addReference(userId, _bibTeX, serviceContext);
            }
            FacesMessage msg = new FacesMessage("Saved Reference");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage("An error occurred.");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

    }

    /*
     * Getters and Setters
     */

    public String getBibTeX() {
        return _bibTeX;
    }

    public void setBibTeX(String bibTeX) {
        this._bibTeX = bibTeX;
    }

    public String getCitation() {
        return _citation;
    }

    public boolean isDisabled() {
        return _disabled;
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

    public String getRedirect() {
        return _redirect;
    }

    public Reference getReference() {
        return _reference;
    }

    public String[] getRequiredValues() {
        return _requiredValues;
    }

    public void setRequiredValues(String[] requiredValues) {
        this._requiredValues = requiredValues;
    }

    private void updateBibTeX() {

        _log.info("updateBibTeX()");

        Key type = new Key(_entryType);
        Key key = new Key(_label);

        BibTeXEntry bibTeXEntry = null;

        // parse src
        if (Validator.isNotNull(_bibTeX)) {
            bibTeXEntry = BibTeXUtil.parse(_bibTeX);
        } else {
            bibTeXEntry = new BibTeXEntry(type, key);
        }

        for (int i = 0; i < _optionalValues.length; i++) {

            String str = _optionalValues[i];
            Value value = new StringValue(str, Style.BRACED);
            String name = getOptionalFields().get(i).getString("name");
            Key field = new Key(name);

            _log.info(name + " = " + str);

            if (Validator.isNotNull(str)) {
                bibTeXEntry.removeField(key);
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
                bibTeXEntry.removeField(key);
                bibTeXEntry.addField(field, value);
            }
        }

        _bibTeX = BibTeXUtil.format(bibTeXEntry);

    }

    private void updateFields() {

        BibTeXEntry bibTeXEntry = BibTeXUtil.parse(_bibTeX);

        if (bibTeXEntry != null) {
            if (bibTeXEntry.getType() != null) {
                _entryType = bibTeXEntry.getType().getValue().toLowerCase();
                try {
                    _entryFields = JSONFactoryUtil.createJSONObject(BibTeXUtil.getProperty("entry.type." + _entryType));
                } catch (JSONException e) {
                    _log.error(e);
                }
                _optionalValues = new String[getOptionalFields().size()];
                _requiredValues = new String[getRequiredFields().size()];
            }
            if (bibTeXEntry.getKey() != null) {
                _label = bibTeXEntry.getKey().getValue();
            }
        }

        for (int i = 0; i < _optionalValues.length; i++) {

            String field = getOptionalFields().get(i).getString("name");
            Key key = new Key(field);
            Value value = bibTeXEntry.getField(key);
            if (value != null) {
                _optionalValues[i] = value.toUserString();
            }
        }

        for (int i = 0; i < _requiredValues.length; i++) {

            String field = getRequiredFields().get(i).getString("name");
            Key key = new Key(field);
            Value value = bibTeXEntry.getField(key);
            if (value != null) {
                _requiredValues[i] = value.toUserString();
            }
        }

    }

    private long _bibliographyId;
    private String _bibTeX;
    private String _citation = "Add a new reference";
    // TODO: check the user's updatePermission
    private boolean _disabled = false;
    private JSONObject _entryFields;
    private String _entryType = "article";
    private List<String> _entryTypes;
    private String _label;
    private String[] _optionalValues;
    private String _redirect;
    private Reference _reference;
    private String[] _requiredValues;

    private static final Log _log = LogFactoryUtil.getLog(ReferenceEditorView.class);

}
