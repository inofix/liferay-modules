package ch.inofix.referencemanager.jsf.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.servlet.http.HttpServletRequest;

import org.jbibtex.BibTeXEntry;
import org.jbibtex.Key;
import org.jbibtex.StringValue;
import org.jbibtex.StringValue.Style;
import org.jbibtex.Value;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;

import com.liferay.asset.kernel.AssetRendererFactoryRegistryUtil;
import com.liferay.asset.kernel.model.AssetRenderer;
import com.liferay.asset.kernel.model.AssetRendererFactory;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import ch.inofix.referencemanager.model.Bibliography;
import ch.inofix.referencemanager.model.Reference;
import ch.inofix.referencemanager.service.BibRefRelationLocalServiceUtil;
import ch.inofix.referencemanager.service.BibliographyServiceUtil;
import ch.inofix.referencemanager.service.ReferenceLocalServiceUtil;
import ch.inofix.referencemanager.service.ReferenceServiceUtil;
import ch.inofix.referencemanager.service.util.BibTeXUtil;
import ch.inofix.referencemanager.service.util.BibliographyUtil;

/**
 * 
 * @author Christian Berndt
 * @created 2017-01-03 14:34
 * @modified 2017-02-04 17:19
 * @version 1.2.3
 *
 */
@ManagedBean
@ViewScoped
public class ReferenceEditorView {

    @PostConstruct
    public void init() {

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        PortletRequest portletRequest = (PortletRequest) externalContext.getRequest();
        PortletResponse portletResponse = (PortletResponse) externalContext.getResponse();
        _bibliographyId = ParamUtil.getLong(portletRequest, "bibliographyId");
        long referenceId = ParamUtil.getLong(portletRequest, "referenceId");
        _redirect = ParamUtil.getString(portletRequest, "redirect");
        ThemeDisplay themeDisplay = (ThemeDisplay) portletRequest.getAttribute(WebKeys.THEME_DISPLAY);

        if (!themeDisplay.isSignedIn()) {
            _disabled = true;
        }

        if (referenceId > 0) {
            try {
                _reference = ReferenceServiceUtil.getReference(referenceId);
            } catch (PortalException e) {
                FacesMessage msg = new FacesMessage("An error occured while loading the requested reference.");
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        } else {
            _reference = ReferenceLocalServiceUtil.createReference(0);
        }

        _bibTeX = _reference.getBibTeX();
        _entryType = _reference.getType();
        _label = _reference.getLabel();

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

        try {
            Sort sort = new Sort("title_sortable", false);

            Hits hits = BibliographyServiceUtil.search(themeDisplay.getUserId(), 0, themeDisplay.getUserId(), null, 0,
                    Integer.MAX_VALUE, sort);

            List<Document> documents = ListUtil.toList(hits.getDocs());

            _userBibliographies = BibliographyUtil.documentsToBibliographies(documents);

        } catch (PortalException pe) {
            _log.error(pe);
        }

        updateBibliographies(portletRequest, portletResponse);
        updateFields();
    }

    public List<Bibliography> completeBibliography(String query) {

        List<Bibliography> results = new ArrayList<Bibliography>();

        for (Bibliography bibliography : _userBibliographies) {
            String title = bibliography.getTitle();

            if (title != null) {
                if (title.toLowerCase().contains(query.toLowerCase())) {
                    results.add(bibliography);
                }
            }
        }

        return results;
    }

    public void onBibliographySelect(SelectEvent event) {

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        PortletRequest portletRequest = (PortletRequest) externalContext.getRequest();
        PortletResponse portletResponse = (PortletResponse) externalContext.getResponse();
        HttpServletRequest request = PortalUtil.getHttpServletRequest(portletRequest);
        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long userId = themeDisplay.getUserId();

        try {
            ServiceContext serviceContext = ServiceContextFactory.getInstance(Reference.class.getName(), request);

            BibRefRelationLocalServiceUtil.addBibRefRelation(userId, _selectedBibliography.getBibliographyId(),
                    _reference.getReferenceId(), serviceContext);

            String[] args = new String[2];
            args[0] = _reference.getCitation();
            args[1] = _selectedBibliography.getTitle();

            String message = LanguageUtil.format(request, "successfully-added-reference-x-to-bibliography-x", args);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message, event.getObject().toString()));

            // reload the re-indexed reference
            
            _reference = ReferenceServiceUtil.getReference(_reference.getReferenceId());
            
            // update related bibliographies
            
            updateBibliographies(portletRequest, portletResponse);

        } catch (Exception e) {

            String message = LanguageUtil.format(request, "an-error-ocurred-x", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, message, event.getObject().toString()));

        }

        _selectedBibliography = null;
    }

    public void onBibTeXChange() {
        _reference.setBibTeX(_bibTeX);
        updateFields();
    }

    public void onEntryTypeChange() {

        try {
            _entryFields = JSONFactoryUtil.createJSONObject(BibTeXUtil.getProperty("entry.type." + _entryType));
            _optionalValues = new String[getOptionalFields().size()];
            _requiredValues = new String[getRequiredFields().size()];

            updateBibTeX();
            updateFields();

        } catch (JSONException e) {
            _log.error(e);
        }
    }

    public void onFieldChange() {
        updateBibTeX();
        _reference.setBibTeX(_bibTeX);
    }

    public void onTabChange(TabChangeEvent event) {
        updateBibTeX();
        updateFields();
    }

    public void saveReference() throws PortalException {

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        PortletRequest portletRequest = (PortletRequest) externalContext.getRequest();
        HttpServletRequest request = PortalUtil.getHttpServletRequest(portletRequest);
        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long userId = themeDisplay.getUserId();
        ServiceContext serviceContext = ServiceContextFactory.getInstance(Reference.class.getName(), request);

        long[] bibliographyIds = new long[0];

        if (_bibliographyId > 0) {
            bibliographyIds = new long[] { _bibliographyId };
        }

        try {
            if (_reference.getReferenceId() > 0) {
                _reference = ReferenceLocalServiceUtil.updateReference(_reference.getReferenceId(), userId, _bibTeX,
                        bibliographyIds, serviceContext);
            } else {
                _reference = ReferenceServiceUtil.addReference(userId, _bibTeX, bibliographyIds, serviceContext);
            }
            _bibTeX = _reference.getBibTeX();
            FacesMessage msg = new FacesMessage("Saved Reference");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Exception e) {

            _log.error(e);

            FacesMessage msg = new FacesMessage("An error occurred.");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    /*
     * Getters and Setters
     */

    public long getBibliographyId() {
        return _bibliographyId;
    }

    public void setBibliographyId(long bibliographyId) {
        this._bibliographyId = bibliographyId;
    }

    public List<Map<String, String>> getBibliographies() {
        return _bibliographies;
    }

    public String getBibTeX() {
        return _bibTeX;
    }

    public void setBibTeX(String bibTeX) {
        this._bibTeX = bibTeX;
    }

    public String getCitation() {
        return _reference.getCitation();
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

    public Bibliography getSelectedBibliography() {
        return _selectedBibliography;
    }

    public void setSelectedBibliography(Bibliography selectedBibliography) {
        this._selectedBibliography = selectedBibliography;
    }
    
    private void updateBibliographies(PortletRequest portletRequest, PortletResponse portletResponse) {

        AssetRendererFactory<Bibliography> assetRendererFactory = AssetRendererFactoryRegistryUtil
                .getAssetRendererFactoryByClass(Bibliography.class);

        _bibliographies = new ArrayList<>();

        try {
            for (long bibliographyId : _reference.getBibliographyIds()) {
                AssetRenderer<Bibliography> assetRenderer = assetRendererFactory.getAssetRenderer(bibliographyId);
                Map<String, String> map = new HashMap<String, String>();
                Locale locale = portletRequest.getLocale();
                String title = assetRenderer.getTitle(locale);
                LiferayPortletRequest liferayPortletRequest = PortalUtil.getLiferayPortletRequest(portletRequest);
                LiferayPortletResponse liferayPortletResponse = PortalUtil.getLiferayPortletResponse(portletResponse);
                String viewURL = assetRenderer.getURLViewInContext(liferayPortletRequest, liferayPortletResponse, null);

                map.put("title", title);
                map.put("url", viewURL);
                _bibliographies.add(map);

            }
        } catch (Exception e) {
            _log.error(e);
        }
    }

    private void updateBibTeX() {

        Key type = new Key(_entryType);
        Key key = new Key(_label);

        BibTeXEntry bibTeXEntry = null;

        // parse src
        if (Validator.isNotNull(_bibTeX)) {
            bibTeXEntry = BibTeXUtil.getBibTeXEntry(_bibTeX);
        }

        if (bibTeXEntry == null) {
            bibTeXEntry = new BibTeXEntry(type, key);
        }

        for (int i = 0; i < _optionalValues.length; i++) {

            String str = _optionalValues[i];
            Value value = new StringValue(str, Style.BRACED);
            String name = getOptionalFields().get(i).getString("name");
            Key field = new Key(name);

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

            if (Validator.isNotNull(str)) {
                bibTeXEntry.removeField(key);
                bibTeXEntry.addField(field, value);
            }
        }

        _bibTeX = BibTeXUtil.format(bibTeXEntry);

        // update src with selected entryType
        String what = _bibTeX.substring(0, _bibTeX.indexOf(StringPool.OPEN_CURLY_BRACE) + 1);
        String with = StringPool.AT + _entryType + StringPool.OPEN_CURLY_BRACE;
        _bibTeX = _bibTeX.replace(what, with);

        _reference.setBibTeX(_bibTeX);

    }

    private void updateFields() {

        BibTeXEntry bibTeXEntry = BibTeXUtil.getBibTeXEntry(_bibTeX);

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
            if (bibTeXEntry != null) {
                Value value = bibTeXEntry.getField(key);
                if (value != null) {
                    _optionalValues[i] = value.toUserString();
                }
            }
        }

        for (int i = 0; i < _requiredValues.length; i++) {

            String field = getRequiredFields().get(i).getString("name");
            Key key = new Key(field);
            if (bibTeXEntry != null) {
                Value value = bibTeXEntry.getField(key);
                if (value != null) {
                    _requiredValues[i] = value.toUserString();
                }
            }
        }
    }

    private List<Map<String, String>> _bibliographies;
    private long _bibliographyId;
    private String _bibTeX;
    private boolean _disabled = false;
    private JSONObject _entryFields;
    private String _entryType = "article";
    private List<String> _entryTypes;
    private String _label;
    private String[] _optionalValues;
    private String _redirect;
    private Reference _reference;
    private String[] _requiredValues;
    private Bibliography _selectedBibliography;
    private List<Bibliography> _userBibliographies;

    private static final Log _log = LogFactoryUtil.getLog(ReferenceEditorView.class);

}
