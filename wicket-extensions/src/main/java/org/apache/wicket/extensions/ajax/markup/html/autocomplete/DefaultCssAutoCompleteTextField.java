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
package org.apache.wicket.extensions.ajax.markup.html.autocomplete;

import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.PackageResourceReference;

/**
 * An {@link AutoCompleteTextField} which automatically includes the default CSS for the
 * suggestions.
 * 
 * @see AutoCompleteTextField
 * @author Antony Stubbs
 * @param <T>
 *            The model type
 */
public abstract class DefaultCssAutoCompleteTextField<T> extends AutoCompleteTextField<T>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Construct.
	 * 
	 * @param string
	 * @param model
	 */
	public DefaultCssAutoCompleteTextField(String string, IModel<T> model)
	{
		super(string, model);
	}

	@Override
	public void renderHead(IHeaderResponse response)
	{
		super.renderHead(response);

		response.renderCSSReference(new PackageResourceReference(
			DefaultCssAutoCompleteTextField.class, "DefaultCssAutoCompleteTextField.css"));
	}

}
