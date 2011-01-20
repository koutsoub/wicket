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
package org.apache.wicket;


import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.IMarkupResourceStreamProvider;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;

public class ComponentQueuingTest extends WicketTestCase
{

	public void test()
	{

		tester.startPage(TestPage.class);
		TestPage page = (TestPage)tester.getLastRenderedPage();
		WebMarkupContainer a = (WebMarkupContainer)page.get("a");
		assertNotNull(a);
		WebMarkupContainer b = (WebMarkupContainer)a.get("b");
		assertNotNull(b);
		assertEquals(a.getId(), b.getParent().getId());
		WebMarkupContainer c = (WebMarkupContainer)b.get("c");
		assertEquals(b.getId(), c.getParent().getId());
		assertNotNull(c);
	}

	public void testFail()
	{
		try
		{
			tester.startPage(TestPageToFail.class);
			TestPageToFail page = (TestPageToFail)tester.getLastRenderedPage();
			fail(" The component hierarchy should fail");
		}
		catch (WicketRuntimeException e)
		{
			// expected
		}
	}

	public void testInnerQueue()
	{

		tester.startPage(TestPageInnerQueue.class);
		TestPageInnerQueue page = (TestPageInnerQueue)tester.getLastRenderedPage();
		WebMarkupContainer a = (WebMarkupContainer)page.get("a");
		assertNotNull(a);
		WebMarkupContainer b = (WebMarkupContainer)a.get("b");
		assertNotNull(b);
		assertEquals(a.getId(), b.getParent().getId());
		WebMarkupContainer c = (WebMarkupContainer)b.get("c");
		assertEquals(b.getId(), c.getParent().getId());
		assertNotNull(c);
	}

	public void testAddAndQueue()
	{

		tester.startPage(TestPageAddAndQueue.class);
		TestPageAddAndQueue page = (TestPageAddAndQueue)tester.getLastRenderedPage();
		WebMarkupContainer a = (WebMarkupContainer)page.get("a");
		assertNotNull(a);
		WebMarkupContainer b = (WebMarkupContainer)a.get("b");
		assertNotNull(b);
		assertEquals(a.getId(), b.getParent().getId());
		WebMarkupContainer c = (WebMarkupContainer)b.get("c");
		assertEquals(b.getId(), c.getParent().getId());
		assertNotNull(c);
	}

	public void testPageWithPanel()
	{

		tester.startPage(SimpleQueuePage.class);
		SimpleQueuePage page = (SimpleQueuePage)tester.getLastRenderedPage();
		WebMarkupContainer a = (WebMarkupContainer)page.get("a");
		assertNotNull(a);
		WebMarkupContainer b = (WebMarkupContainer)a.get("panel");
		assertNotNull(b);

	}

	public void testPageWithListView()
	{

		tester.startPage(TestPageWithListView.class);
		TestPageWithListView page = (TestPageWithListView)tester.getLastRenderedPage();


	}

	public void testPageWithListView2()
	{

		tester.startPage(TestPageWithListView2.class);
		TestPageWithListView2 page = (TestPageWithListView2)tester.getLastRenderedPage();


	}

	public void testPageWithListView3()
	{

		tester.startPage(TestPageWithListView3.class);
		TestPageWithListView3 page = (TestPageWithListView3)tester.getLastRenderedPage();


	}


	public void testIdCollission()
	{
		WebMarkupContainer container = new WebMarkupContainer("container");
		container.queue(new Label("a"));
		try
		{
			container.queue(new Label("a"));
			fail("Should not be able to queue two components with the same id under the same parent");
		}
		catch (WicketRuntimeException e)
		{
			// expected
		}
	}

	public void test2()
	{

		tester.startPage(TestPage2.class);
		TestPage2 page = (TestPage2)tester.getLastRenderedPage();
		WebMarkupContainer a = (WebMarkupContainer)page.get("a");
		assertNotNull(a);
		WebMarkupContainer b = (WebMarkupContainer)a.get("b");
		assertNotNull(b);
		assertEquals(a.getId(), b.getParent().getId());
		WebMarkupContainer c = (WebMarkupContainer)b.get("c");
		assertEquals(b.getId(), c.getParent().getId());
		assertNotNull(c);
	}

	public void test3()
	{

		tester.startPage(TestPage3.class);
		TestPage3 page = (TestPage3)tester.getLastRenderedPage();
		WebMarkupContainer a = (WebMarkupContainer)page.get("a");
		assertNotNull(a);
		WebMarkupContainer b = (WebMarkupContainer)a.get("b");
		assertNotNull(b);
		assertEquals(a.getId(), b.getParent().getId());
		WebMarkupContainer c = (WebMarkupContainer)b.get("c");
		assertEquals(b.getId(), c.getParent().getId());
		assertNotNull(c);
	}

	public void test4()
	{

		tester.startPage(TestPage4.class);
		TestPage4 page = (TestPage4)tester.getLastRenderedPage();
		WebMarkupContainer a = (WebMarkupContainer)page.get("a");
		assertNotNull(a);
		WebMarkupContainer b = (WebMarkupContainer)a.get("b");
		assertNotNull(b);
		assertEquals(a.getId(), b.getParent().getId());
		WebMarkupContainer c = (WebMarkupContainer)b.get("c");
		assertEquals(b.getId(), c.getParent().getId());
		assertNotNull(c);
	}


	public void test5()
	{

		tester.startPage(TestPage5.class);
		TestPage5 page = (TestPage5)tester.getLastRenderedPage();
		WebMarkupContainer a = (WebMarkupContainer)page.get("a");
		assertNotNull(a);
		WebMarkupContainer b = (WebMarkupContainer)a.get("b");
		assertNotNull(b);
		assertEquals(a.getId(), b.getParent().getId());
		WebMarkupContainer c = (WebMarkupContainer)b.get("c");
		assertEquals(b.getId(), c.getParent().getId());
		assertNotNull(c);
	}

	public void test6()
	{

		tester.startPage(TestPage6.class);
		TestPage6 page = (TestPage6)tester.getLastRenderedPage();
		WebMarkupContainer a = (WebMarkupContainer)page.get("a");
		assertNotNull(a);
		WebMarkupContainer b = (WebMarkupContainer)a.get("b");
		assertNotNull(b);
		assertEquals(a.getId(), b.getParent().getId());
		WebMarkupContainer c = (WebMarkupContainer)b.get("c");
		assertEquals(b.getId(), c.getParent().getId());
		assertNotNull(c);
	}

	public void test7()
	{

		tester.startPage(TestPage7.class);
		TestPage7 page = (TestPage7)tester.getLastRenderedPage();
		WebMarkupContainer a = (WebMarkupContainer)page.get("a");
		assertNotNull(a);
		WebMarkupContainer b = (WebMarkupContainer)a.get("b");
		assertNotNull(b);
		assertEquals(a.getId(), b.getParent().getId());
		WebMarkupContainer c = (WebMarkupContainer)b.get("c");
		assertEquals(b.getId(), c.getParent().getId());
		assertNotNull(c);
	}

	public void test8()
	{

		tester.startPage(TestPage8.class);
		TestPage8 page = (TestPage8)tester.getLastRenderedPage();
		WebMarkupContainer a = (WebMarkupContainer)page.get("a");
		assertNotNull(a);
		WebMarkupContainer b = (WebMarkupContainer)a.get("b");
		assertNotNull(b);
		assertEquals(a.getId(), b.getParent().getId());
		WebMarkupContainer c = (WebMarkupContainer)b.get("c");
		assertEquals(b.getId(), c.getParent().getId());
		assertNotNull(c);
	}

	public void testPageWithHierarchyToFail()
	{
		try
		{
			tester.startPage(TestPageWithHierarchyToFail.class);
			fail("Hierarchy should fail");
		}
		catch (WicketRuntimeException e)
		{

		}
	}

	public static class TestPage extends WebPage implements IMarkupResourceStreamProvider
	{

		public TestPage()
		{
			doQueue();
		}

		protected void doQueue()
		{
			queue(new WebMarkupContainer("a"));
			queue(new WebMarkupContainer("b"));
			queue(new WebMarkupContainer("c"));
		}


		public IResourceStream getMarkupResourceStream(MarkupContainer container,
			Class<?> containerClass)
		{
			return new StringResourceStream(
				"<p wicket:id='a'><p wicket:id='b'><p wicket:id='c'></p></p></p>");
		}

	}

	public static class TestPage8 extends WebPage implements IMarkupResourceStreamProvider
	{

		public TestPage8()
		{
			doQueue();
		}

		protected void doQueue()
		{
			WebMarkupContainer a;
			queue(a = new WebMarkupContainer("a"));
			WebMarkupContainer b;
			queue(b = new WebMarkupContainer("b"));
			WebMarkupContainer c;
			queue(c = new WebMarkupContainer("c"));
			c.queue(new WebMarkupContainer("d"));
		}


		public IResourceStream getMarkupResourceStream(MarkupContainer container,
			Class<?> containerClass)
		{
			return new StringResourceStream(
				"<p wicket:id='a'><p wicket:id='b'><p wicket:id='c'><p wicket:id='d'></p></p></p></p>");
		}

	}

	public static class TestPageWithListView extends WebPage
		implements
			IMarkupResourceStreamProvider
	{

		public TestPageWithListView()
		{
			doQueue();
		}

		protected void doQueue()
		{
			ListView<String> listview = new ListView<String>("list", Arrays.asList(new String[] {
					"1", "2", "3" }))
			{

				@Override
				protected void onBeginPopulateItem(ListItem<String> item)
				{

				}

				@Override
				protected void populateItem(ListItem<String> item)
				{
					item.queue(new Label("a", item.getModelObject()));
					// item.unqueueChildren();
				}
			};
			queue(listview);
			listview.setReuseItems(false);
		}


		public IResourceStream getMarkupResourceStream(MarkupContainer container,
			Class<?> containerClass)
		{
			return new StringResourceStream("<p wicket:id='list'><p wicket:id='a'></p></p>");
		}

	}

	public static class TestPageWithListView2 extends WebPage
		implements
			IMarkupResourceStreamProvider
	{

		public TestPageWithListView2()
		{
			doQueue();
		}

		protected void doQueue()
		{
			ListView<String> listview = new ListView<String>("list", Arrays.asList(new String[] {
					"1", "2", "3" }))
			{

				@Override
				protected void onBeginPopulateItem(ListItem<String> item)
				{

				}

				@Override
				protected void populateItem(ListItem<String> item)
				{
					WebMarkupContainer a;
					item.queue(a = new WebMarkupContainer("a"));
					item.queue(new Label("b", item.getModelObject()));
					// item.unqueueChildren();
				}
			};
			queue(listview);
			listview.setReuseItems(false);
		}


		public IResourceStream getMarkupResourceStream(MarkupContainer container,
			Class<?> containerClass)
		{
			return new StringResourceStream(
				"<p wicket:id='list'><p wicket:id='a'><p wicket:id='b'></p></p></p>");
		}
	}

	public static class TestPageWithListView3 extends WebPage
		implements
			IMarkupResourceStreamProvider
	{

		public TestPageWithListView3()
		{
			doQueue();
		}

		protected void doQueue()
		{
			ListView<String> listview = new ListView<String>("list", Arrays.asList(new String[] {
					"1", "2", "3" }))
			{

				@Override
				protected void onBeginPopulateItem(ListItem<String> item)
				{

				}

				@Override
				protected void populateItem(ListItem<String> item)
				{
					WebMarkupContainer a;
					item.queue(new Label("b", item.getModelObject()));
					item.queue(a = new WebMarkupContainer("a"));

					// item.unqueueChildren();
				}
			};
			queue(listview);
			listview.setReuseItems(false);
		}


		public IResourceStream getMarkupResourceStream(MarkupContainer container,
			Class<?> containerClass)
		{
			return new StringResourceStream(
				"<p wicket:id='list'><p wicket:id='a'><p wicket:id='b'></p></p></p>");
		}
	}


	public static class TestPageToFail extends TestPage
	{
		@Override
		protected void doQueue()
		{
			WebMarkupContainer c;
			queue(c = new WebMarkupContainer("c"));
			c.queue(new WebMarkupContainer("b"));
			queue(new WebMarkupContainer("a"));
		}
	}

	public static class TestPage2 extends TestPage
	{
		@Override
		protected void doQueue()
		{
			queue(new WebMarkupContainer("c"));
			queue(new WebMarkupContainer("b"));
			queue(new WebMarkupContainer("a"));
		}

	}

	public static class TestPage3 extends TestPage
	{
		@Override
		protected void doQueue()
		{
			queue(new WebMarkupContainer("c"));
			queue(new WebMarkupContainer("a"));
			queue(new WebMarkupContainer("b"));
		}

	}

	public static class TestPage4 extends TestPage
	{
		@Override
		protected void doQueue()
		{
			queue(new WebMarkupContainer("b"));
			queue(new WebMarkupContainer("a"));
			queue(new WebMarkupContainer("c"));
		}

	}

	public static class TestPage5 extends TestPage
	{
		@Override
		protected void doQueue()
		{
			queue(new WebMarkupContainer("b"));
			queue(new WebMarkupContainer("c"));
			queue(new WebMarkupContainer("a"));
		}

	}

	public static class TestPage6 extends TestPage
	{
		@Override
		protected void doQueue()
		{
			WebMarkupContainer b;
			queue(new WebMarkupContainer("a"));
			queue(b = new WebMarkupContainer("b"));
			b.queue(new WebMarkupContainer("c"));

		}
	}

	public static class TestPage7 extends TestPage
	{
		@Override
		protected void doQueue()
		{
			WebMarkupContainer a;
			queue(a = new WebMarkupContainer("a"));
			queue(new WebMarkupContainer("b"));
			a.queue(new WebMarkupContainer("c"));

		}
	}

	public static class TestPageInnerQueue extends TestPage
	{
		@Override
		protected void doQueue()
		{
			queue(new WebMarkupContainer("a"));
			WebMarkupContainer b;
			queue(b = new WebMarkupContainer("b"));
			b.queue(new WebMarkupContainer("c"));
		}

	}

	public static class TestPageWithHierarchyToFail extends TestPage
	{
		@Override
		protected void doQueue()
		{

			WebMarkupContainer b;
			queue(b = new WebMarkupContainer("b"));
			b.queue(new WebMarkupContainer("c"));
			b.queue(new WebMarkupContainer("a"));
		}

	}

	public static class TestPageAddAndQueue extends TestPage
	{
		@Override
		protected void doQueue()
		{
			WebMarkupContainer a;
			queue(a = new WebMarkupContainer("a"));
			WebMarkupContainer b;
			a.add(b = new WebMarkupContainer("b"));
			b.queue(new WebMarkupContainer("c"));
		}

	}

	public static abstract class QueueListView<T> extends ListView<T>
	{

		public QueueListView(String id, List<? extends T> list)
		{
			super(id, list);
		}

		public QueueListView(String id, IModel<? extends List<? extends T>> model)
		{
			super(id, model);
		}

		public QueueListView(String id)
		{
			super(id);
		}


	}
}
