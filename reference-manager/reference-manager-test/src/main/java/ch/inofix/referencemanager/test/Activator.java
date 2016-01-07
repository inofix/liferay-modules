/**
 * Copyright 2000-present Liferay, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.inofix.referencemanager.test;

import java.util.List;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import ch.inofix.referencemanager.model.Reference;
import ch.inofix.referencemanager.service.ReferenceLocalServiceUtil;
import ch.inofix.referencemanager.service.ReferenceServiceUtil;


public class Activator implements BundleActivator {

	@Override
	public void start(BundleContext bundleContext) throws Exception {
				
		String localResult = ReferenceLocalServiceUtil.referenceLocal();
		System.out.println("ReferenceLocalService Test: "+localResult);
		String remoteResult = ReferenceServiceUtil.referenceRemote();
		System.out.println("ReferenceRemoteService Test: "+remoteResult);
		
		int count = ReferenceLocalServiceUtil.getReferencesCount();
		List<Reference> referenceList = ReferenceLocalServiceUtil.getReferences(0, count);
		for(Reference reference : referenceList){
			System.out.println(reference.getReferenceId()+" "+reference.getBibtex());
		}
		//ReferenceLocalServiceUtil.deleteReference(111);
	/*	if( referenceList == null || referenceList.size()<=0 ){
			for(int i = 0 ; i < 9 ; i++){
				Reference reference = ReferenceLocalServiceUtil.createReference();
				reference.setField1("field1 String"+1);
				reference.setField2(true);
				reference.setField3(i);
				reference.setField4(new Date());
				reference.setField5("field5 String"+i);
				ReferenceLocalServiceUtil.addReference(reference);
			}
		}	*/		
		
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		
	}
}