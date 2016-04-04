
public class TokenAccess {

	// Protocol Definitions
	
	public static int SERVER_PORT_NUMBER = 60001;                        


	public static int READ_INTERVAL = 40;	// 40 Millisecs

	public static int firstnode = 10;	// 01 Seconds
	public static int tokentimer = 3;	// 03 Seconds
	public static int tokenlost = 3;	// 3 Seconds
	public static int exit = 5;	// 45 Seconds
	public static int PERIOD_NOCLIENT = 9;	// 90 Seconds 
	
	
	 public static enum STATES {         
		 STATE_INITIAL,         
		 STATE_STARTED,
                 STATE_tokenLess,
                 STATE_TOKENED,		 
		 STATE_EXIT
	} ;

	public static final int PDU_CONNECT = 1;
	public static final int PDU_ACK = 2;
	public static final int PDU_TOKEN = 3;
	public static final int PDU_DATA = 4;
	public static final int PDU_REQUEST = 5;
	public static final int PDU_CLOSE = 6;

	public static final int PDU_TRANSMITTED	= 1;
	public static final int PDU_RECEIVED = 2;

	public static final int COMMENT_LENGTH = 128;

	public static String PDU_Event_Comment(int type, int event) 
	{
		 String comment;
		switch(type)
		{
			case PDU_CONNECT:
				comment = "SOLICIT....";
				break;
			case PDU_ACK:
				comment = "ACK........";
				break;
			case PDU_TOKEN:
				comment = "TOKEN......";
				break;
			case PDU_DATA:
				comment = "DATA.......";
				break;
			case PDU_REQUEST:
				comment = "REQUEST....";
				break;
			case PDU_CLOSE:
				comment = "CLOSE......";
				break;
			default:
				comment = "UNKNOWN....";
				break;
		}

		switch(event)
		{
			case PDU_TRANSMITTED:
				comment = comment + " transmited";
				break;
			case PDU_RECEIVED:
				comment = comment + "... received";
				break;
		}
		return(comment);
	}
	

	public static final int CMD_CONNECT = 1;
	public static final int CMD_DATA = 2;
	public static final int CMD_TERMINATE = 3;

	public class USERINPUT
	{
		public int Type;
	}

	
}