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
package org.apache.wicket.settings;

import java.util.List;

import org.apache.wicket.markup.resolver.AutoComponentResolver;
import org.apache.wicket.markup.resolver.IComponentResolver;


/**
 * Interface for page related settings.
 * 
 * @author Igor Vaynberg (ivaynberg)
 * @author Eelco Hillenius
 */
public interface IPageSettings
{
	/**
	 * Adds a component resolver to the list.
	 * 
	 * @param resolver
	 *            The {@link IComponentResolver} that is added
	 */
	void addComponentResolver(IComponentResolver resolver);

	/**
	 * Get the (modifiable) list of IComponentResolvers.
	 * 
	 * @see AutoComponentResolver for an example
	 * @return List of ComponentResolvers
	 */
	List<IComponentResolver> getComponentResolvers();

	/**
	 * @return Returns the pagesVersionedByDefault.
	 */
	boolean getVersionPagesByDefault();

	/**
	 * @param pagesVersionedByDefault
	 *            The pagesVersionedByDefault to set.
	 */
	void setVersionPagesByDefault(boolean pagesVersionedByDefault);

}