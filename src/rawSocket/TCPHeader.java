package rawSocket;

public class TCPHeader
{
	private byte[] tcpHeader;
	private byte[] tcpHeaderInBits;
	private int lengthInBits;
	private int lengthInBytes;

	// TCP Header field
	private int SrcPort = 43428;
	private int DestPort = 80;
	private long SeqNumber = 0;
	private long AckNumber = 0;
	private int DataOffset = 5; // Byte
	private int Reserved = 0;
	private int NS = 0;
	private int CWR = 0;
	private int ECE = 0;
	private int URG = 0;
	private int ACK = 0;
	private int PSH = 0;
	private int RST = 0;
	private int SYN = 0;
	private int FIN = 0;
	private int Window = 29213;
	private int Checksum = 0;
	private int URGpointer = 0;
	@SuppressWarnings("unused")
	private int Options;

	public TCPHeader(int length)
	{
		if (length % 4 != 0 || length < 20)
		{
			System.out.println("Invalid TCP header length");
			System.exit(0);
		}
		DataOffset = length / 4;
		lengthInBits = length * 8;
		lengthInBytes = length;
		this.tcpHeaderInBits = new byte[lengthInBits];
		this.tcpHeader = new byte[lengthInBytes];
		setDefaultHeader();
	}

	public TCPHeader()
	{
		lengthInBits = DataOffset * 32;
		lengthInBytes = DataOffset * 4;
		this.tcpHeaderInBits = new byte[lengthInBits];
		this.tcpHeader = new byte[lengthInBytes];
		setDefaultHeader();
		System.out.println(tcpHeaderInBits);
	}

	public TCPHeader(byte[] headerContent)
	{
		tcpHeader = headerContent;
		lengthInBytes = headerContent.length;
		lengthInBits = lengthInBytes * 8;
		this.tcpHeader = headerContent;
		this.tcpHeaderInBits = arrayByteToBit(tcpHeader);
		setHeaderWithContent(tcpHeaderInBits);
	}

	private void setHeaderWithContent(byte[] tcpHeaderInBits)
	{
		SrcPort = (int) bitArrayToLong(tcpHeaderInBits, 0, 15);
		DestPort = (int) bitArrayToLong(tcpHeaderInBits, 16, 31);
		SeqNumber = bitArrayToLong(tcpHeaderInBits, 32, 63);
		AckNumber = bitArrayToLong(tcpHeaderInBits, 64, 95);
		DataOffset = (int) bitArrayToLong(tcpHeaderInBits, 96, 99);
		Reserved = (int) bitArrayToLong(tcpHeaderInBits, 100, 102);
		NS = (int) bitArrayToLong(tcpHeaderInBits, 103, 103);
		CWR = (int) bitArrayToLong(tcpHeaderInBits, 104, 104);
		ECE = (int) bitArrayToLong(tcpHeaderInBits, 105, 105);
		URG = (int) bitArrayToLong(tcpHeaderInBits, 106, 106);
		ACK = (int) bitArrayToLong(tcpHeaderInBits, 107, 107);
		PSH = (int) bitArrayToLong(tcpHeaderInBits, 108, 108);
		RST = (int) bitArrayToLong(tcpHeaderInBits, 109, 109);
		SYN = (int) bitArrayToLong(tcpHeaderInBits, 110, 110);
		FIN = (int) bitArrayToLong(tcpHeaderInBits, 111, 111);
		Window = (int) bitArrayToLong(tcpHeaderInBits, 112, 127);
		Checksum = (int) bitArrayToLong(tcpHeaderInBits, 128, 143);
		URGpointer = (int) bitArrayToLong(tcpHeaderInBits, 144, 159);
		Options = (int) bitArrayToLong(tcpHeaderInBits, 160, lengthInBits - 1);
	}

	private void setDefaultHeader()
	{
		setSrcPort(SrcPort);
		setDestPort(DestPort);
		SetSeqNumber(SeqNumber);
		SetAckNumber(AckNumber);
		SetDataOffset(); // Byte
		SetReserved(Reserved);
		SetNS(NS);
		SetCWR(CWR);
		SetECE(ECE);
		SetURG(URG);
		SetACK(ACK);
		SetPSH(PSH);
		SetRST(RST);
		SetSYN(SYN);
		SetFIN(FIN);
		SetWindow(Window);
		SetChecksum(Checksum);
		SetURGPointer(URGpointer);
	}

	public void SetURGPointer(int urgpointer)
	{
		if (urgpointer < 0 || urgpointer > 65535 || (URG == 0 && urgpointer != 0)) // Does
																					// not
																					// support
																					// real
																					// big
																					// sequence
		{
			System.out.println("Invalid URGPointer");
			System.exit(0);
		}
		int startIndex = 144;
		int endIndex = 159;
		int length = endIndex - startIndex + 1;
		tcpHeaderInBits = replaceBitArray(tcpHeaderInBits, startIndex, endIndex,
				toBitArray(urgpointer, length));
	}

	public void SetChecksum(int checksum)
	{
		if (checksum < 0 || checksum > 65535) // Does not support real big
												// sequence
		{
			System.out.println("Invalid checksum");
			System.exit(0);
		}
		int startIndex = 128;
		int endIndex = 143;
		int length = endIndex - startIndex + 1;
		tcpHeaderInBits = replaceBitArray(tcpHeaderInBits, startIndex, endIndex,
				toBitArray(checksum, length));
	}

	public void SetWindow(int window)
	{
		if (window < 0 || window > 65535) // Does not support real big sequence
		{
			System.out.println("Invalid Window number");
			System.exit(0);
		}
		int startIndex = 112;
		int endIndex = 127;
		int length = endIndex - startIndex + 1;
		tcpHeaderInBits = replaceBitArray(tcpHeaderInBits, startIndex, endIndex,
				toBitArray(window, length));
	}

	public void SetFIN(int fin)
	{
		if (fin < 0 || fin > 1) // Does not support real big sequence
		{
			System.out.println("Invalid FIN number");
			System.exit(0);
		}
		int startIndex = 111;
		int endIndex = 111;
		int length = endIndex - startIndex + 1;
		tcpHeaderInBits = replaceBitArray(tcpHeaderInBits, startIndex, endIndex,
				toBitArray(fin, length));
	}

	public void SetSYN(int syn)
	{
		if (syn < 0 || syn > 1) // Does not support real big sequence
		{
			System.out.println("Invalid SYN number");
			System.exit(0);
		}
		int startIndex = 110;
		int endIndex = 110;
		int length = endIndex - startIndex + 1;
		tcpHeaderInBits = replaceBitArray(tcpHeaderInBits, startIndex, endIndex,
				toBitArray(syn, length));
	}

	public void SetRST(int rst)
	{
		if (rst < 0 || rst > 1) // Does not support real big sequence
		{
			System.out.println("Invalid RST number");
			System.exit(0);
		}
		int startIndex = 109;
		int endIndex = 109;
		int length = endIndex - startIndex + 1;
		tcpHeaderInBits = replaceBitArray(tcpHeaderInBits, startIndex, endIndex,
				toBitArray(rst, length));
	}

	public void SetPSH(int psh)
	{
		if (psh < 0 || psh > 1) // Does not support real big sequence
		{
			System.out.println("Invalid PSH number");
			System.exit(0);
		}
		int startIndex = 108;
		int endIndex = 108;
		int length = endIndex - startIndex + 1;
		tcpHeaderInBits = replaceBitArray(tcpHeaderInBits, startIndex, endIndex,
				toBitArray(psh, length));
	}

	public void SetACK(int ack)
	{
		if (ack < 0 || ack > 1) // Does not support real big sequence
		{
			System.out.println("Invalid ACK number");
			System.exit(0);
		}
		int startIndex = 107;
		int endIndex = 107;
		int length = endIndex - startIndex + 1;
		tcpHeaderInBits = replaceBitArray(tcpHeaderInBits, startIndex, endIndex,
				toBitArray(ack, length));
	}

	public void SetURG(int urg)
	{
		if (urg < 0 || urg > 1) // Does not support real big sequence
		{
			System.out.println("Invalid URG number");
			System.exit(0);
		}
		int startIndex = 106;
		int endIndex = 106;
		int length = endIndex - startIndex + 1;
		tcpHeaderInBits = replaceBitArray(tcpHeaderInBits, startIndex, endIndex,
				toBitArray(urg, length));
	}

	public void SetECE(int ece)
	{
		if (ece < 0 || ece > 1) // Does not support real big sequence
		{
			System.out.println("Invalid ECE number");
			System.exit(0);
		}
		int startIndex = 105;
		int endIndex = 105;
		int length = endIndex - startIndex + 1;
		tcpHeaderInBits = replaceBitArray(tcpHeaderInBits, startIndex, endIndex,
				toBitArray(ece, length));
	}

	public void SetCWR(int cwr)
	{
		if (cwr < 0 || cwr > 1) // Does not support real big sequence
		{
			System.out.println("Invalid CWR number");
			System.exit(0);
		}
		int startIndex = 104;
		int endIndex = 104;
		int length = endIndex - startIndex + 1;
		tcpHeaderInBits = replaceBitArray(tcpHeaderInBits, startIndex, endIndex,
				toBitArray(cwr, length));
	}

	public void SetNS(int ns)
	{
		if (ns < 0 || ns > 1) // Does not support real big sequence
		{
			System.out.println("Invalid NS number");
			System.exit(0);
		}
		int startIndex = 103;
		int endIndex = 103;
		int length = endIndex - startIndex + 1;
		tcpHeaderInBits = replaceBitArray(tcpHeaderInBits, startIndex, endIndex,
				toBitArray(ns, length));
	}

	public void SetReserved(int reserved)
	{
		int startIndex = 100;
		int endIndex = 102;
		int length = endIndex - startIndex + 1;
		tcpHeaderInBits = replaceBitArray(tcpHeaderInBits, startIndex, endIndex,
				toBitArray(reserved, length));
	}

	public void SetDataOffset()
	{
		int startIndex = 96;
		int endIndex = 99;
		int length = endIndex - startIndex + 1;
		tcpHeaderInBits = replaceBitArray(tcpHeaderInBits, startIndex, endIndex,
				toBitArray(DataOffset, length));
	}

	public void SetAckNumber(long ackNumber)
	{
		if (ackNumber < 0 || ackNumber > Math.pow(2, 32) - 1) // Does not
																// support real
																// big sequence
		{
			System.out.println("Invalid ack number: " + ackNumber);
			System.exit(0);
		}
		int startIndex = 64;
		int endIndex = 95;
		int length = endIndex - startIndex + 1;
		tcpHeaderInBits = replaceBitArray(tcpHeaderInBits, startIndex, endIndex,
				toBitArray(ackNumber, length));
	}

	public void SetSeqNumber(long seqNumber)
	{
		if (seqNumber < 0 || seqNumber > Math.pow(2, 32) - 1) // Does not
																// support real
																// big sequence
		{
			System.out.println("Invalid sequence number");
			System.exit(0);
		}
		int startIndex = 32;
		int endIndex = 63;
		int length = endIndex - startIndex + 1;
		tcpHeaderInBits = replaceBitArray(tcpHeaderInBits, startIndex, endIndex,
				toBitArray(seqNumber, length));
	}

	public void setDestPort(int destPort)
	{
		if (destPort < 0 || destPort > 65535)
		{
			System.out.println("Invalid dest port");
			System.exit(0);
		}
		int startIndex = 16;
		int endIndex = 31;
		int length = endIndex - startIndex + 1;
		tcpHeaderInBits = replaceBitArray(tcpHeaderInBits, startIndex, endIndex,
				toBitArray(destPort, length));
	}

	public void setSrcPort(int srcPort)
	{
		if (srcPort < 0 || srcPort > 65535)
		{
			System.out.println("Invalid source port");
			System.exit(0);
		}
		int startIndex = 0;
		int endIndex = 15;
		int length = endIndex - startIndex + 1;
		tcpHeaderInBits = replaceBitArray(tcpHeaderInBits, startIndex, endIndex,
				toBitArray(srcPort, length));
	}

	private byte[] replaceBitArray(byte[] bitBrray, int startIndex, int endIndex, byte[] subBitArray)
	{
		byte[] newBitArray = new byte[bitBrray.length];
		for (int i = 0; i < startIndex; i++)
			newBitArray[i] = bitBrray[i];
		for (int i = startIndex; i <= endIndex; i++)
			newBitArray[i] = subBitArray[i - startIndex];
		for (int i = endIndex + 1; i < bitBrray.length; i++)
			newBitArray[i] = bitBrray[i];
		return newBitArray;
	}

	private byte[] toBitArray(long longValue, int length)
	{
		byte[] bitArray = new byte[length];
		String bitString = Long.toBinaryString(longValue);
		int zeroFillLength = length - bitString.length();
		for (int i = 0; i < zeroFillLength; i++) // Add 0 on left (big endian)
		{
			bitArray[i] = 0;
		}
		for (int i = zeroFillLength; i < length; i++)
		{
			int indexOfBit = i - zeroFillLength;
			bitArray[i] = (byte) Character.getNumericValue(bitString.charAt(indexOfBit));
		}
		return bitArray;
	}

	// IP change format of address
	// Change function of bit array
	public int computeChecksum(IPHeader ipHeader, byte[] data)
	{
		int checksum = 0;
		long sourceAddress = ipHeader.getSrcAddress();
		long destAddress = ipHeader.getDestAddress();
		int protocol = ipHeader.getProtocol();
		int tcpLength = lengthInBytes + data.length;
		int psudoLength = tcpHeaderInBits.length + 96 + data.length * 8;
		byte[] psudoHeader = new byte[psudoLength];
		psudoHeader = replaceBitArray(psudoHeader, 0, 31, toBitArray(sourceAddress, 32));
		psudoHeader = replaceBitArray(psudoHeader, 32, 63, toBitArray(destAddress, 32));
		psudoHeader = replaceBitArray(psudoHeader, 64, 71, toBitArray(0, 8));
		psudoHeader = replaceBitArray(psudoHeader, 72, 79, toBitArray(protocol, 8));
		psudoHeader = replaceBitArray(psudoHeader, 80, 95, toBitArray(tcpLength, 16));
		psudoHeader = replaceBitArray(psudoHeader, 96, 95 + lengthInBits, tcpHeaderInBits);
		// Handle payload, be careful about two things:
		// (1): if payload == 0, do not try to replace bit array, otherwise overflow
		// (2): if after adding payload the total length is odd number, pad eigth "0" to make it divisible by 16
		// (3): if payload == 0. no need to pad as option is always divisible by 32
		if (data.length > 0)
		{
			psudoHeader = replaceBitArray(psudoHeader, 96 + lengthInBits, psudoLength - 1,
					arrayByteToBit(data));
			if (psudoLength % 16 != 0)
			{
				byte[] pad = new byte[8];
				for (int i = 0; i < 8; i++)
				{
					pad[i] = 0;
				}
				psudoHeader = append(psudoHeader, pad);
			}
		}
		
		// Before compute checksum, set original checksum to be 0
		int startIndex = 224;
		int endIndex = 239;
		int length = 16;
		psudoHeader = replaceBitArray(psudoHeader, startIndex, endIndex, toBitArray(0, length));
		StringBuilder sb = new StringBuilder();
		for (byte b : arrayBitToByte(psudoHeader))
		{
			sb.append(String.format("%02X ", b));
		}
		System.out.println(sb.toString());
		checksum = computeChecksum(psudoHeader);
		return checksum;
	}

	private byte[] append(byte[] oldArray, byte[] pad)
	{
		byte[] newArray = new byte[oldArray.length + pad.length];
		for (int i = 0; i < oldArray.length; i++)
		{
			newArray[i] = oldArray[i];
		}
		for (int i = oldArray.length; i < newArray.length; i++)
		{
			newArray[i] = pad[i - oldArray.length];
		}
		return newArray;
	}

	private long bitArrayToLong(byte[] array, int startIndex, int endIndex)
	{
		int length = endIndex - startIndex + 1;
		byte[] bitArray = new byte[length];
		for (int i = 0; i < length; i++)
		{
			bitArray[i] = array[startIndex + i];
		}
		return bitArrayToLong(bitArray);
	}

	private long bitArrayToLong(byte[] bitArray)
	{
		long result = 0;
		for (int i = 0; i < bitArray.length; i++)
		{
			result += bitArray[i] * Math.pow(2, (bitArray.length - 1 - i));
		}
		return result;
	}

	@SuppressWarnings("unused")
	private String[] getDataInHex(byte[] bitArray)
	{
		int length = bitArray.length / 4;
		String[] hexString = new String[length];
		for (int i = 0; i < length; i++)
		{
			int startIndex = 4 * i;
			int endIndex = startIndex + 3;
			hexString[i] = Integer.toString((int) bitArrayToLong(bitArray, startIndex, endIndex),
					16);
		}
		return hexString;
	}

	private int computeChecksum(byte[] psudoHeader)
	{
		int checksum = 0;
		for (int i = 0; i < psudoHeader.length / 16; i++)
		{
			int startIndex = i * 16;
			int endIndex = startIndex + 15;
			checksum += bitArrayToLong(psudoHeader, startIndex, endIndex);
		}
		if (checksum > Math.pow(2, 16) - 1)
		{
			int carry = checksum >> 16;
			checksum = (int) (checksum % Math.pow(2, 16) + carry);
		}
		return checksum ^ 0xffff;
	}

	public String[] getDataInHex()
	{
		int length = tcpHeaderInBits.length / 4;
		String[] headerHex = new String[length];
		for (int i = 0; i < length; i++)
		{
			int startIndex = 4 * i;
			int endIndex = startIndex + 3;
			headerHex[i] = Integer.toString(
					(int) bitArrayToLong(tcpHeaderInBits, startIndex, endIndex), 16);
		}
		return headerHex;
	}

	public byte[] getHeader()
	{
		tcpHeader = arrayBitToByte(tcpHeaderInBits);
		return tcpHeader;
	}

	public int getHeaderLengthInBytes()
	{
		return lengthInBytes;
	}

	public int getHeaderLengthInBits()
	{
		return lengthInBits;
	}

	private byte[] arrayBitToByte(byte[] bitArray)
	{
		byte[] byteArray = new byte[bitArray.length / 8];
		for (int i = 0; i < byteArray.length; i++)
		{
			int startIndex = 8 * i;
			int endIndex = startIndex + 7;
			int intValue = (int) bitArrayToLong(bitArray, startIndex, endIndex);
			byteArray[i] = (byte) (intValue & 0xff);
		}
		return byteArray;
	}

	private byte[] arrayByteToBit(byte[] byteArray)
	{
		byte[] bitArray = new byte[byteArray.length * 8];
		for (int i = 0; i < byteArray.length; i++)
		{
			int startIndex = 8 * i;
			byte[] bits = byteToBitArray(byteArray[i]);
			for (int j = 0; j < 8; j++)
			{
				bitArray[startIndex + j] = bits[j];
			}
		}
		return bitArray;
	}

	private byte[] byteToBitArray(byte byteNumber)
	{
		byte[] bitArray = new byte[8];
		for (int i = 0; i < 8; i++)
		{
			bitArray[i] = (byte) ((byteNumber >> (7 - i)) & 0x1);
		}
		return bitArray;
	}

	public long getSequenceNumber()
	{
		return SeqNumber;
	}

	public long getAckNumber()
	{
		return AckNumber;
	}
}
