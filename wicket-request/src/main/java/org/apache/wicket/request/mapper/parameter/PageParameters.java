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
package org.apache.wicket.request.mapper.parameter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.wicket.request.IRequestMapper;
import org.apache.wicket.util.lang.Args;
import org.apache.wicket.util.lang.Objects;
import org.apache.wicket.util.string.IStringIterator;
import org.apache.wicket.util.string.StringList;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.util.value.ValueMap;

/**
 * Mutable class that holds parameters of a Page. Page parameters consist of indexed parameters and
 * named parameters. Indexed parameters are URL segments before the query string. Named parameters
 * are usually represented as query string params (i.e. ?arg1=var1&amp;arg2=val)
 * <p>
 * <strong>Indexed vs Named Parameters</strong>: Suppose we mounted a page on {@code /user} and the
 * following url was accessed {@code /user/profile/bob?action=view&redirect=false}. In this example
 * {@code profile} and {@code bob} are indexed parameters with respective indexes 0 and 1.
 * {@code action} and {@code redirect} are named parameters.
 * </p>
 * <p>
 * How those parameters are populated depends on the {@link IRequestMapper}s
 * 
 * @author Matej Knopp
 */
public class PageParameters implements Serializable
{
	private static class Entry implements Serializable
	{
		private static final long serialVersionUID = 1L;

		private String key;
		private String value;
	}

	private static final long serialVersionUID = 1L;

	private List<String> indexedParameters;

	private List<Entry> namedParameters;

	/**
	 * Construct.
	 */
	public PageParameters()
	{
	}

	/**
	 * Copy constructor.
	 * 
	 * @param copy
	 */
	public PageParameters(final PageParameters copy)
	{
		if (copy != null)
		{
			if (copy.indexedParameters != null)
			{
				indexedParameters = new ArrayList<String>(copy.indexedParameters);
			}

			if (copy.namedParameters != null)
			{
				namedParameters = new ArrayList<Entry>(copy.namedParameters);
			}
		}
	}


	/**
	 * Construct.
	 * 
	 * @param keyValuePairs
	 *            List of key value pairs separated by commas. For example, "param1=foo,param2=bar"
	 * @see ValueMap#ValueMap(String)
	 * @deprecated use varios setter methods to set parameters
	 */
	@Deprecated
	public PageParameters(final String keyValuePairs)
	{
		this(keyValuePairs, ",");
	}

	/**
	 * Construct.
	 * 
	 * @param keyValuePairs
	 *            List of key value pairs separated by commas. For example, "param1=foo,param2=bar"
	 * @param delimiter
	 *            Delimiter string used to separate key/value pairs
	 * @see ValueMap#ValueMap(String)
	 * 
	 * @deprecated use varios setter methods to set parameters
	 */
	@Deprecated
	public PageParameters(final String keyValuePairs, final String delimiter)
	{
		super();

		// We can not use ValueMaps constructor as it uses
		// VariableAssignmentParser which is more suitable for markup
		// attributes, rather than URL parameters. URL param keys for
		// examples are allowed to start with a digit (e.g. 0=xxx)
		// and quotes are not "quotes".

		// Get list of strings separated by the delimiter
		final StringList pairs = StringList.tokenize(keyValuePairs, delimiter);

		// Go through each string in the list
		for (IStringIterator iterator = pairs.iterator(); iterator.hasNext();)
		{
			// Get the next key value pair
			final String pair = iterator.next();

			final int pos = pair.indexOf('=');
			if (pos == 0)
			{
				throw new IllegalArgumentException("URL parameter is missing the lvalue: " + pair);
			}
			else if (pos != -1)
			{
				final String key = pair.substring(0, pos).trim();
				final String value = pair.substring(pos + 1).trim();

				add(key, value);
			}
			else
			{
				final String key = pair.trim();
				final String value = null;

				add(key, value);
			}
		}
	}


	/**
	 * @return count of indexed parameters
	 */
	public int getIndexedCount()
	{
		return indexedParameters != null ? indexedParameters.size() : 0;
	}

	/**
	 * Sets the indexed parameter on given index
	 * 
	 * @param index
	 * @param object
	 * @return this
	 */
	public PageParameters set(int index, Object object)
	{
		if (indexedParameters == null)
		{
			indexedParameters = new ArrayList<String>(index);
		}

		for (int i = indexedParameters.size(); i <= index; ++i)
		{
			indexedParameters.add(null);
		}

		indexedParameters.set(index, object != null ? object.toString() : null);
		return this;
	}

	/**
	 * @param index
	 * @return indexed parameter on given index
	 */
	public StringValue get(int index)
	{
		if (indexedParameters != null)
		{
			if (index >= 0 && index < indexedParameters.size())
			{
				return StringValue.valueOf(indexedParameters.get(index));
			}
		}
		return StringValue.valueOf((String)null);
	}

	/**
	 * Removes indexed parameter on given index
	 * 
	 * @param index
	 * @return this
	 */
	public PageParameters remove(int index)
	{
		if (indexedParameters != null)
		{
			if (index >= 0 && index < indexedParameters.size())
			{
				indexedParameters.remove(index);
			}
		}
		return this;
	}

	/**
	 * Return set of all named parameter names.
	 * 
	 * @return named parameter names
	 */
	public Set<String> getNamedKeys()
	{
		if (namedParameters == null || namedParameters.isEmpty())
		{
			return Collections.emptySet();
		}
		Set<String> set = new TreeSet<String>();
		for (Entry entry : namedParameters)
		{
			set.add(entry.key);
		}
		return Collections.unmodifiableSet(set);
	}

	/**
	 * Returns parameter value of named parameter with given name
	 * 
	 * @param name
	 * @return parameter value
	 */
	public StringValue get(final String name)
	{
		Args.notNull(name, "name");

		if (namedParameters != null)
		{
			for (Entry entry : namedParameters)
			{
				if (entry.key.equals(name))
				{
					return StringValue.valueOf(entry.value);
				}
			}
		}
		return StringValue.valueOf((String)null);
	}

	/**
	 * Return list of all values for named parameter with given name
	 * 
	 * @param name
	 * @return list of parameter values
	 */
	public List<StringValue> getValues(final String name)
	{
		Args.notNull(name, "name");

		if (namedParameters != null)
		{
			List<StringValue> result = new ArrayList<StringValue>();
			for (Entry entry : namedParameters)
			{
				if (entry.key.equals(name))
				{
					result.add(StringValue.valueOf(entry.value));
				}
			}
			return Collections.unmodifiableList(result);
		}
		else
		{
			return Collections.emptyList();
		}
	}

	/**
	 * Represents a named parameter entry. There can be multiple {@link NamedPair}s in
	 * {@link PageParameters} that have same key.
	 * 
	 * @author Matej Knopp
	 */
	public static class NamedPair
	{
		private final String key;
		private final String value;

		private NamedPair(String key, String value)
		{
			this.key = key;
			this.value = value;
		}

		/**
		 * @return key
		 */
		public String getKey()
		{
			return key;
		}

		/**
		 * @return value
		 */
		public String getValue()
		{
			return value;
		}
	}

	/**
	 * @return All named parameters in exact order.
	 */
	public List<NamedPair> getAllNamed()
	{
		List<NamedPair> res = new ArrayList<NamedPair>();
		if (namedParameters != null)
		{
			for (Entry e : namedParameters)
			{
				res.add(new NamedPair(e.key, e.value));
			}
		}
		return Collections.unmodifiableList(res);
	}

	/**
	 * Removes named parameter with given name.
	 * 
	 * @param name
	 * @return this
	 */
	public PageParameters remove(final String name)
	{
		Args.notNull(name, "name");

		if (namedParameters != null)
		{
			for (Iterator<Entry> i = namedParameters.iterator(); i.hasNext();)
			{
				Entry e = i.next();
				if (e.key.equals(name))
				{
					i.remove();
				}
			}
		}
		return this;
	}

	/**
	 * Adds value to named parameter with given name.
	 * 
	 * @param name
	 * @param value
	 * @return this
	 */
	public PageParameters add(final String name, final Object value)
	{
		add(name, value, -1);
		return this;
	}

	/**
	 * Adds named parameter to a specified position. The {@link IRequestMapper}s may or may not take
	 * the order into account.
	 * 
	 * @param name
	 * @param value
	 * @param index
	 * @return this
	 */
	public PageParameters add(final String name, final Object value, final int index)
	{
		Args.notNull(name, "name");
		Args.notNull(value, "value");

		if (namedParameters == null)
		{
			namedParameters = new ArrayList<Entry>(1);
		}
		Entry entry = new Entry();
		entry.key = name;
		entry.value = value.toString();

		if (index == -1)
		{
			namedParameters.add(entry);
		}
		else
		{
			namedParameters.add(index, entry);
		}
		return this;
	}

	/**
	 * Sets the named parameter on specified position. The {@link IRequestMapper}s may or may not
	 * take the order into account.
	 * 
	 * @param name
	 * @param value
	 * @param index
	 * @return this
	 */
	public PageParameters set(String name, Object value, int index)
	{
		remove(name);

		if (value != null)
		{
			add(name, value);
		}
		return this;
	}

	/**
	 * Sets the value for named parameter with given name.
	 * 
	 * @param name
	 * @param value
	 * @return this
	 */
	public PageParameters set(String name, Object value)
	{
		set(name, value, -1);
		return this;
	}

	/**
	 * Removes all indexed parameters.
	 * 
	 * @return this
	 */
	public PageParameters clearIndexed()
	{
		indexedParameters = null;
		return this;
	}

	/**
	 * Removes all named parameters.
	 * 
	 * @return this
	 */
	public PageParameters clearaNamed()
	{
		namedParameters = null;
		return this;
	}

	/**
	 * Copy the paga parameters
	 * 
	 * @param other
	 * @return this
	 */
	public PageParameters overwriteWith(PageParameters other)
	{
		if (this != other)
		{
			indexedParameters = other.indexedParameters;
			namedParameters = other.namedParameters;
		}
		return this;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}

		if (obj instanceof PageParameters == false)
		{
			return false;
		}

		PageParameters rhs = (PageParameters)obj;
		if (!Objects.equal(indexedParameters, rhs.indexedParameters))
		{
			return false;
		}

		if (namedParameters == null || rhs.namedParameters == null)
		{
			return rhs.namedParameters == namedParameters;
		}

		if (namedParameters.size() != rhs.namedParameters.size())
		{
			return false;
		}

		for (String key : getNamedKeys())
		{
			List<StringValue> values1 = getValues(key);
			Set<String> v1 = new TreeSet<String>();
			List<StringValue> values2 = rhs.getValues(key);
			Set<String> v2 = new TreeSet<String>();
			for (StringValue sv : values1)
			{
				v1.add(sv.toString());
			}
			for (StringValue sv : values2)
			{
				v2.add(sv.toString());
			}
			if (v1.equals(v2) == false)
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Compares two {@link PageParameters} objects.
	 * 
	 * @param p1
	 * @param p2
	 * @return <code>true</code> if the objects are equal, <code>false</code> otherwise.
	 */
	public static boolean equals(PageParameters p1, PageParameters p2)
	{
		if (Objects.equal(p1, p2))
		{
			return true;
		}
		if (p1 == null && p2.getIndexedCount() == 0 && p2.getNamedKeys().isEmpty())
		{
			return true;
		}
		if (p2 == null && p1.getIndexedCount() == 0 && p1.getNamedKeys().isEmpty())
		{
			return true;
		}
		return false;
	}

	/**
	 * @return <code>true</code> if the parameters are empty, <code>false</code> otherwise.
	 */
	public boolean isEmpty()
	{
		return getIndexedCount() == 0 && getNamedKeys().isEmpty();
	}

	@Override
	public String toString()
	{
		StringBuilder str = new StringBuilder();

		if (indexedParameters != null)
		{
			for (int i = 0; i < indexedParameters.size(); i++)
			{
				if (i > 0)
					str.append(", ");

				str.append(i);
				str.append('=');
				str.append('[').append(indexedParameters.get(i)).append(']');
			}
		}

		if (str.length() > 0)
			str.append(", ");

		if (namedParameters != null)
		{
			for (int i = 0; i < namedParameters.size(); i++)
			{
				Entry entry = namedParameters.get(i);

				if (i > 0)
					str.append(", ");

				str.append(entry.key);
				str.append('=');
				str.append('[').append(entry.value).append(']');
			}
		}
		return str.toString();
	}
}
