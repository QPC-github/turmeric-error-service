/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.tools.errorlibrary;

import org.ebayopensource.turmeric.common.v1.types.AckValue;
import org.ebayopensource.turmeric.common.v1.types.ErrorData;
import org.ebayopensource.turmeric.tools.errorlibrary.exception.ErrorIdGeneratorException;
import org.ebayopensource.turmeric.turmeric.v1.services.GetNextIdRequest;
import org.ebayopensource.turmeric.turmeric.v1.services.GetNextIdResponse;
import org.ebayopensource.turmeric.turmeric.v1.services.erroridservice.gen.SharedTurmericErrorIdServiceV1Consumer;

/**
 * @author haozhou
 *
 */
public class WebErrorIdGenerator implements ErrorIdGenerator {
	private String organizationName;
	private SharedTurmericErrorIdServiceV1Consumer consumer;
	
	
	private WebErrorIdGenerator(String serviceLocation, String organizationName) {
		try {
			consumer = new ErrorIdServiceConsumer(serviceLocation);
			this.organizationName = organizationName;
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public static class Builder implements ErrorIdGenerator.Builder {
		private String storeLocation;
		private String organizationName;
		private int blocksize;

		@Override
		public ErrorIdGenerator.Builder storeLocation(
				String storeLocation) {
			this.storeLocation = storeLocation;
			return this;
		}

		@Override
		public ErrorIdGenerator.Builder credentials(
				String username, String password) {
			throw new UnsupportedOperationException();
		}

		@Override
		public ErrorIdGenerator.Builder organizationName(
				String organizationName) {
			this.organizationName = organizationName;
			return this;
		}

		@Override
		public ErrorIdGenerator.Builder blocksize(
				int blocksize) {
			this.blocksize = blocksize;
			return this;
		}

		@Override
		public ErrorIdGenerator build() {
			return new WebErrorIdGenerator(this.storeLocation, this.organizationName);
		}
		
	}

	@Override
	public long getNextId(String domain) throws IllegalArgumentException,
			IllegalStateException, ErrorIdGeneratorException {
		GetNextIdRequest req = new GetNextIdRequest();
		req.setOrganization(this.organizationName);
		req.setDomain(domain);
		
		GetNextIdResponse resp = consumer.getNextId(req);
		if (!AckValue.SUCCESS.equals(resp.getAck())) {
			StringBuffer buffer = new StringBuffer();
			for (ErrorData error : resp.getErrorMessage().getError()) {
				buffer.append(error.getMessage());
				buffer.append("\n");
			}
			throw new ErrorIdGeneratorException("Cannot retrieve error id via service: " + buffer.toString());
		}
		return resp.getErrorId();
	}
}
