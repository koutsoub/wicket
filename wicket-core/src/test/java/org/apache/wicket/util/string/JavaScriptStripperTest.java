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
package org.apache.wicket.util.string;

import junit.framework.TestCase;

/**
 * Tests {@link JavaScriptStripper}
 * 
 * @author <a href="mailto:jbq@apache.org">Jean-Baptiste Quenot</a>
 */
public class JavaScriptStripperTest extends TestCase
{
	public void testUNIXWICKET501()
	{
		String s = new JavaScriptStripper().stripCommentsAndWhitespace("    // Handle the common XPath // expression\n    if ( !t.indexOf(\"//\") ) {");
		assertEquals("  if ( !t.indexOf(\"//\") ) {", s);
	}

	public void testDOSWICKET501()
	{
		String s = new JavaScriptStripper().stripCommentsAndWhitespace("    // Handle the common XPath // expression\r\n    if ( !t.indexOf(\"//\") ) {");
		assertEquals(" \nif ( !t.indexOf(\"//\") ) {", s);
	}

	public void testMACWICKET501()
	{
		String s = new JavaScriptStripper().stripCommentsAndWhitespace("    // Handle the common XPath // expression\r    if ( !t.indexOf(\"//\") ) {");
		assertEquals("  if ( !t.indexOf(\"//\") ) {", s);
	}

	public void testRegexp()
	{
		String s = new JavaScriptStripper().stripCommentsAndWhitespace("    t = jQuery.trim(t).replace( /^\\/\\//i, \"\" );");
		assertEquals(" t = jQuery.trim(t).replace( /^\\/\\//i, \"\" );", s);
	}

	public void testRegexp2()
	{
		String s = new JavaScriptStripper().stripCommentsAndWhitespace("foo.replace(/\"//*strip me*/, \"\"); // strip me\rdoFoo();");
		assertEquals("foo.replace(/\"/, \"\"); doFoo();", s);
	}

	public void testRegexp3()
	{
		String s = new JavaScriptStripper().stripCommentsAndWhitespace("parseFloat( elem.filter.match(/alpha\\(opacity=(.*)\\)/)[1] ) / 100 : 1;\r//foo");
		assertEquals("parseFloat( elem.filter.match(/alpha\\(opacity=(.*)\\)/)[1] ) / 100 : 1;\r",
			s);
	}

	public void testRegexp4()
	{
		String before = " attr: /**/ //xyz\n /\\[((?:[\\w-]*:)?[\\w-]+)\\s*(?:([!^$*~|]?=)\\s*((['\"])([^\\4]*?)\\4|([^'\"][^\\]]*?)))?\\]/    after     regex";
		String after = new JavaScriptStripper().stripCommentsAndWhitespace(before);
		String expected = " attr:   /\\[((?:[\\w-]*:)?[\\w-]+)\\s*(?:([!^$*~|]?=)\\s*((['\"])([^\\4]*?)\\4|([^'\"][^\\]]*?)))?\\]/ after regex";
		assertEquals(expected, after);
		System.out.println(after);
	}

	public void testWICKET1806()
	{
		String before = "a = [ /^(\\[) *@?([\\w-]+) *([!*$^~=]*) *('?\"?)(.*?)\\4 *\\]/ ];    b()";
		String after = new JavaScriptStripper().stripCommentsAndWhitespace(before);
		String expected = "a = [ /^(\\[) *@?([\\w-]+) *([!*$^~=]*) *('?\"?)(.*?)\\4 *\\]/ ]; b()";

		assertEquals(expected, after);
	}

	public void testWICKET2060_1()
	{
		String before = "   a   b   c";
		String after = new JavaScriptStripper().stripCommentsAndWhitespace(before);
		String expected = " a b c";
		assertEquals(expected, after);
	}

	public void testWICKET2060_2()
	{
		String before = "   a \n  b   c\n\n";
		String after = new JavaScriptStripper().stripCommentsAndWhitespace(before);
		String expected = " a\nb c\n";
		assertEquals(expected, after);
	}

	public void testWICKET2060_3()
	{
		String before = "return  this.__unbind__(type, fn);";
		String after = new JavaScriptStripper().stripCommentsAndWhitespace(before);
		String expected = "return this.__unbind__(type, fn);";
		assertEquals(expected, after);
	}
}
