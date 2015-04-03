package rawSocket;

/**
 * The Class Crawler.
 */
public class RawHttpGet
{
	/** The http status OK. */
	private final int HTTP_OK = 200;

	/**
	 * Gets the page content. Setting get parameters, like Host and cookies Set
	 * all other default headers
	 * 
	 * @param URL
	 *            the url
	 * @return the page content
	 */
	private Page getPageContent(Url URL)
	{
		RawHTTPconnection conn = new RawHTTPconnection(URL);
		conn.setRequest("GET");
		conn.setHeader("Host", URL.getHost());
		//conn.setDefaultHeader();
		Page page = conn.processURL();
		return page;
	}

	public int downloadPage(String website)
	{
		Url currentWebsite = new Url(website);
		Page currentPage = getPageContent(currentWebsite);
		if (currentPage != null)
		{
			int responseCode = currentPage.getResponseCode();
			if (responseCode != HTTP_OK)
			{
				System.out.println("Response code is not correct: " + responseCode);
				return -1;
			}
			String htmlString = currentPage.getBody().toString();
			System.out.println(htmlString);
			return 0;
		}
		else
		{
			return -1;
		}
	}
}