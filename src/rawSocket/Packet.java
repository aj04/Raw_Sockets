package rawSocket;

// Packet format: IP Header || TCP Header || DATA

public class Packet
{
	private byte[] packetContent;
	private byte[] Data;
	private int totalLengthInBits;
	private int totalLengthInBytes;
	protected IPHeader IPheader;
	private TCPHeader TCPheader;
	
	public Packet(int dataLength) // In byte
	{
		totalLengthInBytes = dataLength;
		totalLengthInBits = dataLength * 8;
		Data = new byte[dataLength];
	}

	public void setTcpHeader(TCPHeader tcpHeader)
	{
		this.TCPheader = tcpHeader;
	}
	
	public void setIPHeader(IPHeader iPHeader)
	{
		this.IPheader = iPHeader;
	}
	
	public int getDataLengthInBytes()
	{
		// TODO Auto-generated method stub
		return Data.length;
	}
	
	public void setData(byte[] data)
	{
		Data = data;
		
	}
	
	public int getPacketLength()
	{
		return totalLengthInBits/8;
	}
	
	public void consturctPacket()
	{
		int ipHeaderInBytes = IPheader.getHeaderLengthInBytes();
		int tcpHeaderInBytes = TCPheader.getHeaderLengthInBytes();
		totalLengthInBytes =  ipHeaderInBytes +  tcpHeaderInBytes + Data.length;
		packetContent = new byte[totalLengthInBytes];
		byte [] ipHeader = IPheader.getHeader();
		byte [] tcpHeader = TCPheader.getHeader();
		for (int i = 0; i < ipHeaderInBytes; i++)
		{
			packetContent[i] = ipHeader[i];
		}
		for (int i = ipHeaderInBytes; i < ipHeaderInBytes + tcpHeaderInBytes; i++)
		{
			packetContent[i] = tcpHeader[i - ipHeaderInBytes];
		}
		for (int i = ipHeaderInBytes + tcpHeaderInBytes; i < totalLengthInBytes; i++)
		{
			packetContent[i] = Data[i - ipHeaderInBytes - tcpHeaderInBytes];
		}
	}

	public byte[] getContent()
	{
		return packetContent;
	}

	public byte[] getData()
	{
		return Data;
	}
}
