package rawSocket;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import com.savarese.rocksaw.net.RawSocket;

public class RockRawSocket extends RawSocket
{
	private InetAddress serverAddress;
	private RawSocket sock_receive;
	IPHeader ipHeaderRecv;
	TCPHeader tcpHeaderRecv;
	ScheduledThreadPoolExecutor executor = (ScheduledThreadPoolExecutor) Executors
			.newScheduledThreadPool(1);
	SendThread sendPacket;
	
	public int connect(InetAddress address) throws IOException
	{
		serverAddress = address;

		Packet packetSent = new Packet(0);
		IPHeader ipHeaderSent = new IPHeader(20);
		ipHeaderSent.SetDestAddress("216.97.236.245");
		ipHeaderSent.SetSrcAddress("129.10.63.122");
		TCPHeader tcpHeaderSent = new TCPHeader(20);
		tcpHeaderSent.SetSYN(1);
		ipHeaderSent.SetTotalLength(packetSent.getDataLengthInBytes(), tcpHeaderSent.getHeaderLengthInBytes());
		ipHeaderSent.SetChecksum(ipHeaderSent.computeCheckSum());
		tcpHeaderSent
		.SetChecksum(tcpHeaderSent.computeChecksum(ipHeaderSent, packetSent.getData()));
		//ipHeaderSent.verifyChecksum();
		packetSent.setIPHeader(ipHeaderSent);
		packetSent.setTcpHeader(tcpHeaderSent);
		packetSent.consturctPacket();
		
		
		
		//sendPacket = new SendThread(this, serverAddress, packetSent);
		//executor.schedule(sendPacket, 0, TimeUnit.SECONDS);
		this.write(serverAddress, packetSent.getContent());
		//this.write(serverAddress, packetSent.getContent());
		sock_receive = new RawSocket();
		sock_receive.open(17, 3, 0x0300);
		int i = 0;
		while (i < 50)
		{
			byte[] buffer = new byte[100];
			int dataSize = sock_receive.read(buffer, null);
			if (dataSize < 0)
			{
				System.out.println("Recvfrom error , failed to get packets\n");
				return -1;
			}
			else
			{
				// System.out.println(ipHeaderRecv.getSrcAddress());
				/*
				 * StringBuilder sb = new StringBuilder(); for (byte b : buffer)
				 * { sb.append(String.format("%02x ", b)); }
				 * System.out.println(sb.toString());
				 */
				if (parsePacket(buffer, ipHeaderSent)) // IP protocol, else if is 0, ARP
											// protocol
				{
					tcpHeaderRecv = getTCPHeader(buffer, ipHeaderRecv);
					/*
					 * for (String s:tcpHeaderRecv.getDataInHex()) {
					 * System.out.print(s); }
					 */
					// System.out.print("\n");
					/*
					 * //System.out.print(Long.toHexString(sequenceNumber));
					 * AckPacket ackPacket = new AckPacket(ackNumber,
					 * sequenceNumber); ScheduledThreadPoolExecutor executorACK
					 * = (ScheduledThreadPoolExecutor) Executors
					 * .newScheduledThreadPool(1);
					 */
					ipHeaderSent.SetIdentification(2);
					ipHeaderSent.SetChecksum(ipHeaderSent.computeCheckSum());
					tcpHeaderSent.SetSYN(0);
					tcpHeaderSent.SetACK(1);
					tcpHeaderSent.SetSeqNumber(tcpHeaderRecv.getAckNumber());
					tcpHeaderSent.SetAckNumber(tcpHeaderRecv.getSequenceNumber());
					tcpHeaderSent
					.SetChecksum(tcpHeaderSent.computeChecksum(ipHeaderSent, packetSent.getData()));
					//packetSent.setTcpHeader(tcpHeaderSent);
					packetSent.consturctPacket();
					//sendPacket = new SendThread(this, serverAddress, packetSent);
					// SendThread sendACK = new SendThread(this, serverAddress,
					// ackPacket);
					//executor.schedule(sendPacket, 0, TimeUnit.SECONDS);
					this.write(serverAddress, packetSent.getContent());
					//this.write(serverAddress, packetSent.getContent());
					//this.write(serverAddress, packetSent.getContent());
					return 1;
				}
			}
			i++;
			// Now process the packet
		}
		executor.shutdown();
		return -1;
	}

	private boolean parsePacket(byte[] buffer, IPHeader ipHeaderSent)
	{
		int ipProtocolVersion = buffer[14] >> 4;
		int tcpProtocolVersion = buffer[23];
		if (ipProtocolVersion == 4 && tcpProtocolVersion == 6)
		{
			ipHeaderRecv = getIpHeader(buffer);
			if (ipHeaderRecv.getSrcAddress() == ipHeaderSent.getDestAddress())
				return true;
		}
		return false;
	}

	private TCPHeader getTCPHeader(byte[] buffer, IPHeader ipHeaderRecv)
	{
		int startIndex = 14 + ipHeaderRecv.getHeaderLengthInBytes();
		int tcpHeaderLength = 4 * ((buffer[startIndex + 12] >> 4) & 0xF);
		byte[] headerContent = new byte[tcpHeaderLength];
		for (int i = 0; i < tcpHeaderLength; i++)
		{
			headerContent[i] = buffer[startIndex + i];
		}
		TCPHeader header = new TCPHeader(headerContent);
		return header;
	}

	private IPHeader getIpHeader(byte[] buffer)
	{
		byte versionIHL = buffer[14];
		int ipHeaderLength = 4 * (versionIHL & 0xF);
		byte[] headerContent = new byte[ipHeaderLength];
		for (int i = 0; i < ipHeaderLength; i++)
		{
			headerContent[i] = buffer[i + 14];
		}

		IPHeader header = new IPHeader(headerContent);
		return header;
	}

	public int Send(String data)
	{
		// Computing of checksum is not right! And you forgot to set the real value in each class
		byte[] dataFlow = data.getBytes();
		StringBuilder sb = new StringBuilder();
	    for (byte b : dataFlow) {
	        sb.append(String.format("%02X ", b));
	    }
	    //System.out.println(sb.toString());
		Packet packetSent = new Packet(dataFlow.length);
		//Packet packetSent = new Packet(0);
		packetSent.setData(dataFlow);
		IPHeader ipHeaderSent = new IPHeader(20);
		ipHeaderSent.SetDestAddress("216.97.236.245");
		ipHeaderSent.SetSrcAddress("129.10.63.122");
		TCPHeader tcpHeaderSent = new TCPHeader(20);
		ipHeaderSent.SetTotalLength(packetSent.getDataLengthInBytes(), tcpHeaderSent.getHeaderLengthInBytes());
		ipHeaderSent.SetIdentification(2);
		ipHeaderSent.SetChecksum(ipHeaderSent.computeCheckSum());
		tcpHeaderSent.SetSYN(0);
		tcpHeaderSent.SetACK(1);
		tcpHeaderSent.SetPSH(1);
		tcpHeaderSent.SetSeqNumber(tcpHeaderRecv.getAckNumber());
		//tcpHeaderSent.SetSeqNumber(tcpHeaderRecv.getAckNumber());
		//tcpHeaderSent.SetAckNumber(tcpHeaderRecv.getSequenceNumber());
		tcpHeaderSent.SetAckNumber(tcpHeaderRecv.getSequenceNumber() + 1);
		tcpHeaderSent
		.SetChecksum(tcpHeaderSent.computeChecksum(ipHeaderSent, packetSent.getData()));
		packetSent.setIPHeader(ipHeaderSent);
		packetSent.setTcpHeader(tcpHeaderSent);
		packetSent.consturctPacket();
		try
		{
			this.write(serverAddress, packetSent.getContent());
		}
		catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//endPacket = new SendThread(this, serverAddress, packetSent);
		//executor.schedule(sendPacket, 0, TimeUnit.SECONDS);
		//executor.shutdown();
		while (true)
		{
			byte[] buffer = new byte[100];
			try
			{
				@SuppressWarnings("unused")
				int dataSize = sock_receive.read(buffer, null);
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				return -1;
			}
			break;
		}
		return -1;
	}
}
