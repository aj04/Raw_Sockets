package rawSocket;

public class IPHeader
{
	private byte[] ipHeader;
	private byte[] ipHeaderInBits;
	private int lengthInBits;
	private int lengthInBytes;
	
	// Header field
	private int Version = 4; 
	private int IHL = 5; // range [5, 15]Word (4 Byte, 32 bit)
	private int DSCP = 0; 
	private int ECN = 0; 
	private int TotalLength;  //include ip header, tcp header and data, range [20, 65535]Byte
	private int Identification = 0; // For fragmentation
	private int Flags = 2; // 010, do not fragment
	private int FragmentOffset = 0;
	private int TTL = 64; // range [1, 127] Seconds
	private int Protocol = 6; // TCP. full list http://en.wikipedia.org/wiki/List_of_IP_protocol_numbers
	private int Checksum = 0; 
	private long SrcAddress;
	private long DestAddress;
	
	
	public IPHeader(int length) // in Bytes
	{
		if (length % 4 != 0 || length < 20)
		{
			System.out.println("Invalid IP header length");
			System.exit(0);
		}
		IHL = length/4;
		lengthInBytes = length;
		lengthInBits = length*8;
		this.ipHeaderInBits = new byte[lengthInBits]; 
		this.ipHeader = new byte[lengthInBytes];
		setDefaultHeader();
	}

	public IPHeader()
	{
		lengthInBits = IHL * 32;
		lengthInBytes = IHL * 4;
		this.ipHeaderInBits = new byte[lengthInBits]; 
		this.ipHeader = new byte[lengthInBytes];
		setDefaultHeader();
	}

	public IPHeader(byte[] headerContent)
	{
		lengthInBytes = headerContent.length;
		lengthInBits = lengthInBytes * 8;
		this.ipHeader = headerContent;
		this.ipHeaderInBits = arrayByteToBit(ipHeader);
		setHeaderWithContent(ipHeaderInBits);
	}

	private void setHeaderWithContent(byte[] ipHeaderInBits)
	{
		Version = (int) bitArrayToLong(ipHeaderInBits,0,3);
		IHL = (int) bitArrayToLong(ipHeaderInBits,4,7);
		DSCP = (int) bitArrayToLong(ipHeaderInBits,8,13);
		ECN = (int) bitArrayToLong(ipHeaderInBits,14,15);
		TotalLength = (int) bitArrayToLong(ipHeaderInBits,16,31);
		Identification = (int) bitArrayToLong(ipHeaderInBits,32,47);
		Flags = (int) bitArrayToLong(ipHeaderInBits,48,50);
		FragmentOffset = (int) bitArrayToLong(ipHeaderInBits,50,63);
		TTL = (int) bitArrayToLong(ipHeaderInBits,64,71);
		Protocol = (int) bitArrayToLong(ipHeaderInBits,72,79);
		Checksum = (int) bitArrayToLong(ipHeaderInBits,80,95);
		SrcAddress = bitArrayToLong(ipHeaderInBits,96,127);
		DestAddress = bitArrayToLong(ipHeaderInBits,128,159);
	}

	private void setDefaultHeader()
	{
		setVersion(Version);
		SetIHL();  // Byte
		SetDSCP(DSCP); 
		SetECN(ECN);
		SetIdentification(Identification);
		SetFlags(Flags);
		SetFragmentOffset(FragmentOffset);
		SetTTL(TTL);
		SetProtocol(Protocol);
		SetChecksum(Checksum);
		SetSrcAddress("0.0.0.2");
		SetDestAddress("0.0.0.1");
	}
	
	public void SetDestAddress(String destAddress)
	{
		byte[] destAddressBit = addressToBitArray(destAddress);
		DestAddress = bitArrayToLong(destAddressBit);
		int startIndex = 128;
		int endIndex = 159;
		ipHeaderInBits = replaceBitArray(ipHeaderInBits, startIndex, endIndex, destAddressBit);
	}
	

	public void SetSrcAddress(String srcAddress)
	{
		byte[] srcAddressBit = addressToBitArray(srcAddress);
		SrcAddress = bitArrayToLong(srcAddressBit);
		int startIndex = 96;
		int endIndex = 127;
		ipHeaderInBits = replaceBitArray(ipHeaderInBits, startIndex, endIndex, srcAddressBit);
	}
	
	public void SetChecksum(int checksum)
	{
		if (checksum < 0 || checksum > 65534)
		{
			System.out.println("Invalid Checksum");
			System.exit(0);
		}
		int startIndex = 80;
		int endIndex = 95;
		int length = endIndex - startIndex + 1;
		ipHeaderInBits = replaceBitArray(ipHeaderInBits, startIndex, endIndex, toBitArray(checksum, length));
	}
	
	public void SetProtocol(int protocol)
	{
		if (protocol < 0 || protocol > 255)
		{
			System.out.println("Invalid protocol");
			System.exit(0);
		}
		int startIndex = 72;
		int endIndex = 79;
		int length = endIndex - startIndex + 1;
		ipHeaderInBits = replaceBitArray(ipHeaderInBits, startIndex, endIndex, toBitArray(protocol, length));
	}

	public void SetTTL(int ttl)
	{
		if (ttl < 0 || ttl > 127)
		{
			System.out.println("Invalid TTL");
			System.exit(0);
		}
		int startIndex = 64;
		int endIndex = 71;
		int length = endIndex - startIndex + 1;
		ipHeaderInBits = replaceBitArray(ipHeaderInBits, startIndex, endIndex, toBitArray(Math.max(ttl, 1), length));
	}
	
	
	public void SetFragmentOffset(int fragmentOffset)
	{
		if (fragmentOffset < 0 || fragmentOffset > 8191)
		{
			System.out.println("Invalid fragment offset");
			System.exit(0);
		}
		int startIndex = 51;
		int endIndex = 63;
		int length = endIndex - startIndex + 1;
		ipHeaderInBits = replaceBitArray(ipHeaderInBits, startIndex, endIndex, toBitArray(fragmentOffset, length));
	}
	
	public void SetFlags(int flags)
	{
		if (flags < 0 || flags > 7)
		{
			System.out.println("Invalid flags");
			System.exit(0);
		}
		int startIndex = 48;
		int endIndex = 50;
		int length = endIndex - startIndex + 1;
		ipHeaderInBits = replaceBitArray(ipHeaderInBits, startIndex, endIndex, toBitArray(flags, length));
	}
	
	public void SetIdentification(int Identification)
	{
		if (Identification < 0 || Identification > 65535)
		{
			System.out.println("Invalid Identification");
			System.exit(0);
		}
		int startIndex = 32;
		int endIndex = 47;
		int length = endIndex - startIndex + 1;
		ipHeaderInBits = replaceBitArray(ipHeaderInBits, startIndex, endIndex, toBitArray(Identification, length));
	}
	
	public void SetTotalLength(int datalength, int tcpHeaderLength)
	{
		TotalLength = lengthInBits / 8 + datalength + tcpHeaderLength;
		if (TotalLength < 0 || TotalLength > 65535)
		{
			System.out.println("Invalid total length");
			System.exit(0);
		}
		int startIndex = 16;
		int endIndex = 31;
		int length = endIndex - startIndex + 1;
		ipHeaderInBits = replaceBitArray(ipHeaderInBits, startIndex, endIndex, toBitArray(TotalLength, length));
	}
	
	public void SetECN(int ecn)
	{
		if (ecn < 0 || ecn > 3)
		{
			System.out.println("Invalid ECN");
			System.exit(0);
		}
		int startIndex = 14;
		int endIndex = 15;
		int length = endIndex - startIndex + 1;
		ipHeaderInBits = replaceBitArray(ipHeaderInBits, startIndex, endIndex, toBitArray(ecn, length));
	}
	
	
	public void SetDSCP(int dscp)
	{
		if (dscp < 0 || dscp > 65)
		{
			System.out.println("Invalid DSCP");
			System.exit(0);
		}
		int startIndex = 8;
		int endIndex = 13;
		int length = endIndex - startIndex + 1;
		ipHeaderInBits = replaceBitArray(ipHeaderInBits, startIndex, endIndex, toBitArray(dscp, length));
	}
	
	public void SetIHL()
	{
		int startIndex = 4;
		int endIndex = 7;
		int length = endIndex - startIndex + 1;
		ipHeaderInBits = replaceBitArray(ipHeaderInBits, startIndex, endIndex, toBitArray(IHL, length));
	}
	
	public void setVersion(int version)
	{
		if (Version != 4 && Version != 6)
		{
			System.out.println("Invalid IP Vesion");
			System.exit(0);
		}
		int startIndex = 0;
		int endIndex = 3;
		int length = endIndex - startIndex + 1;
		ipHeaderInBits = replaceBitArray(ipHeaderInBits, startIndex, endIndex, toBitArray(version, length));
	}

	public byte[] replaceBitArray(byte[] bitBrray, int startIndex, int endIndex, byte[] subBitArray)
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

	public byte[] toBitArray(long longValue, int length)
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
	
	public byte[] addressToBitArray(String address)
	{
		byte[] addressByte = new byte[32];
		String delims = "[.]+";
		String[] addressTokens = address.split(delims);
		if (addressTokens.length != 4)
		{
			System.out.println("Invalid address");
			System.exit(0);
		}
		for (int i = 0; i < 4; i++)
		{
			int addressTokenInt = Integer.parseInt(addressTokens[i]);
			if (addressTokenInt < 0 || addressTokenInt > 255)
			{
				System.out.println("Invalid address");
				System.exit(0);
			}
			byte[] addressTokenBit = toBitArray(addressTokenInt, 8);
			for (int j = 0; j < 8; j++)
				addressByte[i * 8 + j] = addressTokenBit[j];
		}
		return addressByte;
	}

	public int computeCheckSum()
	{
		int checksum = 0;
		// Before compute checksum, set original checksum to be 0
		int startIndex = 80;
		int endIndex = 95;
		int length = 16;
		ipHeaderInBits = replaceBitArray(ipHeaderInBits, startIndex, endIndex, toBitArray(0, length));
		for (int i = 0; i < ipHeaderInBits.length / 16; i++)
		{
			startIndex = i * 16;
			endIndex = startIndex + 15;
			checksum += bitArrayToLong(ipHeaderInBits, startIndex, endIndex);
		}
		if (checksum > Math.pow(2, 16) - 1)
		{
			int carry = checksum >> 16;
			checksum = (int) (checksum % Math.pow(2, 16) + carry);
		}
		return checksum^0xffff;
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
	
	public long getSrcAddress()
	{
		return SrcAddress;
	}
	
	public long getDestAddress()
	{
		return DestAddress;
	}

	public int getProtocol()
	{
		return Protocol;
	}

	public String[] getDataInHex()
	{
		int length = ipHeaderInBits.length / 4;
		String[] headerHex = new String[length];
		for (int i = 0; i < length; i++)
		{
			int startIndex = 4 * i;
			int endIndex = startIndex + 3;
			headerHex[i] = Integer.toString((int) bitArrayToLong(ipHeaderInBits, startIndex, endIndex), 16);
		}
		return headerHex;
	}
	
	public int getHeaderLengthInBytes()
	{
		return lengthInBytes;
	}
	
	public int getHeaderLengthInBits()
	{
		return lengthInBits;
	}
	
	public byte[] getHeader()
	{
		ipHeader = arrayBitToByte(ipHeaderInBits);
		return ipHeader;
	}

	private byte[] arrayBitToByte(byte[] bitArray)
	{
		byte[] byteArray = new byte[bitArray.length/8];
		for (int i = 0; i < byteArray.length; i++)
		{
			int startIndex = 8 * i;
			int endIndex = startIndex + 7;
			byte intValue = (byte) bitArrayToLong(bitArray, startIndex, endIndex);
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
		for(int i = 0; i < 8; i++)
		{
			bitArray[i] = (byte) ((byteNumber>>(7-i)) & 0x1);
		}
		return bitArray;
	}

	public void verifyChecksum()
	{
		long total=0;
		for (int i = 0; i < ipHeaderInBits.length / 16; i++)
		{
			int startIndex = i * 16;
			int endIndex = startIndex + 15;
			total += bitArrayToLong(ipHeaderInBits, startIndex, endIndex);
		}
		StringBuilder sb = new StringBuilder();
		byte [] stupid = arrayBitToByte(ipHeaderInBits);
		for (byte b : stupid) {
	        sb.append(String.format("%02X ", b));
	    }
		System.out.println(sb);
		System.out.println(Long.toHexString(total));
	}

	
}

