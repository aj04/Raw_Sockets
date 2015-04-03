package rawSocket;


public class AckPacket extends Packet
{
	public AckPacket(int dataLength)
	{
		super(dataLength);
	}

	public AckPacket(long ackNumber, long sequenceNumber)
	{
		super(0);
		TCPHeader tcpHeader = new TCPHeader(20);
		IPHeader ipHeader = new IPHeader(20);
		ipHeader.SetDestAddress("216.97.236.245");
		ipHeader.SetSrcAddress("129.10.63.122");
		ipHeader.SetIdentification(63129);
		ipHeader.SetTotalLength(this.getDataLengthInBytes(),
				tcpHeader.getHeaderLengthInBytes());
		ipHeader.SetFlags(0);
		ipHeader.SetTTL(255);
		ipHeader.SetChecksum(ipHeader.computeCheckSum());
		tcpHeader.setSrcPort(1234);
		tcpHeader.setDestPort(80);
		tcpHeader.SetSeqNumber(ackNumber);
		tcpHeader.SetAckNumber(sequenceNumber + 1);
		tcpHeader.SetSYN(0);
		tcpHeader.SetACK(1);
		tcpHeader.SetWindow(5840);
		tcpHeader
				.SetChecksum(tcpHeader.computeChecksum(ipHeader, this.getData()));
		this.setTcpHeader(tcpHeader);
		this.setIPHeader(ipHeader);
		this.consturctPacket();
	}

	public IPHeader getIpHeader()
	{
		// TODO Auto-generated method stub
		return this.IPheader;
	}
}
