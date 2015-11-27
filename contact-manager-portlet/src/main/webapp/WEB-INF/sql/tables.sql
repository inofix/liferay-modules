create table Inofix_Contact (
	uuid_ VARCHAR(75) null,
	contactId LONG not null primary key,
	groupId LONG,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	parentContactId LONG,
	card TEXT null,
	uid VARCHAR(75) null,
	status INTEGER
);