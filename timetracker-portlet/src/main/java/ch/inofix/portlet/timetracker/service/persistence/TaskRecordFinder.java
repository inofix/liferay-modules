package ch.inofix.portlet.timetracker.service.persistence;

public interface TaskRecordFinder {
    public int countBy_C_G_U_W_S_E_D_S(long companyId, long groupId,
        long userId, java.lang.String workPackage, java.util.Date startDate,
        java.util.Date endDate, java.lang.String description, int status,
        boolean andOperator)
        throws com.liferay.portal.kernel.exception.SystemException;

    public int countBy_C_G_U_W_S_E_D_S(long companyId, long groupId,
        long userId, java.lang.String[] workPackages, java.util.Date startDate,
        java.util.Date endDate, java.lang.String[] descriptions, int status,
        boolean andOperator)
        throws com.liferay.portal.kernel.exception.SystemException;

    public java.util.List<java.lang.Long> countBy_C_G_U_W_S_E_D_S(
        com.liferay.portal.kernel.dao.orm.Session session, long companyId,
        long groupId, long userId, java.lang.String[] workPackages,
        java.util.Date startDate, java.util.Date endDate,
        java.lang.String[] descriptions, int status, boolean andOperator)
        throws com.liferay.portal.kernel.exception.SystemException;

    public int countByKeywords(long companyId, long groupId, long userId,
        java.lang.String keywords, java.util.Date endDate,
        java.util.Date startDate, int status)
        throws com.liferay.portal.kernel.exception.SystemException;

    public java.util.List<ch.inofix.portlet.timetracker.model.TaskRecord> findBy_C_G_U_W_S_E_D_S(
        long companyId, long groupId, long userId,
        java.lang.String workPackage, java.util.Date startDate,
        java.util.Date endDate, java.lang.String description, int status,
        boolean andOperator, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.kernel.exception.SystemException;

    public java.util.List<ch.inofix.portlet.timetracker.model.TaskRecord> findBy_C_G_U_W_S_E_D_S(
        long companyId, long groupId, long userId,
        java.lang.String[] workPackages, java.util.Date startDate,
        java.util.Date endDate, java.lang.String[] descriptions, int status,
        boolean andOperator, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.kernel.exception.SystemException;

    public java.util.List<java.lang.Long> findBy_C_G_U_W_S_E_D_S(
        com.liferay.portal.kernel.dao.orm.Session session, long companyId,
        long groupId, long userId, java.lang.String[] workPackages,
        java.util.Date startDate, java.util.Date endDate,
        java.lang.String[] descriptions, int status, boolean andOperator,
        int start, int end, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.kernel.exception.SystemException;

    public java.util.List<ch.inofix.portlet.timetracker.model.TaskRecord> findByKeywords(
        long companyId, long groupId, long userId, java.lang.String keywords,
        int status, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.kernel.exception.SystemException;

    public java.lang.String whereBy_C_G_U_W_S_E_D_S(long companyId,
        long groupId, long userId, java.lang.String[] workPackages,
        java.util.Date startDate, java.util.Date endDate,
        java.lang.String[] descriptions, int status, boolean andOperator,
        com.liferay.portal.kernel.util.OrderByComparator obc);
}
