package rawSocket;

import java.util.HashMap;

/**
 * The Class Cookie.
 */
public class Cookie
{
	
	/** Store cookie as a hash map. */
	private HashMap<String, String> cookie;  // <Name, Value>
	
	/** The name of cookie. */
	private String name;
	
	/** The value. */
	private String value;
	
	
	/**
	 * Instantiates a new cookie.
	 *
	 * @param cookieName the cookie name
	 * @param cookieValue the cookie value
	 */
	public Cookie(String cookieName, String cookieValue)
	{
		cookie = new HashMap<String, String>();
		name = cookieName;
		value = cookieValue;
		cookie.put(name, value);
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}
 
	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue()
	{
		return value;
	}
	
	/**
	 * Sets the value.
	 *
	 * @param newValue the new value
	 */
	public void setValue(String newValue)
	{
		value = newValue;
	}
}
