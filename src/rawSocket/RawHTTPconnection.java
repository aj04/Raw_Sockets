package rawSocket;

import static com.savarese.rocksaw.net.RawSocket.PF_INET;

import java.io.IOException;
import java.net.InetAddress;
import java.util.LinkedList;

/**
 * The Class HTTPconnection.
 */
public class RawHTTPconnection
{

	/** The DEFAULT protocol used */
	private String PROTOCOL = "HTTP/1.1";

	/** The DEFAULT connection. */
	private String CONNECTION = "close";

	/** The DEFAULT cache control. */
	private String CACHE_CONTROL = "max-age=0";

	/** The DEFAULT accept. */
	private String ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8";

	/** The DEFAULT content type. */
	private String CONTENT_TYPE = "application/x-www-form-urlencoded";

	/** The DEFAULT user agent. */
	private String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.99 Safari/537.36";

	/** The DEFAULT accept lunguage. */
	private String ACCEPT_LUNGUAGE = "en-us";

	/** The current url fetched. */
	private Url currentURL;

	/** The linked list of request header. */
	private LinkedList<String> requestHearder;

	/** The linked list of posted message. */
	private LinkedList<String> postMessage;

	/** The Symbol of new line. */
	private String NEW_LINE = "\n";

	/** The socket used to send stuff. */
	private RockRawSocket sendSocket = null;

	/** The socket used to receive stuff. */
	private RockRawSocket receiveSocket = null;

	/** Time out timer. */
	private static int TIMEOUT = 10000;

	/** Socket type */
	private int SOCK_RAW = 3;
	
	/** Protocol type */
	private int IPPROTO_RAW = 255;

	/**
	 * Instantiates a new HTTP connection. Create URL, requestHeader, and
	 * message body
	 * 
	 * @param URL
	 *            the url
	 */
	public RawHTTPconnection(Url URL)
	{
		this.currentURL = URL;
		requestHearder = new LinkedList<String>();
		postMessage = new LinkedList<String>();
	}

	/**
	 * Sets the request. Add new request in request header
	 * 
	 * @param request
	 *            the new request
	 */
	public void setRequest(String request)
	{
		String httpCommand = request + " " + currentURL.getPath() + " " + PROTOCOL + NEW_LINE;
		requestHearder.add(httpCommand);
	}

	/**
	 * Sets the header. Add new header in header list
	 * 
	 * @param headerName
	 *            the header name
	 * @param headerValue
	 *            the header value
	 */
	public void setHeader(String headerName, String headerValue)
	{
		String header = headerName + ": " + headerValue + NEW_LINE;
		requestHearder.add(header);
	}

	/**
	 * Sets the default header.
	 */
	public void setDefaultHeader()
	{
		requestHearder.add("Connection: " + CONNECTION + NEW_LINE);
		requestHearder.add("Cache-Control: " + CACHE_CONTROL + NEW_LINE);
		requestHearder.add("ACCEPT: " + ACCEPT + NEW_LINE);
		requestHearder.add("CONTENT_TYPE: " + CONTENT_TYPE + NEW_LINE);
		requestHearder.add("USER_AGENT: " + USER_AGENT + NEW_LINE);
		requestHearder.add("ACCEPT_LUNGUAGE: " + ACCEPT_LUNGUAGE + NEW_LINE);
	}

	/**
	 * Sets the post content. Add new content POST message body
	 * 
	 * @param sentContent
	 *            the new post content
	 */
	public void setPostContent(String sentContent)
	{
		postMessage.add(sentContent);
	}

	/**
	 * Sets the cookies. Add more cookies in cookie list
	 * 
	 * @param cookies  the new cookies
	 */
	public void setCookies(LinkedList<Cookie> cookies)
	{
		if (cookies != null)
		{
			String sentCookie = cookies.get(0).getName() + "=" + cookies.get(0).getValue();
			for (int i = 1; i < cookies.size(); i++)
			{
				sentCookie = sentCookie + "; " + cookies.get(i).getName() + "="
						+ cookies.get(i).getValue();
			}
			sentCookie = "Cookie: " + sentCookie + NEW_LINE;
			requestHearder.add(sentCookie);
			// System.out.println(sentCookie);
		}
	}

	/**
	 * Process url. Processing URL according to the set of request, request
	 * header and message body (if there is any)
	 * 
	 * @return the page
	 */
	public Page processURL()
	{
		String content = "";
		sendSocket = new RockRawSocket();
		try
		{
			sendSocket.open(PF_INET, 255, 255);
			sendSocket.setSendTimeout(TIMEOUT);
			receiveSocket = new RockRawSocket();
			receiveSocket.open(PF_INET, SOCK_RAW, IPPROTO_RAW);
			receiveSocket.setSendTimeout(TIMEOUT);
			InetAddress hostAddress = InetAddress.getByName(currentURL.getHost());;
			if (sendSocket.connect(hostAddress) == -1)
			{
				sendSocket.close();
				return null;
			}
				
			System.out.println("Connection succeed!");
			String request = "";
			while (!requestHearder.isEmpty())
			{
				request += requestHearder.remove();
			}
			request += "\n";
			System.out.println(request);
			if (sendSocket.Send(request) == -1)
			{
				sendSocket.close();
				return null;
			}
			//return receive(receiveSocket, hostAddress);
		}
		catch (IllegalStateException | IOException e)
		{
			return null;
		}
		Page currentPage = new Page(content);
		return currentPage;
	}
}
