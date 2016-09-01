package ch.inofix.portlet.assetbrowser.view;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.liferay.portlet.asset.model.AssetEntry;

@ManagedBean(name = "dtSearchView")
@ViewScoped
public class SearchView implements Serializable {

    private List<AssetEntry> entries = null;
    private List<AssetEntry> filteredEntries = null;

    public List<AssetEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<AssetEntry> entries) {
        this.entries = entries;
    }

    public List<AssetEntry> getFilteredEntries() {
        return filteredEntries;
    }

    public void setFilteredEntries(List<AssetEntry> filteredEntries) {
        this.filteredEntries = filteredEntries;
    }
}
