package ch.inofix.portlet.newsletter.model.impl;

import java.util.Calendar;
import java.util.Date;

/**
 * The extended model implementation for the Mailing service. Represents a row
 * in the &quot;inofix_Mailing&quot; database table, with each column mapped to
 * a property of this class.
 *
 * <p>
 * Helper methods and all application logic should be put in this class.
 * Whenever methods are added, rerun ServiceBuilder to copy their definitions
 * into the {@link ch.inofix.portlet.newsletter.model.Mailing} interface.
 * </p>
 *
 * @author Christian Berndt
 * @created 2016-10-22 17:27
 * @modified 2016-10-31 17:12
 * @version 1.0.2
 */
public class MailingImpl extends MailingBaseImpl {
    /*
     * NOTE FOR DEVELOPERS:
     * 
     * Never reference this class directly. All methods that expect a mailing
     * model instance should use the {@link
     * ch.inofix.portlet.newsletter.model.Mailing} interface instead.
     */
    public MailingImpl() {
    }

    /**
     * @return
     * @since 1.0.2
     */
    @Override
    public Date getPublishDate() {

        Date publishDate = super.getPublishDate();

        if (publishDate == null) {
            publishDate = new Date();
        }

        return publishDate;

    }

    /**
     * 
     * @return
     * @since 1.0.1
     */
    public int getPublishDateDay() {

        Date publishDate = getPublishDate();

        if (publishDate != null) {
            return getDay(publishDate);
        } else {
            return -1;
        }

    }

    /**
     * 
     * @return
     * @since 1.0.1
     */
    public int getPublishDateMonth() {

        Date publishDate = getPublishDate();

        if (publishDate != null) {
            return getMonth(publishDate);
        } else {
            return -1;
        }
    }

    /**
     * 
     * @return
     * @since 1.0.1
     */
    public int getPublishDateYear() {

        Date publishDate = getPublishDate();

        if (publishDate != null) {
            return getYear(publishDate);
        } else {
            return -1;
        }
    }

    /**
     * 
     * @return
     * @since 1.0.1
     */
    public int getSendDateDay() {

        Date sendDate = getSendDate();

        if (sendDate != null) {
            return getDay(sendDate);
        } else {
            return 1;
        }

    }

    /**
     * 
     * @return
     * @since 1.0.1
     */
    public int getSendDateMonth() {

        Date sendDate = getSendDate();

        if (sendDate != null) {
            return getMonth(sendDate);
        } else {
            return 0;
        }
    }

    /**
     * 
     * @return
     * @since 1.0.1
     */
    public int getSendDateYear() {

        Date sendDate = getSendDate();

        if (sendDate != null) {
            return getYear(sendDate);
        } else {
            return 1970;
        }
    }

    /**
     * 
     * @param date
     * @return
     * @since 1.0.0
     */
    private int getDay(Date date) {

        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal.get(Calendar.DAY_OF_MONTH);
        } else {
            return 1;
        }
    }

    /**
     * 
     * @param date
     * @return
     * @since 1.0.1
     */
    private int getMonth(Date date) {

        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal.get(Calendar.MONTH);
        } else {
            return 0;
        }
    }

    /**
     * 
     * @param date
     * @return
     * @since 1.0.1
     */
    private int getYear(Date date) {

        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal.get(Calendar.YEAR);
        } else {
            return 1970;
        }
    }
}
