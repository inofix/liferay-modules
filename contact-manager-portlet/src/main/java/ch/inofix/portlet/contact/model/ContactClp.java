package ch.inofix.portlet.contact.model;

import ch.inofix.portlet.contact.service.ClpSerializer;
import ch.inofix.portlet.contact.service.ContactLocalServiceUtil;

import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.lar.StagedModelType;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.model.BaseModel;
import com.liferay.portal.model.impl.BaseModelImpl;
import com.liferay.portal.util.PortalUtil;

import java.io.Serializable;

import java.lang.reflect.Method;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class ContactClp extends BaseModelImpl<Contact> implements Contact {
    private String _uuid;
    private long _contactId;
    private long _groupId;
    private long _companyId;
    private long _userId;
    private String _userUuid;
    private String _userName;
    private Date _createDate;
    private Date _modifiedDate;
    private long _parentContactId;
    private String _card;
    private String _uid;
    private int _status;
    private BaseModel<?> _contactRemoteModel;
    private Class<?> _clpSerializerClass = ch.inofix.portlet.contact.service.ClpSerializer.class;

    public ContactClp() {
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
    public long getPrimaryKey() {
        return _contactId;
    }

    @Override
    public void setPrimaryKey(long primaryKey) {
        setContactId(primaryKey);
    }

    @Override
    public Serializable getPrimaryKeyObj() {
        return _contactId;
    }

    @Override
    public void setPrimaryKeyObj(Serializable primaryKeyObj) {
        setPrimaryKey(((Long) primaryKeyObj).longValue());
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

    @Override
    public String getUuid() {
        return _uuid;
    }

    @Override
    public void setUuid(String uuid) {
        _uuid = uuid;

        if (_contactRemoteModel != null) {
            try {
                Class<?> clazz = _contactRemoteModel.getClass();

                Method method = clazz.getMethod("setUuid", String.class);

                method.invoke(_contactRemoteModel, uuid);
            } catch (Exception e) {
                throw new UnsupportedOperationException(e);
            }
        }
    }

    @Override
    public long getContactId() {
        return _contactId;
    }

    @Override
    public void setContactId(long contactId) {
        _contactId = contactId;

        if (_contactRemoteModel != null) {
            try {
                Class<?> clazz = _contactRemoteModel.getClass();

                Method method = clazz.getMethod("setContactId", long.class);

                method.invoke(_contactRemoteModel, contactId);
            } catch (Exception e) {
                throw new UnsupportedOperationException(e);
            }
        }
    }

    @Override
    public long getGroupId() {
        return _groupId;
    }

    @Override
    public void setGroupId(long groupId) {
        _groupId = groupId;

        if (_contactRemoteModel != null) {
            try {
                Class<?> clazz = _contactRemoteModel.getClass();

                Method method = clazz.getMethod("setGroupId", long.class);

                method.invoke(_contactRemoteModel, groupId);
            } catch (Exception e) {
                throw new UnsupportedOperationException(e);
            }
        }
    }

    @Override
    public long getCompanyId() {
        return _companyId;
    }

    @Override
    public void setCompanyId(long companyId) {
        _companyId = companyId;

        if (_contactRemoteModel != null) {
            try {
                Class<?> clazz = _contactRemoteModel.getClass();

                Method method = clazz.getMethod("setCompanyId", long.class);

                method.invoke(_contactRemoteModel, companyId);
            } catch (Exception e) {
                throw new UnsupportedOperationException(e);
            }
        }
    }

    @Override
    public long getUserId() {
        return _userId;
    }

    @Override
    public void setUserId(long userId) {
        _userId = userId;

        if (_contactRemoteModel != null) {
            try {
                Class<?> clazz = _contactRemoteModel.getClass();

                Method method = clazz.getMethod("setUserId", long.class);

                method.invoke(_contactRemoteModel, userId);
            } catch (Exception e) {
                throw new UnsupportedOperationException(e);
            }
        }
    }

    @Override
    public String getUserUuid() throws SystemException {
        return PortalUtil.getUserValue(getUserId(), "uuid", _userUuid);
    }

    @Override
    public void setUserUuid(String userUuid) {
        _userUuid = userUuid;
    }

    @Override
    public String getUserName() {
        return _userName;
    }

    @Override
    public void setUserName(String userName) {
        _userName = userName;

        if (_contactRemoteModel != null) {
            try {
                Class<?> clazz = _contactRemoteModel.getClass();

                Method method = clazz.getMethod("setUserName", String.class);

                method.invoke(_contactRemoteModel, userName);
            } catch (Exception e) {
                throw new UnsupportedOperationException(e);
            }
        }
    }

    @Override
    public Date getCreateDate() {
        return _createDate;
    }

    @Override
    public void setCreateDate(Date createDate) {
        _createDate = createDate;

        if (_contactRemoteModel != null) {
            try {
                Class<?> clazz = _contactRemoteModel.getClass();

                Method method = clazz.getMethod("setCreateDate", Date.class);

                method.invoke(_contactRemoteModel, createDate);
            } catch (Exception e) {
                throw new UnsupportedOperationException(e);
            }
        }
    }

    @Override
    public Date getModifiedDate() {
        return _modifiedDate;
    }

    @Override
    public void setModifiedDate(Date modifiedDate) {
        _modifiedDate = modifiedDate;

        if (_contactRemoteModel != null) {
            try {
                Class<?> clazz = _contactRemoteModel.getClass();

                Method method = clazz.getMethod("setModifiedDate", Date.class);

                method.invoke(_contactRemoteModel, modifiedDate);
            } catch (Exception e) {
                throw new UnsupportedOperationException(e);
            }
        }
    }

    @Override
    public long getParentContactId() {
        return _parentContactId;
    }

    @Override
    public void setParentContactId(long parentContactId) {
        _parentContactId = parentContactId;

        if (_contactRemoteModel != null) {
            try {
                Class<?> clazz = _contactRemoteModel.getClass();

                Method method = clazz.getMethod("setParentContactId", long.class);

                method.invoke(_contactRemoteModel, parentContactId);
            } catch (Exception e) {
                throw new UnsupportedOperationException(e);
            }
        }
    }

    @Override
    public String getCard() {
        return _card;
    }

    @Override
    public void setCard(String card) {
        _card = card;

        if (_contactRemoteModel != null) {
            try {
                Class<?> clazz = _contactRemoteModel.getClass();

                Method method = clazz.getMethod("setCard", String.class);

                method.invoke(_contactRemoteModel, card);
            } catch (Exception e) {
                throw new UnsupportedOperationException(e);
            }
        }
    }

    @Override
    public String getUid() {
        return _uid;
    }

    @Override
    public void setUid(String uid) {
        _uid = uid;

        if (_contactRemoteModel != null) {
            try {
                Class<?> clazz = _contactRemoteModel.getClass();

                Method method = clazz.getMethod("setUid", String.class);

                method.invoke(_contactRemoteModel, uid);
            } catch (Exception e) {
                throw new UnsupportedOperationException(e);
            }
        }
    }

    @Override
    public int getStatus() {
        return _status;
    }

    @Override
    public void setStatus(int status) {
        _status = status;

        if (_contactRemoteModel != null) {
            try {
                Class<?> clazz = _contactRemoteModel.getClass();

                Method method = clazz.getMethod("setStatus", int.class);

                method.invoke(_contactRemoteModel, status);
            } catch (Exception e) {
                throw new UnsupportedOperationException(e);
            }
        }
    }

    @Override
    public java.util.List<ch.inofix.portlet.contact.dto.InterestDTO> getInterests() {
        try {
            String methodName = "getInterests";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            java.util.List<ch.inofix.portlet.contact.dto.InterestDTO> returnObj = (java.util.List<ch.inofix.portlet.contact.dto.InterestDTO>) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public int getDeathdateYear() {
        try {
            String methodName = "getDeathdateYear";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            Integer returnObj = (Integer) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public java.util.List<ch.inofix.portlet.contact.dto.FileDTO> getSounds() {
        try {
            String methodName = "getSounds";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            java.util.List<ch.inofix.portlet.contact.dto.FileDTO> returnObj = (java.util.List<ch.inofix.portlet.contact.dto.FileDTO>) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public java.lang.String getTimezone() {
        try {
            String methodName = "getTimezone";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            java.lang.String returnObj = (java.lang.String) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public java.lang.String getOffice() {
        try {
            String methodName = "getOffice";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            java.lang.String returnObj = (java.lang.String) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public java.lang.String getCompany() {
        try {
            String methodName = "getCompany";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            java.lang.String returnObj = (java.lang.String) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public java.lang.String getNickname() {
        try {
            String methodName = "getNickname";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            java.lang.String returnObj = (java.lang.String) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public java.lang.String getKind() {
        try {
            String methodName = "getKind";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            java.lang.String returnObj = (java.lang.String) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public java.util.List<ch.inofix.portlet.contact.dto.UrlDTO> getUrls() {
        try {
            String methodName = "getUrls";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            java.util.List<ch.inofix.portlet.contact.dto.UrlDTO> returnObj = (java.util.List<ch.inofix.portlet.contact.dto.UrlDTO>) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public java.lang.String getTitle() {
        try {
            String methodName = "getTitle";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            java.lang.String returnObj = (java.lang.String) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public java.util.List<ch.inofix.portlet.contact.dto.FileDTO> getPhotos() {
        try {
            String methodName = "getPhotos";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            java.util.List<ch.inofix.portlet.contact.dto.FileDTO> returnObj = (java.util.List<ch.inofix.portlet.contact.dto.FileDTO>) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public java.lang.String getFullName(boolean firstLast) {
        try {
            String methodName = "getFullName";

            Class<?>[] parameterTypes = new Class<?>[] { boolean.class };

            Object[] parameterValues = new Object[] { firstLast };

            java.lang.String returnObj = (java.lang.String) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public java.util.List<ch.inofix.portlet.contact.dto.NoteDTO> getNotes() {
        try {
            String methodName = "getNotes";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            java.util.List<ch.inofix.portlet.contact.dto.NoteDTO> returnObj = (java.util.List<ch.inofix.portlet.contact.dto.NoteDTO>) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public int getAnniversaryMonth() {
        try {
            String methodName = "getAnniversaryMonth";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            Integer returnObj = (Integer) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public java.util.List<ch.inofix.portlet.contact.dto.PhoneDTO> getPhones() {
        try {
            String methodName = "getPhones";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            java.util.List<ch.inofix.portlet.contact.dto.PhoneDTO> returnObj = (java.util.List<ch.inofix.portlet.contact.dto.PhoneDTO>) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public int getDeathdateMonth() {
        try {
            String methodName = "getDeathdateMonth";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            Integer returnObj = (Integer) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public int getAnniversaryDay() {
        try {
            String methodName = "getAnniversaryDay";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            Integer returnObj = (Integer) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public java.lang.String getBirthplace() {
        try {
            String methodName = "getBirthplace";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            java.lang.String returnObj = (java.lang.String) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public java.util.List<ch.inofix.portlet.contact.dto.EmailDTO> getEmails() {
        try {
            String methodName = "getEmails";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            java.util.List<ch.inofix.portlet.contact.dto.EmailDTO> returnObj = (java.util.List<ch.inofix.portlet.contact.dto.EmailDTO>) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public java.util.List<ch.inofix.portlet.contact.dto.UriDTO> getCalendarRequestUris() {
        try {
            String methodName = "getCalendarRequestUris";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            java.util.List<ch.inofix.portlet.contact.dto.UriDTO> returnObj = (java.util.List<ch.inofix.portlet.contact.dto.UriDTO>) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public java.util.List<ch.inofix.portlet.contact.dto.UrlDTO> getFreeBusyUrls() {
        try {
            String methodName = "getFreeBusyUrls";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            java.util.List<ch.inofix.portlet.contact.dto.UrlDTO> returnObj = (java.util.List<ch.inofix.portlet.contact.dto.UrlDTO>) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public ch.inofix.portlet.contact.dto.AddressDTO getAddress() {
        try {
            String methodName = "getAddress";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            ch.inofix.portlet.contact.dto.AddressDTO returnObj = (ch.inofix.portlet.contact.dto.AddressDTO) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public java.lang.String getDeathplace() {
        try {
            String methodName = "getDeathplace";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            java.lang.String returnObj = (java.lang.String) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public int getBirthdayDay() {
        try {
            String methodName = "getBirthdayDay";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            Integer returnObj = (Integer) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public java.util.List<ch.inofix.portlet.contact.dto.UriDTO> getCalendarUris() {
        try {
            String methodName = "getCalendarUris";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            java.util.List<ch.inofix.portlet.contact.dto.UriDTO> returnObj = (java.util.List<ch.inofix.portlet.contact.dto.UriDTO>) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public int getBirthdayMonth() {
        try {
            String methodName = "getBirthdayMonth";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            Integer returnObj = (Integer) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public java.lang.String getGender() {
        try {
            String methodName = "getGender";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            java.lang.String returnObj = (java.lang.String) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public java.lang.String getName() {
        try {
            String methodName = "getName";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            java.lang.String returnObj = (java.lang.String) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public java.lang.String getPortrait() {
        try {
            String methodName = "getPortrait";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            java.lang.String returnObj = (java.lang.String) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public java.lang.String getVCardHTML() {
        try {
            String methodName = "getVCardHTML";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            java.lang.String returnObj = (java.lang.String) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public ezvcard.VCard getVCard() {
        try {
            String methodName = "getVCard";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            ezvcard.VCard returnObj = (ezvcard.VCard) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public java.lang.String getRole() {
        try {
            String methodName = "getRole";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            java.lang.String returnObj = (java.lang.String) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public ch.inofix.portlet.contact.dto.EmailDTO getEmail() {
        try {
            String methodName = "getEmail";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            ch.inofix.portlet.contact.dto.EmailDTO returnObj = (ch.inofix.portlet.contact.dto.EmailDTO) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public java.util.List<ch.inofix.portlet.contact.dto.ExpertiseDTO> getExpertises() {
        try {
            String methodName = "getExpertises";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            java.util.List<ch.inofix.portlet.contact.dto.ExpertiseDTO> returnObj =
                (java.util.List<ch.inofix.portlet.contact.dto.ExpertiseDTO>) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public int getDeathdateDay() {
        try {
            String methodName = "getDeathdateDay";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            Integer returnObj = (Integer) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public ch.inofix.portlet.contact.dto.PhoneDTO getPhone() {
        try {
            String methodName = "getPhone";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            ch.inofix.portlet.contact.dto.PhoneDTO returnObj = (ch.inofix.portlet.contact.dto.PhoneDTO) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public java.lang.String getDepartment() {
        try {
            String methodName = "getDepartment";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            java.lang.String returnObj = (java.lang.String) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public int getBirthdayYear() {
        try {
            String methodName = "getBirthdayYear";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            Integer returnObj = (Integer) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public java.util.List<ch.inofix.portlet.contact.dto.CategoriesDTO> getCategoriesList() {
        try {
            String methodName = "getCategoriesList";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            java.util.List<ch.inofix.portlet.contact.dto.CategoriesDTO> returnObj =
                (java.util.List<ch.inofix.portlet.contact.dto.CategoriesDTO>) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public int getAnniversaryYear() {
        try {
            String methodName = "getAnniversaryYear";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            Integer returnObj = (Integer) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public java.util.List<ch.inofix.portlet.contact.dto.LanguageDTO> getLanguages() {
        try {
            String methodName = "getLanguages";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            java.util.List<ch.inofix.portlet.contact.dto.LanguageDTO> returnObj = (java.util.List<ch.inofix.portlet.contact.dto.LanguageDTO>) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public java.lang.String getFullName() {
        try {
            String methodName = "getFullName";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            java.lang.String returnObj = (java.lang.String) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public java.util.List<ch.inofix.portlet.contact.dto.FileDTO> getKeys() {
        try {
            String methodName = "getKeys";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            java.util.List<ch.inofix.portlet.contact.dto.FileDTO> returnObj = (java.util.List<ch.inofix.portlet.contact.dto.FileDTO>) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public java.util.List<ch.inofix.portlet.contact.dto.AddressDTO> getAddresses() {
        try {
            String methodName = "getAddresses";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            java.util.List<ch.inofix.portlet.contact.dto.AddressDTO> returnObj = (java.util.List<ch.inofix.portlet.contact.dto.AddressDTO>) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public java.lang.String getSortAs() {
        try {
            String methodName = "getSortAs";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            java.lang.String returnObj = (java.lang.String) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public java.util.List<ch.inofix.portlet.contact.dto.ImppDTO> getImpps() {
        try {
            String methodName = "getImpps";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            java.util.List<ch.inofix.portlet.contact.dto.ImppDTO> returnObj = (java.util.List<ch.inofix.portlet.contact.dto.ImppDTO>) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public java.util.List<ch.inofix.portlet.contact.dto.FileDTO> getLogos() {
        try {
            String methodName = "getLogos";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            java.util.List<ch.inofix.portlet.contact.dto.FileDTO> returnObj = (java.util.List<ch.inofix.portlet.contact.dto.FileDTO>) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public java.util.List<ch.inofix.portlet.contact.dto.HobbyDTO> getHobbies() {
        try {
            String methodName = "getHobbies";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            java.util.List<ch.inofix.portlet.contact.dto.HobbyDTO> returnObj = (java.util.List<ch.inofix.portlet.contact.dto.HobbyDTO>) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public java.lang.String getFormattedName() {
        try {
            String methodName = "getFormattedName";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            java.lang.String returnObj = (java.lang.String) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public ch.inofix.portlet.contact.dto.StructuredNameDTO getStructuredName() {
        try {
            String methodName = "getStructuredName";

            Class<?>[] parameterTypes = new Class<?>[] {  };

            Object[] parameterValues = new Object[] {  };

            ch.inofix.portlet.contact.dto.StructuredNameDTO returnObj = (ch.inofix.portlet.contact.dto.StructuredNameDTO) invokeOnRemoteModel(methodName,
                    parameterTypes, parameterValues);

            return returnObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public StagedModelType getStagedModelType() {
        return new StagedModelType(PortalUtil.getClassNameId(
                Contact.class.getName()));
    }

    public BaseModel<?> getContactRemoteModel() {
        return _contactRemoteModel;
    }

    public void setContactRemoteModel(BaseModel<?> contactRemoteModel) {
        _contactRemoteModel = contactRemoteModel;
    }

    public Object invokeOnRemoteModel(String methodName,
        Class<?>[] parameterTypes, Object[] parameterValues)
        throws Exception {
        Object[] remoteParameterValues = new Object[parameterValues.length];

        for (int i = 0; i < parameterValues.length; i++) {
            if (parameterValues[i] != null) {
                remoteParameterValues[i] = ClpSerializer.translateInput(parameterValues[i]);
            }
        }

        Class<?> remoteModelClass = _contactRemoteModel.getClass();

        ClassLoader remoteModelClassLoader = remoteModelClass.getClassLoader();

        Class<?>[] remoteParameterTypes = new Class[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            if (parameterTypes[i].isPrimitive()) {
                remoteParameterTypes[i] = parameterTypes[i];
            } else {
                String parameterTypeName = parameterTypes[i].getName();

                remoteParameterTypes[i] = remoteModelClassLoader.loadClass(parameterTypeName);
            }
        }

        Method method = remoteModelClass.getMethod(methodName,
                remoteParameterTypes);

        Object returnValue = method.invoke(_contactRemoteModel,
                remoteParameterValues);

        if (returnValue != null) {
            returnValue = ClpSerializer.translateOutput(returnValue);
        }

        return returnValue;
    }

    @Override
    public void persist() throws SystemException {
        if (this.isNew()) {
            ContactLocalServiceUtil.addContact(this);
        } else {
            ContactLocalServiceUtil.updateContact(this);
        }
    }

    @Override
    public Contact toEscapedModel() {
        return (Contact) ProxyUtil.newProxyInstance(Contact.class.getClassLoader(),
            new Class[] { Contact.class }, new AutoEscapeBeanHandler(this));
    }

    @Override
    public Object clone() {
        ContactClp clone = new ContactClp();

        clone.setUuid(getUuid());
        clone.setContactId(getContactId());
        clone.setGroupId(getGroupId());
        clone.setCompanyId(getCompanyId());
        clone.setUserId(getUserId());
        clone.setUserName(getUserName());
        clone.setCreateDate(getCreateDate());
        clone.setModifiedDate(getModifiedDate());
        clone.setParentContactId(getParentContactId());
        clone.setCard(getCard());
        clone.setUid(getUid());
        clone.setStatus(getStatus());

        return clone;
    }

    @Override
    public int compareTo(Contact contact) {
        int value = 0;

        if (getContactId() < contact.getContactId()) {
            value = -1;
        } else if (getContactId() > contact.getContactId()) {
            value = 1;
        } else {
            value = 0;
        }

        if (value != 0) {
            return value;
        }

        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof ContactClp)) {
            return false;
        }

        ContactClp contact = (ContactClp) obj;

        long primaryKey = contact.getPrimaryKey();

        if (getPrimaryKey() == primaryKey) {
            return true;
        } else {
            return false;
        }
    }

    public Class<?> getClpSerializerClass() {
        return _clpSerializerClass;
    }

    @Override
    public int hashCode() {
        return (int) getPrimaryKey();
    }

    @Override
    public String toString() {
        StringBundler sb = new StringBundler(25);

        sb.append("{uuid=");
        sb.append(getUuid());
        sb.append(", contactId=");
        sb.append(getContactId());
        sb.append(", groupId=");
        sb.append(getGroupId());
        sb.append(", companyId=");
        sb.append(getCompanyId());
        sb.append(", userId=");
        sb.append(getUserId());
        sb.append(", userName=");
        sb.append(getUserName());
        sb.append(", createDate=");
        sb.append(getCreateDate());
        sb.append(", modifiedDate=");
        sb.append(getModifiedDate());
        sb.append(", parentContactId=");
        sb.append(getParentContactId());
        sb.append(", card=");
        sb.append(getCard());
        sb.append(", uid=");
        sb.append(getUid());
        sb.append(", status=");
        sb.append(getStatus());
        sb.append("}");

        return sb.toString();
    }

    @Override
    public String toXmlString() {
        StringBundler sb = new StringBundler(40);

        sb.append("<model><model-name>");
        sb.append("ch.inofix.portlet.contact.model.Contact");
        sb.append("</model-name>");

        sb.append(
            "<column><column-name>uuid</column-name><column-value><![CDATA[");
        sb.append(getUuid());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>contactId</column-name><column-value><![CDATA[");
        sb.append(getContactId());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>groupId</column-name><column-value><![CDATA[");
        sb.append(getGroupId());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>companyId</column-name><column-value><![CDATA[");
        sb.append(getCompanyId());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>userId</column-name><column-value><![CDATA[");
        sb.append(getUserId());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>userName</column-name><column-value><![CDATA[");
        sb.append(getUserName());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>createDate</column-name><column-value><![CDATA[");
        sb.append(getCreateDate());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>modifiedDate</column-name><column-value><![CDATA[");
        sb.append(getModifiedDate());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>parentContactId</column-name><column-value><![CDATA[");
        sb.append(getParentContactId());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>card</column-name><column-value><![CDATA[");
        sb.append(getCard());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>uid</column-name><column-value><![CDATA[");
        sb.append(getUid());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>status</column-name><column-value><![CDATA[");
        sb.append(getStatus());
        sb.append("]]></column-value></column>");

        sb.append("</model>");

        return sb.toString();
    }
}
