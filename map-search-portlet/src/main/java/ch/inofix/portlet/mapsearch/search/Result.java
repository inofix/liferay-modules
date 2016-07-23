
package ch.inofix.portlet.mapsearch.search;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Christian Berndt
 * @created 2016-07-23 23:38
 * @modified 2016-07-23 23:38
 * @version 1.0.0
 */
public class Result {

    private List<List<String>> data = new ArrayList<List<String>>();
    private boolean draw;
    private int recordsFiltered;
    private int recordsTotal;

    public List<List<String>> getData() {

        return data;
    }

    public void setData(List<List<String>> data) {

        this.data = data;
    }

    public boolean isDraw() {

        return draw;
    }

    public void setDraw(boolean draw) {

        this.draw = draw;
    }

    public int getRecordsFiltered() {

        return recordsFiltered;
    }

    public void setRecordsFiltered(int recordsFiltered) {

        this.recordsFiltered = recordsFiltered;
    }

    public int getRecordsTotal() {

        return recordsTotal;
    }

    public void setRecordsTotal(int recordsTotal) {

        this.recordsTotal = recordsTotal;
    }

}
