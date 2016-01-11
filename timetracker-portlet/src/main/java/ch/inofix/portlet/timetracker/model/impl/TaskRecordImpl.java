package ch.inofix.portlet.timetracker.model.impl;

/**
 * The extended model implementation for the TaskRecord service. Represents a
 * row in the &quot;tt_TaskRecord&quot; database table, with each column mapped
 * to a property of this class.
 * 
 * <p>
 * Helper methods and all application logic should be put in this class.
 * Whenever methods are added, rerun ServiceBuilder to copy their definitions
 * into the {@link ch.inofix.portlet.timetracker.model.TaskRecord} interface.
 * </p>
 * 
 * @author Christian Berndt
 * @created 2013-10-07 19:14
 * @modified 2014-02-02 15:46
 * @version 1.1
 */
@SuppressWarnings("serial")
public class TaskRecordImpl extends TaskRecordBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 * 
	 * Never reference this class directly. All methods that expect a task
	 * record model instance should use the {@link
	 * ch.inofix.portlet.timetracker.model.TaskRecord} interface instead.
	 */
	public TaskRecordImpl() {
	}
	
	/**
	 * Return the duration of the task in hours 
	 * (instead of milliseconds).
	 * 
	 * @return the duration of the task in hours.
	 * @since 1.1
	 */
	public double getDurationInHours() {
		
		double hours = 0; 
		long minutes = getDurationInMinutes(); 
		
		if (minutes > 0) {
			hours = ((double) minutes) / 60; 
		}
		
		return hours; 
	}

	/**
	 * Return the duration of the task in minutes 
	 * (instead of milliseconds).
	 * 
	 * @return the duration of the task in minutes.
	 * @since 1.0
	 */
	public long getDurationInMinutes() {
		return getDuration() / (60 * 1000);
	}
}
