package rawSocket;

public class RawGet
{
	public static void main(String[] args)
	{
		String website = "http://david.choffnes.com/classes/cs4700sp15/project4.php"; //cannot change this!
		RawHttpGet httpGet = new RawHttpGet();
		if (httpGet.downloadPage(website) == -1)
		{
			System.out.println("Downloading fail!");
		}
	}
}
