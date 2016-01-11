package ch.inofix.portlet.timetracker.service.persistence;

import com.liferay.portal.kernel.bean.PortletBeanLocatorUtil;
import com.liferay.portal.kernel.util.ReferenceRegistry;


public class TaskRecordFinderUtil {
    private static TaskRecordFinder _finder;

    public static int countBy_C_G_U_W_S_E_D_S(long companyId, long groupId,
        long userId, java.lang.String workPackage, java.util.Date startDate,
        java.util.Date endDate, java.lang.String description, int status,
        boolean andOperator)
        throws com.liferay.portal.kernel.exception.SystemException {
        return getFinder()
                   .countBy_C_G_U_W_S_E_D_S(companyId, groupId, userId,
            workPackage, startDate, endDate, description, status, andOperator);
    }

    public static int countBy_C_G_U_W_S_E_D_S(long companyId, long groupId,
        long userId, java.lang.String[] workPackages, java.util.Date startDate,
        java.util.Date endDate, java.lang.String[] descriptions, int status,
        boolean andOperator)
        throws com.liferay.portal.kernel.exception.SystemException {
        return getFinder()
                   .countBy_C_G_U_W_S_E_D_S(companyId, groupId, userId,
            workPackages, startDate, endDate, descriptions, status, andOperator);
    }

    public static java.util.List<java.lang.Long> countBy_C_G_U_W_S_E_D_S(
        com.liferay.portal.kernel.dao.orm.Session session, long companyId,
        long groupId, long userId, java.lang.String[] workPackages,
        java.util.Date startDate, java.util.Date endDate,
        java.lang.String[] descriptions, int status, boolean andOperator)
        throws com.liferay.portal.kernel.exception.SystemException {
        return getFinder()
                   .countBy_C_G_U_W_S_E_D_S(session, companyId, groupId,
            userId, workPackages, startDate, endDate, descriptions, status,
            andOperator);
    }

    public static int countByKeywords(long companyId, long groupId,
        long userId, java.lang.String keywords, java.util.Date endDate,
        java.util.Date startDate, int status)
        throws com.liferay.portal.kernel.exception.SystemException {
        return getFinder()
                   .countByKeywords(companyId, groupId, userId, keywords,
            endDate, startDate, status);
    }

    public static java.util.List<ch.inofix.portlet.timetracker.model.TaskRecord> findBy_C_G_U_W_S_E_D_S(
        long companyId, long groupId, long userId,
        java.lang.String workPackage, java.util.Date startDate,
        java.util.Date endDate, java.lang.String description, int status,
        boolean andOperator, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.kernel.exception.SystemException {
        return getFinder()
                   .findBy_C_G_U_W_S_E_D_S(companyId, groupId, userId,
            workPackage, startDate, endDate, description, status, andOperator,
            start, end, obc);
    }

    public static java.util.List<ch.inofix.portlet.timetracker.model.TaskRecord> findBy_C_G_U_W_S_E_D_S(
        long companyId, long groupId, long userId,
        java.lang.String[] workPackages, java.util.Date startDate,
        java.util.Date endDate, java.lang.String[] descriptions, int status,
        boolean andOperator, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.kernel.exception.SystemException {
        return getFinder()
                   .findBy_C_G_U_W_S_E_D_S(companyId, groupId, userId,
            workPackages, startDate, endDate, descriptions, status,
            andOperator, start, end, obc);
    }

    public static java.util.List<java.lang.Long> findBy_C_G_U_W_S_E_D_S(
        com.liferay.portal.kernel.dao.orm.Session session, long companyId,
        long groupId, long userId, java.lang.String[] workPackages,
        java.util.Date startDate, java.util.Date endDate,
        java.lang.String[] descriptions, int status, boolean andOperator,
        int start, int end, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.kernel.exception.SystemException {
        return getFinder()
                   .findBy_C_G_U_W_S_E_D_S(session, companyId, groupId, userId,
            workPackages, startDate, endDate, descriptions, status,
            andOperator, start, end, obc);
    }

    public static java.util.List<ch.inofix.portlet.timetracker.model.TaskRecord> findByKeywords(
        long companyId, long groupId, long userId, java.lang.String keywords,
        int status, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.kernel.exception.SystemException {
        return getFinder()
                   .findByKeywords(companyId, groupId, userId, keywords,
            status, start, end, obc);
    }

    public static java.lang.String whereBy_C_G_U_W_S_E_D_S(long companyId,
        long groupId, long userId, java.lang.String[] workPackages,
        java.util.Date startDate, java.util.Date endDate,
        java.lang.String[] descriptions, int status, boolean andOperator,
        com.liferay.portal.kernel.util.OrderByComparator obc) {
        return getFinder()
                   .whereBy_C_G_U_W_S_E_D_S(companyId, groupId, userId,
            workPackages, startDate, endDate, descriptions, status,
            andOperator, obc);
    }

    public static TaskRecordFinder getFinder() {
        if (_finder == null) {
            _finder = (TaskRecordFinder) PortletBeanLocatorUtil.locate(ch.inofix.portlet.timetracker.service.ClpSerializer.getServletContextName(),
                    TaskRecordFinder.class.getName());

            ReferenceRegistry.registerReference(TaskRecordFinderUtil.class,
                "_finder");
        }

        return _finder;
    }

    public void setFinder(TaskRecordFinder finder) {
        _finder = finder;

        ReferenceRegistry.registerReference(TaskRecordFinderUtil.class,
            "_finder");
    }
}
