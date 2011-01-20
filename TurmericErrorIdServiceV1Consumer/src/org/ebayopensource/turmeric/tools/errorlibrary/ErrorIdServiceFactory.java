/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.tools.errorlibrary;

/**
 * @author haozhou
 *
 */
public class ErrorIdServiceFactory implements IErrorIdServiceFactory {
	
	private ErrorIdServiceFactory(){
		
	}
	
	private static IErrorIdServiceFactory _this;
	
	public static IErrorIdServiceFactory getInstance() {
		synchronized (ErrorIdServiceFactory.class) {
			if (_this == null) {
				_this = new ErrorIdServiceFactory();
			}
		}
		return _this;
	}
	
	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.tools.errorlibrary.IErrorIdServiceFactory#getErrorIdGenerator(java.lang.String, java.lang.String)
	 */
	@Override
	public ErrorIdGenerator getErrorIdGenerator(String storeLocation, String organization) {
		ErrorIdGenerator.Builder builder = new WebErrorIdGenerator.Builder();
		builder.storeLocation(storeLocation);
		builder.organizationName(organization);
		return builder.build();
	}
}
