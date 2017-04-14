package ch.inofix.portal.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.model.Group;
import com.liferay.portal.service.GroupLocalService;
import com.liferay.portal.util.comparator.GroupNameComparator;

/**
 *
 * @author Christian Berndt
 * @created 2017-04-13 18:05
 * @modified 2017-04-13 18:05
 * @version 1.0.0
 *
 */
public class GroupLocalServiceWrapper extends
        com.liferay.portal.service.GroupLocalServiceWrapper {

    public GroupLocalServiceWrapper(GroupLocalService groupLocalService) {
        super(groupLocalService);
    }

    @Override
    public List<Group> search(long companyId, String name, String description,
            LinkedHashMap<String, Object> params, boolean andOperator,
            int start, int end, OrderByComparator obc) throws SystemException {

        _log.info("search(advanced)");

        _log.info(obc);

        List<Group> groups = super.search(companyId, name, description, params,
                andOperator, start, end, obc);

        return sort(groups, start, end, obc);

    }

    @Override
    public List<Group> search(long companyId, String keywords,
            LinkedHashMap<String, Object> params, int start, int end,
            OrderByComparator obc) throws SystemException {

        _log.info("search(keywords)");

        List<Group> groups = super.search(companyId, keywords, params, start,
                end, obc);

        return sort(groups, start, end, obc);
    }

    /**
     * Custom sort mechanism.
     *
     * @param groups
     * @param start
     * @param end
     * @param obc
     * @return
     */
    protected List<Group> sort(List<Group> groups, int start, int end,
            OrderByComparator obc) {

        if (obc == null) {
            obc = new GroupDescriptiveNameComparator(true);
        } else if (obc.getClass().equals(GroupNameComparator.class)) {
            boolean ascending = obc.isAscending();
            obc = new GroupDescriptiveNameComparator(ascending);
        }

        _log.info(obc);

        List<Group> sortableGroups = new ArrayList<Group>(groups);

        Collections.sort(sortableGroups, obc);

        return Collections.unmodifiableList(ListUtil.subList(sortableGroups, start,
                end));

    }

    public class GroupDescriptiveNameComparator extends OrderByComparator {

        public static final String ORDER_BY_ASC = "groupName ASC";

        public static final String ORDER_BY_DESC = "groupName DESC";

        public GroupDescriptiveNameComparator() {
            this(false);
        }

        public GroupDescriptiveNameComparator(boolean ascending) {

            _log.info("init GroupDescriptiveNameComparator");
            _ascending = ascending;
        }

        @Override
        public int compare(Object obj1, Object obj2) {

            Group group1 = (Group) obj1;
            Group group2 = (Group) obj2;

            String name1 = group1.getName();
            String name2 = group2.getName();

            // Custom comparison of descriptive name
            try {
                name1 = group1.getDescriptiveName();
                name2 = group2.getDescriptiveName();
            } catch (PortalException pe) {
                _log.error(pe.getMessage());
            } catch (SystemException se) {
                _log.error(se.getMessage());
            }

            int value = name1.toLowerCase().compareTo(name2.toLowerCase());

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
        public boolean isAscending() {
            return _ascending;
        }

        private boolean _ascending;

    }

    private static Log _log = LogFactoryUtil
            .getLog(GroupLocalServiceWrapper.class.getName());
}
