/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.jmx;

import org.apache.wicket.util.time.Duration;


/**
 * Exposes Application related functionality for JMX.
 * 
 * @author eelcohillenius
 */
public class RequestCycleSettings implements RequestCycleSettingsMBean
{
	private final org.apache.wicket.Application application;

	/**
	 * Create.
	 * 
	 * @param application
	 */
	public RequestCycleSettings(org.apache.wicket.Application application)
	{
		this.application = application;
	}

	/**
	 * @see org.apache.wicket.jmx.RequestCycleSettingsMBean#getBufferResponse()
	 */
	public boolean getBufferResponse()
	{
		return application.getRequestCycleSettings().getBufferResponse();
	}

	/**
	 * @see org.apache.wicket.jmx.RequestCycleSettingsMBean#getGatherExtendedBrowserInfo()
	 */
	public boolean getGatherExtendedBrowserInfo()
	{
		return application.getRequestCycleSettings().getGatherExtendedBrowserInfo();
	}

	/**
	 * @see org.apache.wicket.jmx.RequestCycleSettingsMBean#getResponseRequestEncoding()
	 */
	public String getResponseRequestEncoding()
	{
		return application.getRequestCycleSettings().getResponseRequestEncoding();
	}

	/**
	 * @see org.apache.wicket.jmx.RequestCycleSettingsMBean#getTimeout()
	 */
	public String getTimeout()
	{
		return application.getRequestCycleSettings().getTimeout().toString();
	}

	/**
	 * @see org.apache.wicket.jmx.RequestCycleSettingsMBean#setBufferResponse(boolean)
	 */
	public void setBufferResponse(boolean bufferResponse)
	{
		application.getRequestCycleSettings().setBufferResponse(bufferResponse);
	}

	/**
	 * @see org.apache.wicket.jmx.RequestCycleSettingsMBean#setGatherExtendedBrowserInfo(boolean)
	 */
	public void setGatherExtendedBrowserInfo(boolean gatherExtendedBrowserInfo)
	{
		application.getRequestCycleSettings().setGatherExtendedBrowserInfo(
				gatherExtendedBrowserInfo);
	}

	/**
	 * @see org.apache.wicket.jmx.RequestCycleSettingsMBean#setResponseRequestEncoding(java.lang.String)
	 */
	public void setResponseRequestEncoding(String responseRequestEncoding)
	{
		application.getRequestCycleSettings().setResponseRequestEncoding(responseRequestEncoding);
	}

	/**
	 * @see org.apache.wicket.jmx.RequestCycleSettingsMBean#setTimeout(java.lang.String)
	 */
	public void setTimeout(String timeout)
	{
		application.getRequestCycleSettings().setTimeout(Duration.valueOf(timeout));
	}
}
