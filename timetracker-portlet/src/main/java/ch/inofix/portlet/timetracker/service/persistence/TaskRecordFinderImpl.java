package ch.inofix.portlet.timetracker.service.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.inofix.portlet.timetracker.model.TaskRecord;
import ch.inofix.portlet.timetracker.util.TaskRecordFields;

import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.Type;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;
import com.liferay.util.dao.orm.CustomSQLUtil;

/**
 * Finder methods of the timetracker portlet.
 * 
 * @author Christian Berndt
 * @created 2013-10-07 12:30
 * @modified 2013-10-07 12:30
 * @version 1.0
 * 
 */
public class TaskRecordFinderImpl extends BasePersistenceImpl<TaskRecord>
		implements TaskRecordFinder {

	// Enable loging for this class.
	private static Log _log = LogFactoryUtil.getLog(TaskRecordFinderImpl.class
			.getName());

	public static final String SELECT_STMT = TaskRecordFinder.class.getName()
	        + ".SELECT_STMT";;

	public static final String WHERE_BY_C_G_U_W_S_E_D_S = TaskRecordFinder.class
			.getName() + ".WHERE_BY_C_G_U_W_S_E_D_S";

	protected static String STATUS_SQL = "AND (status = ?)";
	protected static String USER_ID_SQL = "AND (userId = ?)";

	/**
	 * Return the number of description records matching the given parameters.
	 * 
	 * @param companyId
	 * @param groupId
	 * @param userId
	 * @param workPackage
	 * @param endDate
	 * @param starDate
	 * @param description
	 * @param status
	 * @param andOperator
	 *            AND or OR.
	 * @return the number of description records matching the given parameters.
	 * @since 1.0
	 * @throws SystemException
	 */
	public int countBy_C_G_U_W_S_E_D_S(long companyId, long groupId,
			long userId, String workPackage, Date startDate, Date endDate,
			String description, int status, boolean andOperator)
			throws SystemException {

		_log.info("Executing countBy_C_G_U_W_S_E_D_S(str).");

		// Prepend and append "%" for the LIKE statement.
		String[] workPackages = CustomSQLUtil.keywords(workPackage);
		String[] descriptions = CustomSQLUtil.keywords(description);

		return countBy_C_G_U_W_S_E_D_S(companyId, groupId, userId,
				workPackages, endDate, startDate, descriptions, status,
				andOperator);
	}

	/**
	 * Return the number of description records matching the given parameters.
	 * 
	 * @param companyId
	 * @param groupId
	 * @param userId
	 * @param workPackages
	 * @param endDate
	 * @param startDate
	 * @param descriptions
	 * @param status
	 * @param andOperator
	 *            AND or OR.
	 * @return the number of description records matching the given parameters.
	 * @since 1.0
	 * @throws SystemException
	 */
	public int countBy_C_G_U_W_S_E_D_S(long companyId, long groupId,
			long userId, String[] workPackages, Date startDate, Date endDate,
			String[] descriptions, int status, boolean andOperator)
			throws SystemException {

		_log.info("Executing countBy_C_G_U_W_S_E_D_S(str[]).");

		// Prepend and append "%" for the LIKE statement.
		workPackages = CustomSQLUtil.keywords(workPackages);
		descriptions = CustomSQLUtil.keywords(descriptions);

		Session session = null;

		try {
			session = openSession();
			Set<Long> taskRecordIds = new HashSet<Long>();
			taskRecordIds.addAll(countBy_C_G_U_W_S_E_D_S(session,
					companyId, groupId, userId, workPackages, endDate,
					startDate, descriptions, status, andOperator));

			_log.debug("taskRecords.size() = "
					+ taskRecordIds.size());

			return taskRecordIds.size();

		} catch (Exception e) {

			_log.error(e.getCause());
			throw new SystemException(e);

		} finally {

			closeSession(session);

		}
	}

	/**
	 * Return the number of description records matching the given parameters.
	 * 
	 * @param session
	 * @param companyId
	 * @param groupId
	 * @param userId
	 * @param workPackages
	 * @param endDate
	 * @param startDate
	 * @param descriptions
	 * @param status
	 * @param andOperator
	 *            AND or OR.
	 * @return the number of description records matching the given parameters.
	 * @since 1.0
	 * @throws SystemException
	 */
	public List<Long> countBy_C_G_U_W_S_E_D_S(Session session, long companyId,
			long groupId, long userId, String[] workPackages, Date startDate,
			Date endDate, String[] descriptions, int status,
			boolean andOperator) throws SystemException {

		_log.info("Executing countBy_C_G_U_W_S_E_D_S(session,str[]).");

		String stmt = whereBy_C_G_U_W_S_E_D_S(companyId, groupId, userId,
				workPackages, startDate, endDate, descriptions, status,
				andOperator, null);
		
		Date[] startDates = new Date[1];
		startDates[0] = startDate; 
		
		Date[] endDates = new Date[1]; 
		endDates[0] = endDate; 

		// The sqlQuery object containing all the info for the SELECT.
		SQLQuery sqlQuery = session.createSQLQuery(stmt);

		sqlQuery.addScalar(TaskRecordFields.TASK_RECORD_ID, Type.LONG);

		// One QueryPos per Query, controlling the values of the statement..
		QueryPos queryPos = QueryPos.getInstance(sqlQuery);

		_log.debug("stmt = " + stmt);

		// Replace all the "?" in the query (2x for each regular field)
        queryPos.add(companyId);
        queryPos.add(groupId);		
		queryPos.add(workPackages, 2);
		queryPos.add(descriptions, 2);
		queryPos.add(startDates, 2); 
		queryPos.add(endDates, 2);

		if (userId > 0) {
			queryPos.add(userId); 
		}
		if (status != WorkflowConstants.STATUS_ANY) {
			queryPos.add(status);
		}

		// now get the list (via javax.persistence.Query) for the here
		// unmodifiable query and return it right away.
		return sqlQuery.list(true);

	}

	/**
	 * Return the number of description records found for the given keywords.
	 * 
	 * @param companyId
	 * @param groupId
	 * @param keywords
	 * @param status
	 * @return number of description records found for the given keywords.
	 * @since 1.0
	 * @throws SystemException
	 */
	public int countByKeywords(long companyId, long groupId, long userId,
			String keywords, Date endDate, Date startDate, int status)
			throws SystemException {

		_log.info("Executing countByKeywords(" + keywords + ", "
				+ String.valueOf(status) + ".");

		String[] args = null;

		boolean andOperator = false;

		if (Validator.isNotNull(keywords)) {

			// Prepend and append "%" for the LIKE statement.
			args = CustomSQLUtil.keywords(keywords);

			_log.debug("args = " + args.toString());

		} else {

			andOperator = true;
		}

		return countBy_C_G_U_W_S_E_D_S(companyId, groupId, userId, args,
				endDate, startDate, args, status, andOperator);
	}
	
	/**
	 * Return the list of TaskRecords for the given parameters.
	 * 
	 * @param companyId
	 * @param groupId
	 * @param userId
	 * @param workPackage
	 * @param startDate
	 * @param endDate
	 * @param description
	 * @param status
	 * @param andOperator
	 * @param start
	 * @param end
	 * @param obc
	 * @return the list of TaskRecords for the given parameters.
	 * @since 1.0
	 * @throws SystemException
	 */
	public List<TaskRecord> findBy_C_G_U_W_S_E_D_S(long companyId,
			long groupId, long userId, String workPackage, Date startDate,
			Date endDate, String description, int status, boolean andOperator,
			int start, int end, OrderByComparator obc) throws SystemException {

		_log.info("Executing findBy_C_G_U_W_S_E_D_S(str).");
		
        // Prepend and append % to the string arguments
        // and convert the string to lowercase.
        String[] workPackages = CustomSQLUtil.keywords(workPackage);
        String[] descriptions = CustomSQLUtil.keywords(description);
		
		return findBy_C_G_U_W_S_E_D_S(companyId, groupId, userId, workPackages,
				startDate, endDate, descriptions, status, andOperator, start,
				end, obc);
		
	}
	
	public List<TaskRecord> findBy_C_G_U_W_S_E_D_S(
			long companyId, long groupId, long userId, String[] workPackages,
			Date startDate, Date endDate, String[] descriptions, int status,
			boolean andOperator, int start, int end, OrderByComparator obc)
			throws SystemException {
		
		_log.info("Executing findBy_C_G_U_W_S_E_D_S(String[]).");
		
		Session session = null;

		try {
			session = openSession();

			List<Long> taskRecordIds = findBy_C_G_U_W_S_E_D_S(session,
					companyId, groupId, userId, workPackages, startDate,
					endDate, descriptions, status, andOperator, start, end, obc);					
					
			_log.debug("taskRecordIds.size() = " + taskRecordIds.size());

			List<TaskRecord> taskRecords = new ArrayList<TaskRecord>(
			        taskRecordIds.size());

			for (Long taskRecordId : taskRecordIds) {
				
				TaskRecord taskRecord = TaskRecordUtil
				        .findByPrimaryKey(taskRecordId);
				taskRecords.add(taskRecord);
			}

			return taskRecords;

		} catch (Exception e) {
			throw new SystemException(e);
		} finally {
			closeSession(session);
		}
	}
	
	/**
	 * Return the list of TaskRecords for the given parameters.
	 *
	 * @param session
	 * @param companyId
	 * @param groupId
	 * @param userId
	 * @param workPackages
	 * @param startDate
	 * @param endDate
	 * @param descriptions
	 * @param status
	 * @param andOperator
	 * @param start
	 * @param end
	 * @param obc
	 * @return the list of TaskRecords for the given parameters.
	 * @since 1.0
	 * @throws SystemException
	 */
	public List<Long> findBy_C_G_U_W_S_E_D_S(Session session,
			long companyId, long groupId, long userId, String[] workPackages,
			Date startDate, Date endDate, String[] descriptions, int status,
			boolean andOperator, int start, int end, OrderByComparator obc)
			throws SystemException {

		_log.info("Executing findBy_C_G_U_W_S_E_D_S(String[]).");
		
		// Prepend and append % to the string arguments
		workPackages = CustomSQLUtil.keywords(workPackages);
		descriptions = CustomSQLUtil.keywords(descriptions); 
		
		Date[] startDates = new Date[1];
		startDates[0] = startDate; 
		
		Date[] endDates = new Date[1]; 
		endDates[0] = endDate; 
		
		String stmt = whereBy_C_G_U_W_S_E_D_S(companyId, groupId, userId,
				workPackages, startDate, endDate, descriptions, status,
				andOperator, obc);
		
		// The sqlQuery object containing all the information for the SELECT.
		SQLQuery sqlQuery = session.createSQLQuery(stmt);

		// Make sure to get the single value of interest.
		sqlQuery.addScalar("taskRecordId", Type.LONG);
		
		// One QueryPos per Query, controlling the values of the statement..
		QueryPos queryPos = QueryPos.getInstance(sqlQuery);
		
		// Replace all the "?" in the query (2x for each field)
        queryPos.add(companyId);
        queryPos.add(groupId);
		queryPos.add(workPackages, 2);
		queryPos.add(descriptions, 2);
		queryPos.add(startDates, 2); 
		queryPos.add(endDates, 2); 
		
		if (userId > 0) {
			queryPos.add(userId); 
		}

		if (status != WorkflowConstants.STATUS_ANY) {
			queryPos.add(status);
		}
		
		return (List<Long>) QueryUtil.list(sqlQuery, getDialect(), start, end);

	}

	/**
	 * Return a list of description record ids found for the given keywords.
	 * 
	 * @param companyId
	 * @param groupId
	 * @param keywords
	 * @param status
	 * @param start
	 * @param end
	 * @param obc
	 * @return a list of description record ids found for the given keywords.
	 * @since 1.0
	 * @throws SystemException
	 */
	public List<TaskRecord> findByKeywords(long companyId, long groupId,
			long userId, String keywords, int status, int start, int end,
			OrderByComparator obc) throws SystemException {

		_log.debug("Executing findByKeywords(" + keywords + ", "
				+ String.valueOf(status) + ").");

        String[] args = null;

        boolean andOperator = false;

        if (Validator.isNotNull(keywords)) {

            // Prepend and append "%" for the LIKE statement.
            args = CustomSQLUtil.keywords(keywords);

            _log.debug("args = " + args.toString());

        } else {
            andOperator = true;
        }
        
		return findBy_C_G_U_W_S_E_D_S(companyId, groupId, userId, keywords,
				null, null, keywords, status, andOperator, start, end, obc);

	}

	/**
	 * Return the where phrase of the statement.
	 * 
	 * @param companyId
	 * @param groupId
	 * @param userId
	 * @param workPackages
	 * @param endDate
	 * @param startDate
	 * @param descriptions
	 * @param status
	 * @param andOperator
	 * @param obc
	 * @return the where phrase of the statement.
	 * @since 1.0
	 */
	public String whereBy_C_G_U_W_S_E_D_S(long companyId, long groupId,
			long userId, String[] workPackages, Date startDate, Date endDate,
			String[] descriptions, int status, boolean andOperator,
			OrderByComparator obc) {

		_log.info("Executing whereBy_C_G_U_W_S_E_D_S().");
		
		_log.debug("companyId = " + companyId); 
		_log.debug("groupId = " + groupId); 
		_log.debug("userId = " + userId); 
		_log.debug("workPackages = " + workPackages); 
		_log.debug("startDate = " + startDate); 
		_log.debug("endDate = " + endDate); 
		_log.debug("descriptions = " + descriptions); 
		_log.debug("status = " + status); 

		if (workPackages != null) {
			_log.debug("workPackages.length = " + workPackages.length);
		}
		if (descriptions != null) {
			_log.debug("descriptions.length = " + descriptions.length);
		}

		// Retrieve the select part of the query from
		// the custom-sql configuration
		String select = CustomSQLUtil.get(SELECT_STMT);
		_log.debug("SELECT: " + select);

		// Retrieve the where part of the query from
		// the custom-sql configuration.
		String where = CustomSQLUtil.get(WHERE_BY_C_G_U_W_S_E_D_S);
		_log.debug("WHERE: " + where);

		// Repeat the place holder according to the number of
		// keywords for each field.
		where = CustomSQLUtil.replaceKeywords(where, "lower(workPackage)",
		        StringPool.LIKE, false, workPackages);

		where = CustomSQLUtil.replaceKeywords(where, "lower(description)",
		        StringPool.LIKE, false, descriptions); 

		_log.debug(where);

		if (userId < 1) {
			where = StringUtil.replace(where, USER_ID_SQL, StringPool.BLANK);
		}
		
		if (status == WorkflowConstants.STATUS_ANY) {
			where = StringUtil.replace(where, STATUS_SQL, StringPool.BLANK);
		}

		// Replace the and / or placeholders
		where = CustomSQLUtil.replaceAndOperator(where, andOperator);

		_log.debug(where);

		// Collect the strings and merge the parts.
		StringBuffer sb = new StringBuffer();
		sb.append(select);
		sb.append(" ");
		sb.append(where);

		if (obc != null) {

			sb.append(" ORDER BY ");
			sb.append(obc.toString());
		}

		String stmt = sb.toString();

		_log.debug(stmt);

		return stmt;
	}

}
