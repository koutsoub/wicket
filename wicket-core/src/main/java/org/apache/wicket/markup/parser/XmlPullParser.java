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
package org.apache.wicket.markup.parser;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

import org.apache.wicket.markup.MarkupElement;
import org.apache.wicket.markup.parser.XmlTag.TagType;
import org.apache.wicket.util.io.FullyBufferedReader;
import org.apache.wicket.util.io.IOUtils;
import org.apache.wicket.util.io.XmlReader;
import org.apache.wicket.util.parse.metapattern.parsers.TagNameParser;
import org.apache.wicket.util.parse.metapattern.parsers.VariableAssignmentParser;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;

/**
 * A fairly shallow markup pull parser which parses a markup string of a given type of markup (for
 * example, html, xml, vxml or wml) into ComponentTag and RawMarkup tokens.
 * 
 * @author Jonathan Locke
 * @author Juergen Donnerstag
 */
public final class XmlPullParser implements IXmlPullParser
{
	/**
	 * Reads the xml data from an input stream and converts the chars according to its encoding
	 * (<?xml ... encoding="..." ?>)
	 */
	private XmlReader xmlReader;

	/**
	 * A XML independent reader which loads the whole source data into memory and which provides
	 * convenience methods to access the data.
	 */
	private FullyBufferedReader input;

	/** temporary variable which will hold the name of the closing tag. */
	private String skipUntilText;

	/** The last substring selected from the input */
	private CharSequence lastText;

	/** Everything in between &lt;!DOCTYPE ... &gt; */
	private CharSequence doctype;

	/** The type of what is in lastText */
	private ELEMENT_TYPE lastType = ELEMENT_TYPE.NOT_INITIALIZED;

	/** If lastType == TAG, than ... */
	private XmlTag lastTag;

	/**
	 * Construct.
	 */
	public XmlPullParser()
	{
	}

	public final String getEncoding()
	{
		return xmlReader.getEncoding();
	}

	public final CharSequence getXmlDeclaration()
	{
		return xmlReader.getXmlDeclaration();
	}

	public final CharSequence getDoctype()
	{
		return doctype;
	}

	public final CharSequence getInputFromPositionMarker(final int toPos)
	{
		return input.getSubstring(toPos);
	}

	public final CharSequence getInput(final int fromPos, final int toPos)
	{
		return input.getSubstring(fromPos, toPos);
	}

	/**
	 * Whatever will be in between the current index and the closing tag, will be ignored (and thus
	 * treated as raw markup (text). This is useful for tags like 'script'.
	 * 
	 * @throws ParseException
	 */
	private final void skipUntil() throws ParseException
	{
		// this is a tag with non-XHTML text as body - skip this until the
		// skipUntilText is found.
		final int startIndex = input.getPosition();
		final int tagNameLen = skipUntilText.length();

		int pos = input.getPosition() - 1;
		String endTagText = null;
		int lastPos = 0;
		while (!skipUntilText.equalsIgnoreCase(endTagText))
		{
			pos = input.find("</", pos + 1);
			if ((pos == -1) || ((pos + (tagNameLen + 2)) >= input.size()))
			{
				throw new ParseException(
					skipUntilText + " tag not closed" + getLineAndColumnText(), startIndex);
			}

			lastPos = pos + 2;
			endTagText = input.getSubstring(lastPos, lastPos + tagNameLen).toString();
		}

		input.setPosition(pos);
		lastText = input.getSubstring(startIndex, pos);
		lastType = ELEMENT_TYPE.BODY;

		// Check that the tag is properly closed
		lastPos = input.find('>', lastPos + tagNameLen);
		if (lastPos == -1)
		{
			throw new ParseException("Script tag not closed" + getLineAndColumnText(), startIndex);
		}

		// Reset the state variable
		skipUntilText = null;
	}

	/**
	 * 
	 * @return line and column number
	 */
	private String getLineAndColumnText()
	{
		return " (line " + input.getLineNumber() + ", column " + input.getColumnNumber() + ")";
	}

	/**
	 * @return XXX
	 * @throws ParseException
	 */
	public final ELEMENT_TYPE next() throws ParseException
	{
		// Reached end of markup file?
		if (input.getPosition() >= input.size())
		{
			return ELEMENT_TYPE.NOT_INITIALIZED;
		}

		if (skipUntilText != null)
		{
			skipUntil();
			return lastType;
		}

		// Any more tags in the markup?
		final int openBracketIndex = input.find('<');

		// Tag or Body?
		if (input.charAt(input.getPosition()) != '<')
		{
			if (openBracketIndex == -1)
			{
				// There is no next matching tag.
				lastText = input.getSubstring(-1);
				input.setPosition(input.size());
				lastType = ELEMENT_TYPE.BODY;
				return lastType;
			}

			lastText = input.getSubstring(openBracketIndex);
			input.setPosition(openBracketIndex);
			lastType = ELEMENT_TYPE.BODY;
			return lastType;
		}

		// Determine the line number
		input.countLinesTo(openBracketIndex);

		// Get index of closing tag and advance past the tag
		int closeBracketIndex = input.find('>', openBracketIndex + 1);
		if (closeBracketIndex == -1)
		{
			throw new ParseException("No matching close bracket at" + getLineAndColumnText(),
				input.getPosition());
		}

		// Get the complete tag text
		lastText = input.getSubstring(openBracketIndex, closeBracketIndex + 1);

		// Get the tagtext between open and close brackets
		String tagText = lastText.subSequence(1, lastText.length() - 1).toString();
		if (tagText.length() == 0)
		{
			throw new ParseException("Found empty tag: '<>' at" + getLineAndColumnText(),
				input.getPosition());
		}

		// Handle special tags like <!-- and <![CDATA ...
		final char firstChar = tagText.charAt(0);
		if ((firstChar == '!') || (firstChar == '?'))
		{
			specialTagHandling(tagText, openBracketIndex, closeBracketIndex);
			return lastType;
		}

		// Type of the tag, to be determined next
		final TagType type;

		// If the tag ends in '/', it's a "simple" tag like <foo/>
		if (tagText.endsWith("/"))
		{
			type = TagType.OPEN_CLOSE;
			tagText = tagText.substring(0, tagText.length() - 1);
		}
		else if (tagText.startsWith("/"))
		{
			// The tag text starts with a '/', it's a simple close tag
			type = TagType.CLOSE;
			tagText = tagText.substring(1);
		}
		else
		{
			// It must be an open tag
			type = TagType.OPEN;

			// If open tag and starts with "s" like "script" or "style", than
			// ...
			if ((tagText.length() > 5) &&
				((tagText.charAt(0) == 's') || (tagText.charAt(0) == 'S')))
			{
				final String lowerCase = tagText.substring(0, 6).toLowerCase();
				if (lowerCase.startsWith("script"))
				{
					// prepare to skip everything between the open and close tag
					skipUntilText = "script";
				}
				else if (lowerCase.startsWith("style"))
				{
					// prepare to skip everything between the open and close tag
					skipUntilText = "style";
				}
			}
		}

		// Parse remaining tag text, obtaining a tag object or null
		// if it's invalid
		lastTag = parseTagText(tagText);
		if (lastTag != null)
		{
			// Populate tag fields
			lastTag.type = type;
			lastTag.pos = openBracketIndex;
			lastTag.length = lastText.length();
			lastTag.text = lastText;
			lastTag.lineNumber = input.getLineNumber();
			lastTag.columnNumber = input.getColumnNumber();

			// Move to position after the tag
			input.setPosition(closeBracketIndex + 1);
			lastType = ELEMENT_TYPE.TAG;
			return lastType;
		}
		else
		{
			throw new ParseException("Malformed tag" + getLineAndColumnText(), openBracketIndex);
		}
	}

	/**
	 * Handle special tags like <!-- --> or <![CDATA[..]]> or <?xml>
	 * 
	 * @param tagText
	 * @param openBracketIndex
	 * @param closeBracketIndex
	 * @throws ParseException
	 */
	protected void specialTagHandling(String tagText, final int openBracketIndex,
		int closeBracketIndex) throws ParseException
	{
		// Handle comments
		if (tagText.startsWith("!--"))
		{
			// Normal comment section.
			// Skip ahead to "-->". Note that you can not simply test for
			// tagText.endsWith("--") as the comment might contain a '>'
			// inside.
			int pos = input.find("-->", openBracketIndex + 1);
			if (pos == -1)
			{
				throw new ParseException("Unclosed comment beginning at" + getLineAndColumnText(),
					openBracketIndex);
			}

			pos += 3;
			lastText = input.getSubstring(openBracketIndex, pos);
			lastType = ELEMENT_TYPE.COMMENT;

			// Conditional comment? E.g. "<!--[if IE]><a href='test.html'>my link</a><![endif]-->"
			if (tagText.startsWith("!--[if ") && tagText.endsWith("]") &&
				lastText.toString().endsWith("<![endif]-->"))
			{
				lastType = ELEMENT_TYPE.CONDITIONAL_COMMENT;

				// Actually it is no longer a comment. It is now
				// up to the browser to select the section appropriate.
				input.setPosition(closeBracketIndex + 1);
			}
			else
			{
				input.setPosition(pos);
			}
			return;
		}

		// The closing tag of a conditional comment, e.g.
		// "<!--[if IE]><a href='test.html'>my link</a><![endif]-->"
		if (tagText.equals("![endif]--"))
		{
			lastType = ELEMENT_TYPE.CONDITIONAL_COMMENT;
			input.setPosition(closeBracketIndex + 1);
			return;
		}

		// CDATA sections might contain "<" which is not part of an XML tag.
		// Make sure escaped "<" are treated right
		if (tagText.startsWith("!["))
		{
			final String startText = (tagText.length() <= 8 ? tagText : tagText.substring(0, 8));
			if (startText.toUpperCase().equals("![CDATA["))
			{
				int pos1 = openBracketIndex;
				do
				{
					// Get index of closing tag and advance past the tag
					closeBracketIndex = findChar('>', pos1);

					if (closeBracketIndex == -1)
					{
						throw new ParseException("No matching close bracket at" +
							getLineAndColumnText(), input.getPosition());
					}

					// Get the tagtext between open and close brackets
					tagText = input.getSubstring(openBracketIndex + 1, closeBracketIndex)
						.toString();

					pos1 = closeBracketIndex + 1;
				}
				while (tagText.endsWith("]]") == false);

				// Move to position after the tag
				input.setPosition(closeBracketIndex + 1);

				lastText = tagText;
				lastType = ELEMENT_TYPE.CDATA;
				return;
			}
		}

		if (tagText.charAt(0) == '?')
		{
			lastType = ELEMENT_TYPE.PROCESSING_INSTRUCTION;

			// Move to position after the tag
			input.setPosition(closeBracketIndex + 1);
			return;
		}

		if (tagText.startsWith("!DOCTYPE"))
		{
			lastType = ELEMENT_TYPE.DOCTYPE;

			// Get the tagtext between open and close brackets
			doctype = input.getSubstring(openBracketIndex + 1, closeBracketIndex);

			// Move to position after the tag
			input.setPosition(closeBracketIndex + 1);
			return;
		}

		// Move to position after the tag
		lastType = ELEMENT_TYPE.SPECIAL_TAG;
		input.setPosition(closeBracketIndex + 1);
	}

	/**
	 * @return MarkupElement
	 */
	public final MarkupElement getElement()
	{
		return lastTag;
	}

	/**
	 * @return The xml string from the last element
	 */
	public final CharSequence getString()
	{
		return lastText;
	}

	/**
	 * @return The next XML tag
	 * @throws ParseException
	 */
	public final MarkupElement nextTag() throws ParseException
	{
		while (next() != ELEMENT_TYPE.NOT_INITIALIZED)
		{
			switch (lastType)
			{
				case TAG :
					return lastTag;

				case BODY :
					break;

				case COMMENT :
					break;

				case CONDITIONAL_COMMENT :
					break;

				case CDATA :
					break;

				case PROCESSING_INSTRUCTION :
					break;

				case SPECIAL_TAG :
					break;
			}
		}

		return null;
	}

	/**
	 * Find the char but ignore any text within ".." and '..'
	 * 
	 * @param ch
	 *            The character to search
	 * @param startIndex
	 *            Start index
	 * @return -1 if not found, else the index
	 */
	private int findChar(final char ch, int startIndex)
	{
		char quote = 0;

		for (; startIndex < input.size(); startIndex++)
		{
			final char charAt = input.charAt(startIndex);
			if (quote != 0)
			{
				if (quote == charAt)
				{
					quote = 0;
				}
			}
			else if ((charAt == '"') || (charAt == '\''))
			{
				quote = charAt;
			}
			else if (charAt == ch)
			{
				return startIndex;
			}
		}

		return -1;
	}

	/**
	 * Parse the given string.
	 * <p>
	 * Note: xml character encoding is NOT applied. It is assumed the input provided does have the
	 * correct encoding already.
	 * 
	 * @param string
	 *            The input string
	 * @throws IOException
	 *             Error while reading the resource
	 * @throws ResourceStreamNotFoundException
	 *             Resource not found
	 */
	public void parse(final CharSequence string) throws IOException,
		ResourceStreamNotFoundException
	{
		parse(new ByteArrayInputStream(string.toString().getBytes()), null);
	}

	/**
	 * Reads and parses markup from an input stream, using UTF-8 encoding by default when not
	 * specified in XML declaration.
	 * 
	 * @param in
	 *            The input stream to read and parse
	 * @throws IOException
	 * @throws ResourceStreamNotFoundException
	 */
	public void parse(final InputStream in) throws IOException, ResourceStreamNotFoundException
	{
		// When XML declaration does not specify encoding, it defaults to UTF-8
		parse(in, "UTF-8");
	}

	/**
	 * Reads and parses markup from an input stream
	 * 
	 * @param inputStream
	 *            The input stream to read and parse
	 * @param encoding
	 *            The default character encoding of the input
	 * @throws IOException
	 * @throws ResourceStreamNotFoundException
	 */
	public void parse(final InputStream inputStream, final String encoding) throws IOException,
		ResourceStreamNotFoundException
	{
		try
		{
			xmlReader = new XmlReader(new BufferedInputStream(inputStream, 4000), encoding);
			input = new FullyBufferedReader(xmlReader);
		}
		finally
		{
			try
			{
				inputStream.close();
			}
			finally
			{
				IOUtils.close(xmlReader);
			}
		}
	}

	public final void setPositionMarker()
	{
		input.setPositionMarker(input.getPosition());
	}

	public final void setPositionMarker(final int pos)
	{
		input.setPositionMarker(pos);
	}

	@Override
	public String toString()
	{
		return input.toString();
	}

	/**
	 * Parses the text between tags. For example, "a href=foo.html".
	 * 
	 * @param tagText
	 *            The text between tags
	 * @return A new Tag object or null if the tag is invalid
	 * @throws ParseException
	 */
	private XmlTag parseTagText(final String tagText) throws ParseException
	{
		// Get the length of the tagtext
		final int tagTextLength = tagText.length();

		// If we match tagname pattern
		final TagNameParser tagnameParser = new TagNameParser(tagText);
		if (tagnameParser.matcher().lookingAt())
		{
			final XmlTag tag = new XmlTag();

			// Extract the tag from the pattern matcher
			tag.name = tagnameParser.getName();
			tag.namespace = tagnameParser.getNamespace();

			// Are we at the end? Then there are no attributes, so we just
			// return the tag
			int pos = tagnameParser.matcher().end(0);
			if (pos == tagTextLength)
			{
				return tag;
			}

			// Extract attributes
			final VariableAssignmentParser attributeParser = new VariableAssignmentParser(tagText);
			while (attributeParser.matcher().find(pos))
			{
				// Get key and value using attribute pattern
				String value = attributeParser.getValue();

				// In case like <html xmlns:wicket> will the value be null
				if (value == null)
				{
					value = "";
				}

				// Set new position to end of attribute
				pos = attributeParser.matcher().end(0);

				// Chop off double quotes or single quotes
				if (value.startsWith("\"") || value.startsWith("\'"))
				{
					value = value.substring(1, value.length() - 1);
				}

				// Trim trailing whitespace
				value = value.trim();

				// Get key
				final String key = attributeParser.getKey();

				// Put the attribute in the attributes hash
				if (null != ((TagAttributes)tag.getAttributes()).putInternal(key, value))
				{
					throw new ParseException("Same attribute found twice: " + key +
						getLineAndColumnText(), input.getPosition());
				}

				// The input has to match exactly (no left over junk after
				// attributes)
				if (pos == tagTextLength)
				{
					return tag;
				}
			}

			return tag;
		}

		return null;
	}
}
