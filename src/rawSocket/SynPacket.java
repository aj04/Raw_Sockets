package rawSocket;

import java.net.InetAddress;

public class SynPacket extends Packet
{
	public SynPacket(int dataLength)
	{
		super(dataLength);
	}

	public SynPacket(InetAddress serverAddress)
	{
		super(0);
		TCPHeader tcpHeader = new TCPHeader(20);
		IPHeader ipHeader = new IPHeader(20);
		ipHeader.SetDestAddress("216.97.236.245");
		ipHeader.SetSrcAddress("129.10.63.122");
		ipHeader.SetIdentification(12093);
		ipHeader.SetTotalLength(this.getDataLengthInBytes(),
				tcpHeader.getHeaderLengthInBytes());
		ipHeader.SetFlags(0);
		ipHeader.SetTTL(255);
		ipHeader.SetChecksum(ipHeader.computeCheckSum());
		tcpHeader.setSrcPort(35951);
		tcpHeader.setDestPort(80);
		tcpHeader.SetSeqNumber(0);
		tcpHeader.SetAckNumber(0);
		tcpHeader.SetSYN(1);
		tcpHeader.SetWindow(5840);
		tcpHeader
				.SetChecksum(tcpHeader.computeChecksum(ipHeader, this.getData()));
		this.setTcpHeader(tcpHeader);
		this.setIPHeader(ipHeader);
		// synPacket.setData();
		this.consturctPacket();
	}

	public IPHeader getIpHeader()
	{
		// TODO Auto-generated method stub
		return this.IPheader;
	}
}
