package ch.inofix.portlet.contact.model;

import com.liferay.portal.model.PersistedModel;

/**
 * The extended model interface for the Contact service. Represents a row in the &quot;Inofix_Contact&quot; database table, with each column mapped to a property of this class.
 *
 * @author Brian Wing Shun Chan
 * @see ContactModel
 * @see ch.inofix.portlet.contact.model.impl.ContactImpl
 * @see ch.inofix.portlet.contact.model.impl.ContactModelImpl
 * @generated
 */
public interface Contact extends ContactModel, PersistedModel {
    /*
     * NOTE FOR DEVELOPERS:
     *
     * Never modify this interface directly. Add methods to {@link ch.inofix.portlet.contact.model.impl.ContactImpl} and rerun ServiceBuilder to automatically copy the method declarations to this interface.
     */

    /**
    * @return the preferred address.
    * @since 1.0.8
    */
    public ch.inofix.portlet.contact.dto.AddressDTO getAddress();

    /**
    * @return
    * @since 1.0.0
    */
    public java.util.List<ch.inofix.portlet.contact.dto.AddressDTO> getAddresses();

    /**
    * @return
    * @since 1.0.0
    */
    public int getAnniversaryDay();

    /**
    * @return
    * @since 1.0.0
    */
    public int getAnniversaryMonth();

    /**
    * @return
    * @since 1.0.0
    */
    public int getAnniversaryYear();

    /**
    * @return
    * @since 1.0.0
    */
    public int getBirthdayDay();

    /**
    * @return
    * @since 1.0.0
    */
    public int getBirthdayMonth();

    /**
    * @return
    * @since 1.0.0
    */
    public int getBirthdayYear();

    /**
    * @return
    * @since 1.0.5
    */
    public java.lang.String getBirthplace();

    /**
    * @since 1.0.0
    */
    public java.util.List<ch.inofix.portlet.contact.dto.UriDTO> getCalendarRequestUris();

    /**
    * @return
    * @since 1.0.0
    */
    public java.util.List<ch.inofix.portlet.contact.dto.UriDTO> getCalendarUris();

    /**
    * @return
    * @since 1.1.0
    */
    public java.util.List<ch.inofix.portlet.contact.dto.CategoriesDTO> getCategoriesList();

    /**
    * @return
    * @since 1.0.0
    */
    public java.lang.String getCompany();

    /**
    * @return
    * @since 1.0.0
    */
    public int getDeathdateDay();

    /**
    * @return
    * @since 1.0.0
    */
    public int getDeathdateMonth();

    /**
    * @return
    * @since 1.0.0
    */
    public int getDeathdateYear();

    /**
    * @return
    * @since 1.0.0
    */
    public java.lang.String getDepartment();

    /**
    * @return
    * @since 1.0.5
    */
    public java.lang.String getDeathplace();

    /**
    * @return the preferred email.
    * @since 1.0.8
    */
    public ch.inofix.portlet.contact.dto.EmailDTO getEmail();

    /**
    * @return
    * @since 1.0.0
    */
    public java.util.List<ch.inofix.portlet.contact.dto.EmailDTO> getEmails();

    /**
    * @return
    * @since 1.0.0
    */
    public java.util.List<ch.inofix.portlet.contact.dto.ExpertiseDTO> getExpertises();

    /**
    * @return
    * @since 1.0.0
    */
    public java.lang.String getFormattedName();

    /**
    * @return
    * @since 1.0.6
    */
    public java.util.List<ch.inofix.portlet.contact.dto.UrlDTO> getFreeBusyUrls();

    /**
    * @return
    * @since 1.0.0
    */
    public java.lang.String getFullName();

    /**
    * @param firstLast
    * @return
    * @since 1.0.0
    */
    public java.lang.String getFullName(boolean firstLast);

    /**
    * @return
    * @since 1.1.5
    */
    public java.lang.String getGender();

    /**
    * @return
    * @since 1.0.0
    */
    public java.util.List<ch.inofix.portlet.contact.dto.HobbyDTO> getHobbies();

    /**
    * @return
    * @since 1.0.0
    */
    public java.util.List<ch.inofix.portlet.contact.dto.ImppDTO> getImpps();

    public java.util.List<ch.inofix.portlet.contact.dto.FileDTO> getKeys();

    /**
    * @return
    * @since 1.0.0
    */
    public java.lang.String getKind();

    /**
    * @return
    * @since 1.0.0
    */
    public java.util.List<ch.inofix.portlet.contact.dto.InterestDTO> getInterests();

    /**
    * @return
    * @since 1.1.1
    */
    public java.util.List<ch.inofix.portlet.contact.dto.LanguageDTO> getLanguages();

    /**
    * @return
    * @since 1.1.3
    */
    public java.util.List<ch.inofix.portlet.contact.dto.FileDTO> getLogos();

    /**
    * @return
    * @since 1.0.4
    */
    public java.lang.String getName();

    /**
    * @return
    * @since 1.0.0
    */
    public java.lang.String getNickname();

    /**
    * @return
    * @since 1.0.0
    */
    public java.util.List<ch.inofix.portlet.contact.dto.NoteDTO> getNotes();

    /**
    * @return
    * @since 1.0.0
    */
    public java.lang.String getOffice();

    /**
    * @return the preferred phone.
    * @since 1.0.8
    */
    public ch.inofix.portlet.contact.dto.PhoneDTO getPhone();

    /**
    * @return
    * @since 1.0.0
    */
    public java.util.List<ch.inofix.portlet.contact.dto.PhoneDTO> getPhones();

    /**
    * @return
    * @since 1.1.2
    */
    public java.util.List<ch.inofix.portlet.contact.dto.FileDTO> getPhotos();

    /**
    * @return a dataURI for the entity the vCard represents, i.e. the first
    photo if the vCard represents a person or a logo if the vCard
    represents an organization.
    * @since 1.1.6
    */
    public java.lang.String getPortrait();

    /**
    * @return
    * @since 1.0.0
    */
    public java.lang.String getRole();

    /**
    * @return
    * @since 1.0.0
    */
    public java.lang.String getSortAs();

    /**
    * @return
    * @since 1.1.3
    */
    public java.util.List<ch.inofix.portlet.contact.dto.FileDTO> getSounds();

    /**
    * @return
    * @since 1.0.0
    */
    public ch.inofix.portlet.contact.dto.StructuredNameDTO getStructuredName();

    /**
    * @return
    * @since 1.0.0
    */
    public java.lang.String getTitle();

    /**
    * @return
    * @since 1.0.0
    */
    public java.lang.String getTimezone();

    /**
    * @since 1.0.0
    */
    public ezvcard.VCard getVCard();

    /**
    * @since 1.0.0
    */
    public java.lang.String getVCardHTML();

    /**
    * @since 1.0.0
    */
    public java.util.List<ch.inofix.portlet.contact.dto.UrlDTO> getUrls();
}
