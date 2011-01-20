/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.turmeric.v1.services.erroridservice.impl;

import java.util.HashMap;
import java.util.Map;

import org.ebayopensource.turmeric.common.v1.types.AckValue;
import org.ebayopensource.turmeric.common.v1.types.CommonErrorData;
import org.ebayopensource.turmeric.common.v1.types.ErrorMessage;
import org.ebayopensource.turmeric.common.v1.types.ErrorSeverity;
import org.ebayopensource.turmeric.tools.errorlibrary.ErrorIdGenerator;
import org.ebayopensource.turmeric.tools.errorlibrary.ErrorIdGeneratorFactory;
import org.ebayopensource.turmeric.turmeric.v1.services.GetNextIdRequest;
import org.ebayopensource.turmeric.turmeric.v1.services.GetNextIdResponse;
import org.ebayopensource.turmeric.turmeric.v1.services.GetVersionRequest;
import org.ebayopensource.turmeric.turmeric.v1.services.GetVersionResponse;
import org.ebayopensource.turmeric.turmeric.v1.services.erroridservice.TurmericErrorIdServiceV1;

public class TurmericErrorIdServiceV1Impl implements TurmericErrorIdServiceV1 {

	private Map<String, ErrorIdGenerator> generatorMap = new HashMap<String, ErrorIdGenerator>();
	private String storeLocation = "//usa-entfs-003.corp.ebay.com/Engineering/ProdDev/API/SOAFramework/ErrorManagment/";

    public GetNextIdResponse getNextId(GetNextIdRequest req) {
    	String org = req.getOrganization();
		String domain = req.getDomain();
		GetNextIdResponse response = new GetNextIdResponse();
		
		if (isEmpty(org) || isEmpty(domain)) {
			response.setAck(AckValue.FAILURE);
			ErrorMessage message = new ErrorMessage();
			CommonErrorData error = new CommonErrorData();
			error.setMessage("organization and domain cannot be empty");
			error.setSeverity(ErrorSeverity.ERROR);
			message.getError().add(error);
			response.setErrorMessage(message);
			return response;
		} 
		//trim organization name and domain name
		org = org.trim();
		domain = domain.trim();
		
		long nextId = -1;
		try {
			if (generatorMap.containsKey(org)) {
				nextId = generatorMap.get(org).getNextId(domain);
			} else {
				ErrorIdGenerator generator = ErrorIdGeneratorFactory.getErrorIdGenerator(storeLocation, org);
				nextId = generator.getNextId(domain);
				generatorMap.put(org, generator);
			}
			response.setAck(AckValue.SUCCESS);
			response.setErrorId(nextId);
		} catch (Exception e) {
			response.setAck(AckValue.FAILURE);
			ErrorMessage message = new ErrorMessage();
			CommonErrorData error = new CommonErrorData();
			error.setMessage(e.getMessage());
			error.setSeverity(ErrorSeverity.ERROR);
			message.getError().add(error);
			response.setErrorMessage(message);
		}
		return response;
    }

    public GetVersionResponse getVersion(GetVersionRequest req) {
    	GetVersionResponse resp = new GetVersionResponse();
		resp.setAck(AckValue.SUCCESS);
		resp.setVersion("1.0.0");
		return resp;
    }
    
    private boolean isEmpty(String str) {
    	if (str == null || str.trim().length() == 0) {
    		return true;
    	}
    	return false;
    }

}
