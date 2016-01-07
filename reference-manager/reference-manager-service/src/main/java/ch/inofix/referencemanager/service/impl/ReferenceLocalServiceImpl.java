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

package ch.inofix.referencemanager.service.impl;

import aQute.bnd.annotation.ProviderType;
import ch.inofix.referencemanager.model.Reference;
import ch.inofix.referencemanager.service.base.ReferenceLocalServiceBaseImpl;

/**
 * The implementation of the reference local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the
 * {@link ch.inofix.referencemanager.service.ReferenceLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @author Christian Berndt
 * @see ReferenceLocalServiceBaseImpl
 * @see ch.inofix.referencemanager.service.ReferenceLocalServiceUtil
 * @version 0.0.1
 */
@ProviderType
public class ReferenceLocalServiceImpl extends ReferenceLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 * 
	 * Never reference this class directly. Always use {@link
	 * ch.inofix.referencemanager.service.ReferenceLocalServiceUtil} to access
	 * the reference local service.
	 */
	public Reference addReferenceWithoutId(Reference reference) {
		long resourcePrimKey = counterLocalService.increment();
		reference.setReferenceId(resourcePrimKey);
		return addReference(reference);
	}

	public String referenceLocal() {
		return "referenceLocal";
	}
}