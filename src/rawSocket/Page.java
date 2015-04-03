package rawSocket;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * The Class Page.
 */
public class Page
{
	
	/** The header. */
	private String headers;
	
	/** Store all headers in a hash map. */
	private Map<String, String> header;
	
	/** The protocol. */
	private String protocol;

	/** The response code. */
	private int responseCode;
	
	/** The body. */
	private Document body;

	/**
	 * Instantiates a new page.
	 * Read all content get from the website, separate headers and body
	 * @param content the content
	 */
	public Page(String content)
	{
		if (content != null)
		{
			headers = content.substring(0, content.indexOf("\n\n")).trim();
			header = new HashMap<String, String>();
			processHeader(headers);
			body = Jsoup.parse(content.substring(content.indexOf("\n\n")).trim());
		}
	}

	/**
	 * Process header.
	 * Read each header and store them in the header collection
	 * @param headers the headers
	 */
	private void processHeader(String headers)
	{
		String delimsNewLine = "\n+";
		String[] headPerLine = headers.split(delimsNewLine);
		String delimsSpace = "[ ]+";
		String[] headFirstLine = headPerLine[0].split(delimsSpace);
		responseCode = Integer.parseInt(headFirstLine[1]);
		protocol = headFirstLine[0];
		for (int i = 1; i < headPerLine.length; i++)
		{
			String key = headPerLine[i].substring(0, headPerLine[i].indexOf(":")).trim();
			String value = headPerLine[i].substring(headPerLine[i].indexOf(":") + 2).trim();
			if (header.containsKey(key))
			{
				value = header.get(key) + "||" + value; // Just for cookies
			}
			header.put(key, value);
		}
	}

	/**
	 * Gets the response code.
	 *
	 * @return the response code
	 */
	public int getResponseCode()
	{
		return responseCode;
	}

	/**
	 * Gets the protocol.
	 *
	 * @return the protocol
	 */
	public String getProtocol()
	{
		return protocol;
	}

	/**
	 * Gets the secret flags.
	 *
	 * @return the secret flags
	 */
	public LinkedList<String> getSecretFlags()
	{
		Elements secretFlagsEle = body.select("h2.secret_flag");
		LinkedList<String> flags = new LinkedList<String>();
		for (Element flag : secretFlagsEle)
		{
			//System.out.println(flag.text());
			flags.add(flag.text());
		}
		return flags;
	}

	/**
	 * Gets the links.
	 *
	 * @return the links
	 */
	public LinkedList<String> getLinks()
	{
		Elements linksEle = body.select("a[href]");
		LinkedList<String> links = new LinkedList<String>();
		for (Element link : linksEle)
		{
			links.add(link.attr("href"));
		}
		return links;
	}

	/**
	 * Gets the csrf code.
	 *
	 * @return the csrf code
	 */
	public String getCsrfCode()
	{
		Elements inputElements = body.getElementsByTag("input");
		for (Element inputElement : inputElements)
		{
			String nameOfInputElement = inputElement.attr("name");
			if (nameOfInputElement.equals("csrfmiddlewaretoken"))
			{
				return inputElement.attr("value");
			}
		}
		return null;
	}

	/**
	 * Gets the cookies.
	 *
	 * @return the cookies
	 */
	public LinkedList<Cookie> getCookies()
	{
		LinkedList<Cookie> cookieList = new LinkedList<Cookie>();
		for (Map.Entry<String, String> entry : header.entrySet())
		{
			String key = entry.getKey();
			if (key.equals("Set-Cookie"))
			{
				String allCookies = (String) entry.getValue();
				String delim = "[||]+";
				String[] cookies = allCookies.split(delim);
				for (String everyCookieContent:cookies)
				{
					String cookieName = everyCookieContent.substring(0, everyCookieContent.indexOf("="));	
					String cookieValue = everyCookieContent.substring(everyCookieContent.indexOf("=")+1, everyCookieContent.indexOf(";"));	
					Cookie cookie = new Cookie(cookieName, cookieValue);
					cookieList.add(cookie);
				}
			}
		}
		if (cookieList.size() > 0)
		{

			return cookieList;
		}
		else
		{
			return null;
		}
	}

	/**
	 * Gets the body.
	 *
	 * @return the body
	 */
	public Document getBody()
	{
		return body;
	}
	
	/**
	 * Gets the location.
	 *
	 * @return the location
	 */
	public String getLocation()
	{
		return header.get("Location");
	}

}
