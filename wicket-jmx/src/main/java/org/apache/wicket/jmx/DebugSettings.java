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

/**
 * Exposes Application related functionality for JMX.
 * 
 * @author eelcohillenius
 */
public class DebugSettings implements DebugSettingsMBean
{
	private final org.apache.wicket.Application application;

	/**
	 * Create.
	 * 
	 * @param application
	 */
	public DebugSettings(org.apache.wicket.Application application)
	{
		this.application = application;
	}

	/**
	 * @see org.apache.wicket.jmx.DebugSettingsMBean#getComponentUseCheck()
	 */
	public boolean getComponentUseCheck()
	{
		return application.getDebugSettings().getComponentUseCheck();
	}

	/**
	 * @see org.apache.wicket.jmx.DebugSettingsMBean#isAjaxDebugModeEnabled()
	 */
	public boolean isAjaxDebugModeEnabled()
	{
		return application.getDebugSettings().isAjaxDebugModeEnabled();
	}

	/**
	 * @see org.apache.wicket.jmx.DebugSettingsMBean#setAjaxDebugModeEnabled(boolean)
	 */
	public void setAjaxDebugModeEnabled(boolean enable)
	{
		application.getDebugSettings().setAjaxDebugModeEnabled(enable);
	}

	/**
	 * @see org.apache.wicket.jmx.DebugSettingsMBean#setComponentUseCheck(boolean)
	 */
	public void setComponentUseCheck(boolean check)
	{
		application.getDebugSettings().setComponentUseCheck(check);
	}

	/**
	 * @see org.apache.wicket.jmx.DebugSettingsMBean#setOutputComponentPath(boolean)
	 */
	public void setOutputComponentPath(boolean enabled)
	{
		application.getDebugSettings().setOutputComponentPath(enabled);
	}

	/**
	 * @see org.apache.wicket.jmx.DebugSettingsMBean#isOutputComponentPath()
	 */
	public boolean isOutputComponentPath()
	{
		return application.getDebugSettings().isOutputComponentPath();
	}


	/**
	 * @see org.apache.wicket.jmx.DebugSettingsMBean#setOutputMarkupContainerClassName(boolean)
	 */
	public void setOutputMarkupContainerClassName(boolean enable)
	{
		application.getDebugSettings().setOutputMarkupContainerClassName(enable);
	}

	/**
	 * @see org.apache.wicket.jmx.DebugSettingsMBean#isOutputMarkupContainerClassName()
	 */
	public boolean isOutputMarkupContainerClassName()
	{
		return application.getDebugSettings().isOutputMarkupContainerClassName();
	}

	/**
	 * @see org.apache.wicket.jmx.DebugSettingsMBean#isLinePreciseReportingOnAddComponentEnabled()
	 */
	public boolean isLinePreciseReportingOnAddComponentEnabled()
	{
		return application.getDebugSettings().isLinePreciseReportingOnAddComponentEnabled();
	}

	/**
	 * @see org.apache.wicket.jmx.DebugSettingsMBean#setLinePreciseReportingOnAddComponentEnabled(boolean)
	 */
	public void setLinePreciseReportingOnAddComponentEnabled(boolean enable)
	{
		application.getDebugSettings().setLinePreciseReportingOnAddComponentEnabled(enable);
	}

	/**
	 * @see org.apache.wicket.jmx.DebugSettingsMBean#isLinePreciseReportingOnNewComponentEnabled()
	 */
	public boolean isLinePreciseReportingOnNewComponentEnabled()
	{
		return application.getDebugSettings().isLinePreciseReportingOnNewComponentEnabled();
	}

	/**
	 * @see org.apache.wicket.jmx.DebugSettingsMBean#setLinePreciseReportingOnNewComponentEnabled(boolean)
	 */
	public void setLinePreciseReportingOnNewComponentEnabled(boolean enable)
	{
		application.getDebugSettings().setLinePreciseReportingOnNewComponentEnabled(enable);
	}

	/**
	 * @see org.apache.wicket.jmx.DebugSettingsMBean#setDevelopmentUtilitiesEnabled(boolean)
	 */
	public void setDevelopmentUtilitiesEnabled(boolean enable)
	{
		application.getDebugSettings().setDevelopmentUtilitiesEnabled(enable);
	}

	/**
	 * @see org.apache.wicket.jmx.DebugSettingsMBean#isDevelopmentUtilitiesEnabled()
	 */
	public boolean isDevelopmentUtilitiesEnabled()
	{
		return application.getDebugSettings().isDevelopmentUtilitiesEnabled();
	}
}
