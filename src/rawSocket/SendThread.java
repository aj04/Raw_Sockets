package rawSocket;

import java.io.IOException;
import java.net.InetAddress;

public class SendThread implements Runnable
{
	private RockRawSocket socket;
	private InetAddress destAddress;
	private Packet packet;

	public SendThread(RockRawSocket socket, InetAddress destAddress, Packet packet)
	{
		this.socket = socket;
		this.destAddress = destAddress;
		this.packet = packet;
	}
	
	@Override
	public void run()
	{
		try
		{
			socket.write(destAddress, packet.getContent());
		}
		catch (IOException e)
		{
			System.out.println("I am trapped");
		}
	}

}
