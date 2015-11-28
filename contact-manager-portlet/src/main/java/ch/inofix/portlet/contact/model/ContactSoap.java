package ch.inofix.portlet.contact.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class is used by SOAP remote services, specifically {@link ch.inofix.portlet.contact.service.http.ContactServiceSoap}.
 *
 * @author Brian Wing Shun Chan
 * @see ch.inofix.portlet.contact.service.http.ContactServiceSoap
 * @generated
 */
public class ContactSoap implements Serializable {
    private String _uuid;
    private long _contactId;
    private long _groupId;
    private long _companyId;
    private long _userId;
    private String _userName;
    private Date _createDate;
    private Date _modifiedDate;
    private long _parentContactId;
    private String _card;
    private String _uid;
    private int _status;

    public ContactSoap() {
    }

    public static ContactSoap toSoapModel(Contact model) {
        ContactSoap soapModel = new ContactSoap();

        soapModel.setUuid(model.getUuid());
        soapModel.setContactId(model.getContactId());
        soapModel.setGroupId(model.getGroupId());
        soapModel.setCompanyId(model.getCompanyId());
        soapModel.setUserId(model.getUserId());
        soapModel.setUserName(model.getUserName());
        soapModel.setCreateDate(model.getCreateDate());
        soapModel.setModifiedDate(model.getModifiedDate());
        soapModel.setParentContactId(model.getParentContactId());
        soapModel.setCard(model.getCard());
        soapModel.setUid(model.getUid());
        soapModel.setStatus(model.getStatus());

        return soapModel;
    }

    public static ContactSoap[] toSoapModels(Contact[] models) {
        ContactSoap[] soapModels = new ContactSoap[models.length];

        for (int i = 0; i < models.length; i++) {
            soapModels[i] = toSoapModel(models[i]);
        }

        return soapModels;
    }

    public static ContactSoap[][] toSoapModels(Contact[][] models) {
        ContactSoap[][] soapModels = null;

        if (models.length > 0) {
            soapModels = new ContactSoap[models.length][models[0].length];
        } else {
            soapModels = new ContactSoap[0][0];
        }

        for (int i = 0; i < models.length; i++) {
            soapModels[i] = toSoapModels(models[i]);
        }

        return soapModels;
    }

    public static ContactSoap[] toSoapModels(List<Contact> models) {
        List<ContactSoap> soapModels = new ArrayList<ContactSoap>(models.size());

        for (Contact model : models) {
            soapModels.add(toSoapModel(model));
        }

        return soapModels.toArray(new ContactSoap[soapModels.size()]);
    }

    public long getPrimaryKey() {
        return _contactId;
    }

    public void setPrimaryKey(long pk) {
        setContactId(pk);
    }

    public String getUuid() {
        return _uuid;
    }

    public void setUuid(String uuid) {
        _uuid = uuid;
    }

    public long getContactId() {
        return _contactId;
    }

    public void setContactId(long contactId) {
        _contactId = contactId;
    }

    public long getGroupId() {
        return _groupId;
    }

    public void setGroupId(long groupId) {
        _groupId = groupId;
    }

    public long getCompanyId() {
        return _companyId;
    }

    public void setCompanyId(long companyId) {
        _companyId = companyId;
    }

    public long getUserId() {
        return _userId;
    }

    public void setUserId(long userId) {
        _userId = userId;
    }

    public String getUserName() {
        return _userName;
    }

    public void setUserName(String userName) {
        _userName = userName;
    }

    public Date getCreateDate() {
        return _createDate;
    }

    public void setCreateDate(Date createDate) {
        _createDate = createDate;
    }

    public Date getModifiedDate() {
        return _modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        _modifiedDate = modifiedDate;
    }

    public long getParentContactId() {
        return _parentContactId;
    }

    public void setParentContactId(long parentContactId) {
        _parentContactId = parentContactId;
    }

    public String getCard() {
        return _card;
    }

    public void setCard(String card) {
        _card = card;
    }

    public String getUid() {
        return _uid;
    }

    public void setUid(String uid) {
        _uid = uid;
    }

    public int getStatus() {
        return _status;
    }

    public void setStatus(int status) {
        _status = status;
    }
}
