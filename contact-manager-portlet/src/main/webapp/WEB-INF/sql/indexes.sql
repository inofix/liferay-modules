create index IX_9DD7B058 on Inofix_Contact (groupId);
create unique index IX_D2E29528 on Inofix_Contact (groupId, uid);
create index IX_334D78E2 on Inofix_Contact (uuid_);
create index IX_98658A06 on Inofix_Contact (uuid_, companyId);
create unique index IX_EBB4DA08 on Inofix_Contact (uuid_, groupId);