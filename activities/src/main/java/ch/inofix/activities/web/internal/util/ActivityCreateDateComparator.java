package ch.inofix.activities.web.internal.util;

import java.util.Date;

import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.social.kernel.model.SocialActivity;

/**
 * 
 * @author Christian Berndt
 * @created 2016-12-26 00:39
 * @modified 2016-12-26 00:39
 * @version 1.0.0
 *
 */
public class ActivityCreateDateComparator extends OrderByComparator<SocialActivity> {

    public static final String ORDER_BY_ASC = "SocialActivity.createDate ASC";

    public static final String ORDER_BY_DESC = "SocialActivity.createDate DESC";

    public static final String[] ORDER_BY_FIELDS = { "createDate" };

    public ActivityCreateDateComparator() {
        this(false);
    }

    public ActivityCreateDateComparator(boolean ascending) {
        _ascending = ascending;
    }

    @Override
    public int compare(SocialActivity activity1, SocialActivity activity2) {
        int value = DateUtil.compareTo(new Date(activity1.getCreateDate()), new Date(activity2.getCreateDate()));

        if (_ascending) {
            return value;
        } else {
            return -value;
        }
    }

    @Override
    public String getOrderBy() {
        if (_ascending) {
            return ORDER_BY_ASC;
        } else {
            return ORDER_BY_DESC;
        }
    }

    @Override
    public String[] getOrderByFields() {
        return ORDER_BY_FIELDS;
    }

    @Override
    public boolean isAscending() {
        return _ascending;
    }

    private final boolean _ascending;
}
