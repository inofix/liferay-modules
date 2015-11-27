package ch.inofix.portlet.contact.model.impl;

import ch.inofix.portlet.contact.model.Contact;

import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.CacheModel;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

/**
 * The cache model class for representing Contact in entity cache.
 *
 * @author Brian Wing Shun Chan
 * @see Contact
 * @generated
 */
public class ContactCacheModel implements CacheModel<Contact>, Externalizable {
    public String uuid;
    public long contactId;
    public long groupId;
    public long companyId;
    public long userId;
    public String userName;
    public long createDate;
    public long modifiedDate;
    public long parentContactId;
    public String card;
    public String uid;
    public int status;

    @Override
    public String toString() {
        StringBundler sb = new StringBundler(25);

        sb.append("{uuid=");
        sb.append(uuid);
        sb.append(", contactId=");
        sb.append(contactId);
        sb.append(", groupId=");
        sb.append(groupId);
        sb.append(", companyId=");
        sb.append(companyId);
        sb.append(", userId=");
        sb.append(userId);
        sb.append(", userName=");
        sb.append(userName);
        sb.append(", createDate=");
        sb.append(createDate);
        sb.append(", modifiedDate=");
        sb.append(modifiedDate);
        sb.append(", parentContactId=");
        sb.append(parentContactId);
        sb.append(", card=");
        sb.append(card);
        sb.append(", uid=");
        sb.append(uid);
        sb.append(", status=");
        sb.append(status);
        sb.append("}");

        return sb.toString();
    }

    @Override
    public Contact toEntityModel() {
        ContactImpl contactImpl = new ContactImpl();

        if (uuid == null) {
            contactImpl.setUuid(StringPool.BLANK);
        } else {
            contactImpl.setUuid(uuid);
        }

        contactImpl.setContactId(contactId);
        contactImpl.setGroupId(groupId);
        contactImpl.setCompanyId(companyId);
        contactImpl.setUserId(userId);

        if (userName == null) {
            contactImpl.setUserName(StringPool.BLANK);
        } else {
            contactImpl.setUserName(userName);
        }

        if (createDate == Long.MIN_VALUE) {
            contactImpl.setCreateDate(null);
        } else {
            contactImpl.setCreateDate(new Date(createDate));
        }

        if (modifiedDate == Long.MIN_VALUE) {
            contactImpl.setModifiedDate(null);
        } else {
            contactImpl.setModifiedDate(new Date(modifiedDate));
        }

        contactImpl.setParentContactId(parentContactId);

        if (card == null) {
            contactImpl.setCard(StringPool.BLANK);
        } else {
            contactImpl.setCard(card);
        }

        if (uid == null) {
            contactImpl.setUid(StringPool.BLANK);
        } else {
            contactImpl.setUid(uid);
        }

        contactImpl.setStatus(status);

        contactImpl.resetOriginalValues();

        return contactImpl;
    }

    @Override
    public void readExternal(ObjectInput objectInput) throws IOException {
        uuid = objectInput.readUTF();
        contactId = objectInput.readLong();
        groupId = objectInput.readLong();
        companyId = objectInput.readLong();
        userId = objectInput.readLong();
        userName = objectInput.readUTF();
        createDate = objectInput.readLong();
        modifiedDate = objectInput.readLong();
        parentContactId = objectInput.readLong();
        card = objectInput.readUTF();
        uid = objectInput.readUTF();
        status = objectInput.readInt();
    }

    @Override
    public void writeExternal(ObjectOutput objectOutput)
        throws IOException {
        if (uuid == null) {
            objectOutput.writeUTF(StringPool.BLANK);
        } else {
            objectOutput.writeUTF(uuid);
        }

        objectOutput.writeLong(contactId);
        objectOutput.writeLong(groupId);
        objectOutput.writeLong(companyId);
        objectOutput.writeLong(userId);

        if (userName == null) {
            objectOutput.writeUTF(StringPool.BLANK);
        } else {
            objectOutput.writeUTF(userName);
        }

        objectOutput.writeLong(createDate);
        objectOutput.writeLong(modifiedDate);
        objectOutput.writeLong(parentContactId);

        if (card == null) {
            objectOutput.writeUTF(StringPool.BLANK);
        } else {
            objectOutput.writeUTF(card);
        }

        if (uid == null) {
            objectOutput.writeUTF(StringPool.BLANK);
        } else {
            objectOutput.writeUTF(uid);
        }

        objectOutput.writeInt(status);
    }
}
