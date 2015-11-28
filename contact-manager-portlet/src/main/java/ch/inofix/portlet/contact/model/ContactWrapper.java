package ch.inofix.portlet.contact.model;

import com.liferay.portal.kernel.lar.StagedModelType;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.ModelWrapper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * This class is a wrapper for {@link Contact}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see Contact
 * @generated
 */
public class ContactWrapper implements Contact, ModelWrapper<Contact> {
    private Contact _contact;

    public ContactWrapper(Contact contact) {
        _contact = contact;
    }

    @Override
    public Class<?> getModelClass() {
        return Contact.class;
    }

    @Override
    public String getModelClassName() {
        return Contact.class.getName();
    }

    @Override
    public Map<String, Object> getModelAttributes() {
        Map<String, Object> attributes = new HashMap<String, Object>();

        attributes.put("uuid", getUuid());
        attributes.put("contactId", getContactId());
        attributes.put("groupId", getGroupId());
        attributes.put("companyId", getCompanyId());
        attributes.put("userId", getUserId());
        attributes.put("userName", getUserName());
        attributes.put("createDate", getCreateDate());
        attributes.put("modifiedDate", getModifiedDate());
        attributes.put("parentContactId", getParentContactId());
        attributes.put("card", getCard());
        attributes.put("uid", getUid());
        attributes.put("status", getStatus());

        return attributes;
    }

    @Override
    public void setModelAttributes(Map<String, Object> attributes) {
        String uuid = (String) attributes.get("uuid");

        if (uuid != null) {
            setUuid(uuid);
        }

        Long contactId = (Long) attributes.get("contactId");

        if (contactId != null) {
            setContactId(contactId);
        }

        Long groupId = (Long) attributes.get("groupId");

        if (groupId != null) {
            setGroupId(groupId);
        }

        Long companyId = (Long) attributes.get("companyId");

        if (companyId != null) {
            setCompanyId(companyId);
        }

        Long userId = (Long) attributes.get("userId");

        if (userId != null) {
            setUserId(userId);
        }

        String userName = (String) attributes.get("userName");

        if (userName != null) {
            setUserName(userName);
        }

        Date createDate = (Date) attributes.get("createDate");

        if (createDate != null) {
            setCreateDate(createDate);
        }

        Date modifiedDate = (Date) attributes.get("modifiedDate");

        if (modifiedDate != null) {
            setModifiedDate(modifiedDate);
        }

        Long parentContactId = (Long) attributes.get("parentContactId");

        if (parentContactId != null) {
            setParentContactId(parentContactId);
        }

        String card = (String) attributes.get("card");

        if (card != null) {
            setCard(card);
        }

        String uid = (String) attributes.get("uid");

        if (uid != null) {
            setUid(uid);
        }

        Integer status = (Integer) attributes.get("status");

        if (status != null) {
            setStatus(status);
        }
    }

    /**
    * Returns the primary key of this contact.
    *
    * @return the primary key of this contact
    */
    @Override
    public long getPrimaryKey() {
        return _contact.getPrimaryKey();
    }

    /**
    * Sets the primary key of this contact.
    *
    * @param primaryKey the primary key of this contact
    */
    @Override
    public void setPrimaryKey(long primaryKey) {
        _contact.setPrimaryKey(primaryKey);
    }

    /**
    * Returns the uuid of this contact.
    *
    * @return the uuid of this contact
    */
    @Override
    public java.lang.String getUuid() {
        return _contact.getUuid();
    }

    /**
    * Sets the uuid of this contact.
    *
    * @param uuid the uuid of this contact
    */
    @Override
    public void setUuid(java.lang.String uuid) {
        _contact.setUuid(uuid);
    }

    /**
    * Returns the contact ID of this contact.
    *
    * @return the contact ID of this contact
    */
    @Override
    public long getContactId() {
        return _contact.getContactId();
    }

    /**
    * Sets the contact ID of this contact.
    *
    * @param contactId the contact ID of this contact
    */
    @Override
    public void setContactId(long contactId) {
        _contact.setContactId(contactId);
    }

    /**
    * Returns the group ID of this contact.
    *
    * @return the group ID of this contact
    */
    @Override
    public long getGroupId() {
        return _contact.getGroupId();
    }

    /**
    * Sets the group ID of this contact.
    *
    * @param groupId the group ID of this contact
    */
    @Override
    public void setGroupId(long groupId) {
        _contact.setGroupId(groupId);
    }

    /**
    * Returns the company ID of this contact.
    *
    * @return the company ID of this contact
    */
    @Override
    public long getCompanyId() {
        return _contact.getCompanyId();
    }

    /**
    * Sets the company ID of this contact.
    *
    * @param companyId the company ID of this contact
    */
    @Override
    public void setCompanyId(long companyId) {
        _contact.setCompanyId(companyId);
    }

    /**
    * Returns the user ID of this contact.
    *
    * @return the user ID of this contact
    */
    @Override
    public long getUserId() {
        return _contact.getUserId();
    }

    /**
    * Sets the user ID of this contact.
    *
    * @param userId the user ID of this contact
    */
    @Override
    public void setUserId(long userId) {
        _contact.setUserId(userId);
    }

    /**
    * Returns the user uuid of this contact.
    *
    * @return the user uuid of this contact
    * @throws SystemException if a system exception occurred
    */
    @Override
    public java.lang.String getUserUuid()
        throws com.liferay.portal.kernel.exception.SystemException {
        return _contact.getUserUuid();
    }

    /**
    * Sets the user uuid of this contact.
    *
    * @param userUuid the user uuid of this contact
    */
    @Override
    public void setUserUuid(java.lang.String userUuid) {
        _contact.setUserUuid(userUuid);
    }

    /**
    * Returns the user name of this contact.
    *
    * @return the user name of this contact
    */
    @Override
    public java.lang.String getUserName() {
        return _contact.getUserName();
    }

    /**
    * Sets the user name of this contact.
    *
    * @param userName the user name of this contact
    */
    @Override
    public void setUserName(java.lang.String userName) {
        _contact.setUserName(userName);
    }

    /**
    * Returns the create date of this contact.
    *
    * @return the create date of this contact
    */
    @Override
    public java.util.Date getCreateDate() {
        return _contact.getCreateDate();
    }

    /**
    * Sets the create date of this contact.
    *
    * @param createDate the create date of this contact
    */
    @Override
    public void setCreateDate(java.util.Date createDate) {
        _contact.setCreateDate(createDate);
    }

    /**
    * Returns the modified date of this contact.
    *
    * @return the modified date of this contact
    */
    @Override
    public java.util.Date getModifiedDate() {
        return _contact.getModifiedDate();
    }

    /**
    * Sets the modified date of this contact.
    *
    * @param modifiedDate the modified date of this contact
    */
    @Override
    public void setModifiedDate(java.util.Date modifiedDate) {
        _contact.setModifiedDate(modifiedDate);
    }

    /**
    * Returns the parent contact ID of this contact.
    *
    * @return the parent contact ID of this contact
    */
    @Override
    public long getParentContactId() {
        return _contact.getParentContactId();
    }

    /**
    * Sets the parent contact ID of this contact.
    *
    * @param parentContactId the parent contact ID of this contact
    */
    @Override
    public void setParentContactId(long parentContactId) {
        _contact.setParentContactId(parentContactId);
    }

    /**
    * Returns the card of this contact.
    *
    * @return the card of this contact
    */
    @Override
    public java.lang.String getCard() {
        return _contact.getCard();
    }

    /**
    * Sets the card of this contact.
    *
    * @param card the card of this contact
    */
    @Override
    public void setCard(java.lang.String card) {
        _contact.setCard(card);
    }

    /**
    * Returns the uid of this contact.
    *
    * @return the uid of this contact
    */
    @Override
    public java.lang.String getUid() {
        return _contact.getUid();
    }

    /**
    * Sets the uid of this contact.
    *
    * @param uid the uid of this contact
    */
    @Override
    public void setUid(java.lang.String uid) {
        _contact.setUid(uid);
    }

    /**
    * Returns the status of this contact.
    *
    * @return the status of this contact
    */
    @Override
    public int getStatus() {
        return _contact.getStatus();
    }

    /**
    * Sets the status of this contact.
    *
    * @param status the status of this contact
    */
    @Override
    public void setStatus(int status) {
        _contact.setStatus(status);
    }

    @Override
    public boolean isNew() {
        return _contact.isNew();
    }

    @Override
    public void setNew(boolean n) {
        _contact.setNew(n);
    }

    @Override
    public boolean isCachedModel() {
        return _contact.isCachedModel();
    }

    @Override
    public void setCachedModel(boolean cachedModel) {
        _contact.setCachedModel(cachedModel);
    }

    @Override
    public boolean isEscapedModel() {
        return _contact.isEscapedModel();
    }

    @Override
    public java.io.Serializable getPrimaryKeyObj() {
        return _contact.getPrimaryKeyObj();
    }

    @Override
    public void setPrimaryKeyObj(java.io.Serializable primaryKeyObj) {
        _contact.setPrimaryKeyObj(primaryKeyObj);
    }

    @Override
    public com.liferay.portlet.expando.model.ExpandoBridge getExpandoBridge() {
        return _contact.getExpandoBridge();
    }

    @Override
    public void setExpandoBridgeAttributes(
        com.liferay.portal.model.BaseModel<?> baseModel) {
        _contact.setExpandoBridgeAttributes(baseModel);
    }

    @Override
    public void setExpandoBridgeAttributes(
        com.liferay.portlet.expando.model.ExpandoBridge expandoBridge) {
        _contact.setExpandoBridgeAttributes(expandoBridge);
    }

    @Override
    public void setExpandoBridgeAttributes(
        com.liferay.portal.service.ServiceContext serviceContext) {
        _contact.setExpandoBridgeAttributes(serviceContext);
    }

    @Override
    public java.lang.Object clone() {
        return new ContactWrapper((Contact) _contact.clone());
    }

    @Override
    public int compareTo(ch.inofix.portlet.contact.model.Contact contact) {
        return _contact.compareTo(contact);
    }

    @Override
    public int hashCode() {
        return _contact.hashCode();
    }

    @Override
    public com.liferay.portal.model.CacheModel<ch.inofix.portlet.contact.model.Contact> toCacheModel() {
        return _contact.toCacheModel();
    }

    @Override
    public ch.inofix.portlet.contact.model.Contact toEscapedModel() {
        return new ContactWrapper(_contact.toEscapedModel());
    }

    @Override
    public ch.inofix.portlet.contact.model.Contact toUnescapedModel() {
        return new ContactWrapper(_contact.toUnescapedModel());
    }

    @Override
    public java.lang.String toString() {
        return _contact.toString();
    }

    @Override
    public java.lang.String toXmlString() {
        return _contact.toXmlString();
    }

    @Override
    public void persist()
        throws com.liferay.portal.kernel.exception.SystemException {
        _contact.persist();
    }

    /**
    * @return the preferred address.
    * @since 1.0.8
    */
    @Override
    public ch.inofix.portlet.contact.dto.AddressDTO getAddress() {
        return _contact.getAddress();
    }

    /**
    * @return
    * @since 1.0.0
    */
    @Override
    public java.util.List<ch.inofix.portlet.contact.dto.AddressDTO> getAddresses() {
        return _contact.getAddresses();
    }

    /**
    * @return
    * @since 1.0.0
    */
    @Override
    public int getAnniversaryDay() {
        return _contact.getAnniversaryDay();
    }

    /**
    * @return
    * @since 1.0.0
    */
    @Override
    public int getAnniversaryMonth() {
        return _contact.getAnniversaryMonth();
    }

    /**
    * @return
    * @since 1.0.0
    */
    @Override
    public int getAnniversaryYear() {
        return _contact.getAnniversaryYear();
    }

    /**
    * @return
    * @since 1.0.0
    */
    @Override
    public int getBirthdayDay() {
        return _contact.getBirthdayDay();
    }

    /**
    * @return
    * @since 1.0.0
    */
    @Override
    public int getBirthdayMonth() {
        return _contact.getBirthdayMonth();
    }

    /**
    * @return
    * @since 1.0.0
    */
    @Override
    public int getBirthdayYear() {
        return _contact.getBirthdayYear();
    }

    /**
    * @return
    * @since 1.0.5
    */
    @Override
    public java.lang.String getBirthplace() {
        return _contact.getBirthplace();
    }

    /**
    * @since 1.0.0
    */
    @Override
    public java.util.List<ch.inofix.portlet.contact.dto.UriDTO> getCalendarRequestUris() {
        return _contact.getCalendarRequestUris();
    }

    /**
    * @return
    * @since 1.0.0
    */
    @Override
    public java.util.List<ch.inofix.portlet.contact.dto.UriDTO> getCalendarUris() {
        return _contact.getCalendarUris();
    }

    /**
    * @return
    * @since 1.1.0
    */
    @Override
    public java.util.List<ch.inofix.portlet.contact.dto.CategoriesDTO> getCategoriesList() {
        return _contact.getCategoriesList();
    }

    /**
    * @return
    * @since 1.0.0
    */
    @Override
    public java.lang.String getCompany() {
        return _contact.getCompany();
    }

    /**
    * @return
    * @since 1.0.0
    */
    @Override
    public int getDeathdateDay() {
        return _contact.getDeathdateDay();
    }

    /**
    * @return
    * @since 1.0.0
    */
    @Override
    public int getDeathdateMonth() {
        return _contact.getDeathdateMonth();
    }

    /**
    * @return
    * @since 1.0.0
    */
    @Override
    public int getDeathdateYear() {
        return _contact.getDeathdateYear();
    }

    /**
    * @return
    * @since 1.0.0
    */
    @Override
    public java.lang.String getDepartment() {
        return _contact.getDepartment();
    }

    /**
    * @return
    * @since 1.0.5
    */
    @Override
    public java.lang.String getDeathplace() {
        return _contact.getDeathplace();
    }

    /**
    * @return the preferred email.
    * @since 1.0.8
    */
    @Override
    public ch.inofix.portlet.contact.dto.EmailDTO getEmail() {
        return _contact.getEmail();
    }

    /**
    * @return
    * @since 1.0.0
    */
    @Override
    public java.util.List<ch.inofix.portlet.contact.dto.EmailDTO> getEmails() {
        return _contact.getEmails();
    }

    /**
    * @return
    * @since 1.0.0
    */
    @Override
    public java.util.List<ch.inofix.portlet.contact.dto.ExpertiseDTO> getExpertises() {
        return _contact.getExpertises();
    }

    /**
    * @return
    * @since 1.0.0
    */
    @Override
    public java.lang.String getFormattedName() {
        return _contact.getFormattedName();
    }

    /**
    * @return
    * @since 1.0.6
    */
    @Override
    public java.util.List<ch.inofix.portlet.contact.dto.UrlDTO> getFreeBusyUrls() {
        return _contact.getFreeBusyUrls();
    }

    /**
    * @return
    * @since 1.0.0
    */
    @Override
    public java.lang.String getFullName() {
        return _contact.getFullName();
    }

    /**
    * @param firstLast
    * @return
    * @since 1.0.0
    */
    @Override
    public java.lang.String getFullName(boolean firstLast) {
        return _contact.getFullName(firstLast);
    }

    /**
    * @return
    * @since 1.1.5
    */
    @Override
    public java.lang.String getGender() {
        return _contact.getGender();
    }

    /**
    * @return
    * @since 1.0.0
    */
    @Override
    public java.util.List<ch.inofix.portlet.contact.dto.HobbyDTO> getHobbies() {
        return _contact.getHobbies();
    }

    /**
    * @return
    * @since 1.0.0
    */
    @Override
    public java.util.List<ch.inofix.portlet.contact.dto.ImppDTO> getImpps() {
        return _contact.getImpps();
    }

    @Override
    public java.util.List<ch.inofix.portlet.contact.dto.FileDTO> getKeys() {
        return _contact.getKeys();
    }

    /**
    * @return
    * @since 1.0.0
    */
    @Override
    public java.lang.String getKind() {
        return _contact.getKind();
    }

    /**
    * @return
    * @since 1.0.0
    */
    @Override
    public java.util.List<ch.inofix.portlet.contact.dto.InterestDTO> getInterests() {
        return _contact.getInterests();
    }

    /**
    * @return
    * @since 1.1.1
    */
    @Override
    public java.util.List<ch.inofix.portlet.contact.dto.LanguageDTO> getLanguages() {
        return _contact.getLanguages();
    }

    /**
    * @return
    * @since 1.1.3
    */
    @Override
    public java.util.List<ch.inofix.portlet.contact.dto.FileDTO> getLogos() {
        return _contact.getLogos();
    }

    /**
    * @return
    * @since 1.0.4
    */
    @Override
    public java.lang.String getName() {
        return _contact.getName();
    }

    /**
    * @return
    * @since 1.0.0
    */
    @Override
    public java.lang.String getNickname() {
        return _contact.getNickname();
    }

    /**
    * @return
    * @since 1.0.0
    */
    @Override
    public java.util.List<ch.inofix.portlet.contact.dto.NoteDTO> getNotes() {
        return _contact.getNotes();
    }

    /**
    * @return
    * @since 1.0.0
    */
    @Override
    public java.lang.String getOffice() {
        return _contact.getOffice();
    }

    /**
    * @return the preferred phone.
    * @since 1.0.8
    */
    @Override
    public ch.inofix.portlet.contact.dto.PhoneDTO getPhone() {
        return _contact.getPhone();
    }

    /**
    * @return
    * @since 1.0.0
    */
    @Override
    public java.util.List<ch.inofix.portlet.contact.dto.PhoneDTO> getPhones() {
        return _contact.getPhones();
    }

    /**
    * @return
    * @since 1.1.2
    */
    @Override
    public java.util.List<ch.inofix.portlet.contact.dto.FileDTO> getPhotos() {
        return _contact.getPhotos();
    }

    /**
    * @return a dataURI for the entity the vCard represents, i.e. the first
    photo if the vCard represents a person or a logo if the vCard
    represents an organization.
    * @since 1.1.6
    */
    @Override
    public java.lang.String getPortrait() {
        return _contact.getPortrait();
    }

    /**
    * @return
    * @since 1.0.0
    */
    @Override
    public java.lang.String getRole() {
        return _contact.getRole();
    }

    /**
    * @return
    * @since 1.0.0
    */
    @Override
    public java.lang.String getSortAs() {
        return _contact.getSortAs();
    }

    /**
    * @return
    * @since 1.1.3
    */
    @Override
    public java.util.List<ch.inofix.portlet.contact.dto.FileDTO> getSounds() {
        return _contact.getSounds();
    }

    /**
    * @return
    * @since 1.0.0
    */
    @Override
    public ch.inofix.portlet.contact.dto.StructuredNameDTO getStructuredName() {
        return _contact.getStructuredName();
    }

    /**
    * @return
    * @since 1.0.0
    */
    @Override
    public java.lang.String getTitle() {
        return _contact.getTitle();
    }

    /**
    * @return
    * @since 1.0.0
    */
    @Override
    public java.lang.String getTimezone() {
        return _contact.getTimezone();
    }

    /**
    * @since 1.0.0
    */
    @Override
    public ezvcard.VCard getVCard() {
        return _contact.getVCard();
    }

    /**
    * @since 1.0.0
    */
    @Override
    public java.lang.String getVCardHTML() {
        return _contact.getVCardHTML();
    }

    /**
    * @since 1.0.0
    */
    @Override
    public java.util.List<ch.inofix.portlet.contact.dto.UrlDTO> getUrls() {
        return _contact.getUrls();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof ContactWrapper)) {
            return false;
        }

        ContactWrapper contactWrapper = (ContactWrapper) obj;

        if (Validator.equals(_contact, contactWrapper._contact)) {
            return true;
        }

        return false;
    }

    @Override
    public StagedModelType getStagedModelType() {
        return _contact.getStagedModelType();
    }

    /**
     * @deprecated As of 6.1.0, replaced by {@link #getWrappedModel}
     */
    public Contact getWrappedContact() {
        return _contact;
    }

    @Override
    public Contact getWrappedModel() {
        return _contact;
    }

    @Override
    public void resetOriginalValues() {
        _contact.resetOriginalValues();
    }
}
