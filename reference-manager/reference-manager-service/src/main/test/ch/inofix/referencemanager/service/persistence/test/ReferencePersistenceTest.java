/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package ch.inofix.referencemanager.service.persistence.test;

import ch.inofix.referencemanager.exception.NoSuchReferenceException;
import ch.inofix.referencemanager.model.Reference;
import ch.inofix.referencemanager.service.ReferenceLocalServiceUtil;
import ch.inofix.referencemanager.service.persistence.ReferencePersistence;
import ch.inofix.referencemanager.service.persistence.ReferenceUtil;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;

import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.TransactionalTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.util.IntegerWrapper;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.OrderByComparatorFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PersistenceTestRule;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.junit.runner.RunWith;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @generated
 */
@RunWith(Arquillian.class)
public class ReferencePersistenceTest {
	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule = new AggregateTestRule(new LiferayIntegrationTestRule(),
			PersistenceTestRule.INSTANCE,
			new TransactionalTestRule(Propagation.REQUIRED));

	@Before
	public void setUp() {
		_persistence = ReferenceUtil.getPersistence();

		Class<?> clazz = _persistence.getClass();

		_dynamicQueryClassLoader = clazz.getClassLoader();
	}

	@After
	public void tearDown() throws Exception {
		Iterator<Reference> iterator = _references.iterator();

		while (iterator.hasNext()) {
			_persistence.remove(iterator.next());

			iterator.remove();
		}
	}

	@Test
	public void testCreate() throws Exception {
		long pk = RandomTestUtil.nextLong();

		Reference reference = _persistence.create(pk);

		Assert.assertNotNull(reference);

		Assert.assertEquals(reference.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		Reference newReference = addReference();

		_persistence.remove(newReference);

		Reference existingReference = _persistence.fetchByPrimaryKey(newReference.getPrimaryKey());

		Assert.assertNull(existingReference);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addReference();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = RandomTestUtil.nextLong();

		Reference newReference = _persistence.create(pk);

		newReference.setUuid(RandomTestUtil.randomString());

		newReference.setGroupId(RandomTestUtil.nextLong());

		newReference.setCompanyId(RandomTestUtil.nextLong());

		newReference.setUserId(RandomTestUtil.nextLong());

		newReference.setUserName(RandomTestUtil.randomString());

		newReference.setCreateDate(RandomTestUtil.nextDate());

		newReference.setModifiedDate(RandomTestUtil.nextDate());

		newReference.setBibtex(RandomTestUtil.randomString());

		newReference.setStatus(RandomTestUtil.nextInt());

		_references.add(_persistence.update(newReference));

		Reference existingReference = _persistence.findByPrimaryKey(newReference.getPrimaryKey());

		Assert.assertEquals(existingReference.getUuid(), newReference.getUuid());
		Assert.assertEquals(existingReference.getReferenceId(),
			newReference.getReferenceId());
		Assert.assertEquals(existingReference.getGroupId(),
			newReference.getGroupId());
		Assert.assertEquals(existingReference.getCompanyId(),
			newReference.getCompanyId());
		Assert.assertEquals(existingReference.getUserId(),
			newReference.getUserId());
		Assert.assertEquals(existingReference.getUserName(),
			newReference.getUserName());
		Assert.assertEquals(Time.getShortTimestamp(
				existingReference.getCreateDate()),
			Time.getShortTimestamp(newReference.getCreateDate()));
		Assert.assertEquals(Time.getShortTimestamp(
				existingReference.getModifiedDate()),
			Time.getShortTimestamp(newReference.getModifiedDate()));
		Assert.assertEquals(existingReference.getBibtex(),
			newReference.getBibtex());
		Assert.assertEquals(existingReference.getStatus(),
			newReference.getStatus());
	}

	@Test
	public void testCountByUuid() throws Exception {
		_persistence.countByUuid(StringPool.BLANK);

		_persistence.countByUuid(StringPool.NULL);

		_persistence.countByUuid((String)null);
	}

	@Test
	public void testCountByUUID_G() throws Exception {
		_persistence.countByUUID_G(StringPool.BLANK, RandomTestUtil.nextLong());

		_persistence.countByUUID_G(StringPool.NULL, 0L);

		_persistence.countByUUID_G((String)null, 0L);
	}

	@Test
	public void testCountByUuid_C() throws Exception {
		_persistence.countByUuid_C(StringPool.BLANK, RandomTestUtil.nextLong());

		_persistence.countByUuid_C(StringPool.NULL, 0L);

		_persistence.countByUuid_C((String)null, 0L);
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		Reference newReference = addReference();

		Reference existingReference = _persistence.findByPrimaryKey(newReference.getPrimaryKey());

		Assert.assertEquals(existingReference, newReference);
	}

	@Test(expected = NoSuchReferenceException.class)
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		_persistence.findByPrimaryKey(pk);
	}

	@Test
	public void testFindAll() throws Exception {
		_persistence.findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			getOrderByComparator());
	}

	protected OrderByComparator<Reference> getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create("RM_Reference", "uuid",
			true, "referenceId", true, "groupId", true, "companyId", true,
			"userId", true, "userName", true, "createDate", true,
			"modifiedDate", true, "bibtex", true, "status", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		Reference newReference = addReference();

		Reference existingReference = _persistence.fetchByPrimaryKey(newReference.getPrimaryKey());

		Assert.assertEquals(existingReference, newReference);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		Reference missingReference = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingReference);
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereAllPrimaryKeysExist()
		throws Exception {
		Reference newReference1 = addReference();
		Reference newReference2 = addReference();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newReference1.getPrimaryKey());
		primaryKeys.add(newReference2.getPrimaryKey());

		Map<Serializable, Reference> references = _persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(2, references.size());
		Assert.assertEquals(newReference1,
			references.get(newReference1.getPrimaryKey()));
		Assert.assertEquals(newReference2,
			references.get(newReference2.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereNoPrimaryKeysExist()
		throws Exception {
		long pk1 = RandomTestUtil.nextLong();

		long pk2 = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(pk1);
		primaryKeys.add(pk2);

		Map<Serializable, Reference> references = _persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(references.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereSomePrimaryKeysExist()
		throws Exception {
		Reference newReference = addReference();

		long pk = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newReference.getPrimaryKey());
		primaryKeys.add(pk);

		Map<Serializable, Reference> references = _persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, references.size());
		Assert.assertEquals(newReference,
			references.get(newReference.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithNoPrimaryKeys()
		throws Exception {
		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		Map<Serializable, Reference> references = _persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(references.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithOnePrimaryKey()
		throws Exception {
		Reference newReference = addReference();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newReference.getPrimaryKey());

		Map<Serializable, Reference> references = _persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, references.size());
		Assert.assertEquals(newReference,
			references.get(newReference.getPrimaryKey()));
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery = ReferenceLocalServiceUtil.getActionableDynamicQuery();

		actionableDynamicQuery.setPerformActionMethod(new ActionableDynamicQuery.PerformActionMethod<Reference>() {
				@Override
				public void performAction(Reference reference) {
					Assert.assertNotNull(reference);

					count.increment();
				}
			});

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		Reference newReference = addReference();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Reference.class,
				_dynamicQueryClassLoader);

		dynamicQuery.add(RestrictionsFactoryUtil.eq("referenceId",
				newReference.getReferenceId()));

		List<Reference> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Reference existingReference = result.get(0);

		Assert.assertEquals(existingReference, newReference);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Reference.class,
				_dynamicQueryClassLoader);

		dynamicQuery.add(RestrictionsFactoryUtil.eq("referenceId",
				RandomTestUtil.nextLong()));

		List<Reference> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		Reference newReference = addReference();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Reference.class,
				_dynamicQueryClassLoader);

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("referenceId"));

		Object newReferenceId = newReference.getReferenceId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("referenceId",
				new Object[] { newReferenceId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingReferenceId = result.get(0);

		Assert.assertEquals(existingReferenceId, newReferenceId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Reference.class,
				_dynamicQueryClassLoader);

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("referenceId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("referenceId",
				new Object[] { RandomTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		Reference newReference = addReference();

		_persistence.clearCache();

		Reference existingReference = _persistence.findByPrimaryKey(newReference.getPrimaryKey());

		Assert.assertTrue(Validator.equals(existingReference.getUuid(),
				ReflectionTestUtil.invoke(existingReference, "getOriginalUuid",
					new Class<?>[0])));
		Assert.assertEquals(Long.valueOf(existingReference.getGroupId()),
			ReflectionTestUtil.<Long>invoke(existingReference,
				"getOriginalGroupId", new Class<?>[0]));
	}

	protected Reference addReference() throws Exception {
		long pk = RandomTestUtil.nextLong();

		Reference reference = _persistence.create(pk);

		reference.setUuid(RandomTestUtil.randomString());

		reference.setGroupId(RandomTestUtil.nextLong());

		reference.setCompanyId(RandomTestUtil.nextLong());

		reference.setUserId(RandomTestUtil.nextLong());

		reference.setUserName(RandomTestUtil.randomString());

		reference.setCreateDate(RandomTestUtil.nextDate());

		reference.setModifiedDate(RandomTestUtil.nextDate());

		reference.setBibtex(RandomTestUtil.randomString());

		reference.setStatus(RandomTestUtil.nextInt());

		_references.add(_persistence.update(reference));

		return reference;
	}

	private List<Reference> _references = new ArrayList<Reference>();
	private ReferencePersistence _persistence;
	private ClassLoader _dynamicQueryClassLoader;
}