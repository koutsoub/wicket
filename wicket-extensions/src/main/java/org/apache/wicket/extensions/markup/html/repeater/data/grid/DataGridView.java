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
package org.apache.wicket.extensions.markup.html.repeater.data.grid;

import java.util.List;

import org.apache.wicket.markup.repeater.data.IDataProvider;


/**
 * Simple concrete implementation of {@link AbstractDataGridView}
 * 
 * <p>
 * Example:
 * 
 * <pre>
 *           &lt;table&gt;
 *             &lt;tr wicket:id=&quot;rows&quot;&gt;
 *               &lt;td wicket:id=&quot;cells&quot;&gt;
 *                 &lt;span wicket:id=&quot;cell&quot;&gt; &lt;/span&gt;
 *               &lt;/td&gt;
 *             &lt;/tr&gt;
 *           &lt;/table&gt;
 * </pre>
 * 
 * <p>
 * Though this example is about a HTML table, DataGridView is not at all limited to HTML tables. Any
 * kind of grid can be rendered using DataGridView.
 * <p>
 * And the related Java code:
 * 
 * <pre>
 * 
 * List&lt;ICellPopulator&gt; columns = new ArrayList&lt;ICellPopulator&gt;();
 * 
 * columns.add(new PropertyPopulator(&quot;firstName&quot;));
 * columns.add(new PropertyPopulator(&quot;lastName&quot;));
 * 
 * add(new DataGridView(&quot;rows&quot;, columns, new UserProvider()));
 * 
 * </pre>
 * 
 * @see AbstractDataGridView
 * @see IDataProvider
 * 
 * @author Igor Vaynberg (ivaynberg)
 * 
 * @param <T>
 *            Model object type
 */
public class DataGridView<T> extends AbstractDataGridView<T>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * Notice cells are created in the same order as cell populators in the list
	 * 
	 * @param id
	 *            component id
	 * @param populators
	 *            list of ICellPopulators used to populate cells
	 * @param dataProvider
	 *            data provider
	 */
	public DataGridView(String id, List<? extends ICellPopulator<T>> populators,
		IDataProvider<T> dataProvider)
	{
		super(id, populators, dataProvider);
	}

	/**
	 * Returns the list of cell populators
	 * 
	 * @return the list of cell populators
	 */
	public List<? extends ICellPopulator<T>> getPopulators()
	{
		return internalGetPopulators();
	}

	/**
	 * Returns the data provider
	 * 
	 * @return data provider
	 */
	public IDataProvider<T> getDataProvider()
	{
		return internalGetDataProvider();
	}


}
