/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.tools.errorlibrary;

import java.net.MalformedURLException;
import java.net.URL;

import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.sif.service.Service;
import org.ebayopensource.turmeric.runtime.sif.service.ServiceFactory;
import org.ebayopensource.turmeric.turmeric.v1.services.erroridservice.AsyncTurmericErrorIdServiceV1;
import org.ebayopensource.turmeric.turmeric.v1.services.erroridservice.gen.SharedTurmericErrorIdServiceV1Consumer;

/**
 * @author haozhou
 *
 */
public class ErrorIdServiceConsumer extends SharedTurmericErrorIdServiceV1Consumer {
	private static final String ERRORID_SERVICE = "TurmericErrorIdServiceV1";
	private static final String ERRORID_SERVICE_CLIENT = ERRORID_SERVICE + "Consumer";
	private static final String DEFAULT_ENVIRONMENT = "production";
	
	private static Service fService = null;
	private AsyncTurmericErrorIdServiceV1 fProxy = null;
	private URL fServiceLocation = null; 
	
	/**
	 * @param clientName
	 * @throws ServiceException
	 * @throws MalformedURLException 
	 */
	public ErrorIdServiceConsumer(String url) throws ServiceException, MalformedURLException {
		super(ERRORID_SERVICE_CLIENT, DEFAULT_ENVIRONMENT);
		this.fServiceLocation = new URL(url);
	}

	@Override
	protected AsyncTurmericErrorIdServiceV1 getProxy() throws ServiceException {
		if (fService == null || !fService.getServiceLocation().equals(fServiceLocation)) {
			fService = ServiceFactory.create(ERRORID_SERVICE, DEFAULT_ENVIRONMENT, ERRORID_SERVICE_CLIENT, fServiceLocation);
		}

		fProxy = fService.getProxy();
		return fProxy;
	}

}
