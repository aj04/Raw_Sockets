package rawSocket;

/**
 * The Class Url.
 */
public class Url
{
	
	/** The address of URL */
	private String address = null;
	
	/** The host of URL. */
	private String host = null;
	
	/** The port of URL. */
	private int port = 80;
	
	/** The path of URL. */
	private String path = null;
	
	/** The query of URL. */
	private String query = null;
	
	/**
	 * Instantiates a new url.
	 * Create a new URL according to website address
	 * @param websiteAddress the website address
	 */
	public Url(String websiteAddress)
	{
		address = parse(websiteAddress);
		host = getHostByAddress(address);
		path = getPathByAddress(address);
		query = getStringQueryByAddress(address);
	}

	/**
	 * Parses the website address
	 * Three rules of an website address:
	 * 1: Must start with "http://"
	 * 2: Must not consist "www."
	 * 3: Must end with "/"
	 * @param websiteAddress the website address
	 * @return the string
	 */
	private String parse(String websiteAddress)
	{
		websiteAddress = websiteAddress.replace("www.", "");
		if (websiteAddress.indexOf("http://") == -1)
		{
			websiteAddress = "http://" + websiteAddress;
		}
		String lastChar = websiteAddress.substring(websiteAddress.length()-1);
		if (!lastChar.equals("/"))
		{
			websiteAddress = websiteAddress + "/";
		}
		return websiteAddress;
	}

	/**
	 * Gets the path by address. 
	 * @param websiteAddress the website address
	 * @return the path
	 */
	private String getPathByAddress(String websiteAddress)
	{
		websiteAddress =  websiteAddress.replace("http://", "");
		websiteAddress = websiteAddress.replace("www.", "");
		int index1 = websiteAddress.indexOf("/");
		if (index1 != -1)
		{
			int index2 = websiteAddress.indexOf("?");
			if (index2 == -1)
			{
				websiteAddress = websiteAddress.substring(index1);
			}
			else
			{
				websiteAddress = websiteAddress.substring(index1,index2);
			}
			return websiteAddress;
		}
		else
		{
			return "/";
		}
	}

	/**
	 * Gets the string query by address.
	 * @param websiteAddress the website address
	 * @return the string query
	 */
	private String getStringQueryByAddress(String websiteAddress)
	{
		int index = websiteAddress.indexOf("?");
		if (index != -1)
		{
			websiteAddress = websiteAddress.substring(index+1);
			return websiteAddress;
		}
		else
		{
			return "";
		}
	}
	
	/**
	 * Gets the host by address.
	 *
	 * @param websiteAddress the website address
	 * @return the host name
	 */
	private String getHostByAddress(String websiteAddress)
	{
		websiteAddress =  websiteAddress.replace("http://", "");
		websiteAddress = websiteAddress.replace("www.", "");
		int index1 = websiteAddress.indexOf("/");
		if (index1 != -1)
		{
			websiteAddress = websiteAddress.substring(0, index1);
		}
		return websiteAddress;
	}
	
	/**
	 * Gets the host.
	 * Public function
	 * @return the host
	 */
	public String getHost()
	{
		return host;
	}
	
	/**
	 * Gets the port.
	 * Public function
	 * @return the port
	 */
	public int getPort()
	{
		return port;
	}
	
	/**
	 * Gets the path.
	 * Public function
	 * @return the path
	 */
	public String getPath()
	{
		return path;
	}

	/**
	 * Gets the query.
	 * Public function
	 * @return the query
	 */
	public String getQuery()
	{
		return query;
	}
	
	/**
	 * Gets the origin.
	 * Public function
	 * @return the origin
	 */
	public String getOrigin()
	{
		// TODO Auto-generated method stub
		return "http://"+host;
	}

	/**
	 * Gets the address.
	 * Public function
	 * @return the address
	 */
	public String getAddress()
	{
		return address;
	}
}
