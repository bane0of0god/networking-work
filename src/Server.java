
// //////////////////////////////////////////////////////////////////////////////
// ------------------------------------------------------------------------------
// TestUDPSocket Server
// ------------------------------------------------------------------------------
// //////////////////////////////////////////////////////////////////////////////

import java.io.IOException;


public class Server {
	
	// ------------------------------------------------------------------------------
	// Miscellaneous
	// ------------------------------------------------------------------------------
	static void waitKeyPress(String message)
	{
	  System.out.println(message + ": Press ENTER to continue...");
	  try {
		while (System.in.available() == 0);
	  } catch (IOException e) {
		System.err.println(e.getMessage());
		System.exit(1);
	  }
	}

	static final int SERVER_PORT_NUMBER = 63336;
	
	// //////////////////////////////////////////////////////////////////////////////
	// ------------------------------------------------------------------------------
	// MAIN
	// ------------------------------------------------------------------------------
	// //////////////////////////////////////////////////////////////////////////////

	public static void main(String [] args) throws IOException
	{
		KUSOCKET Socket = new KUSOCKET();
		ENDPOINT ServerAddress = new ENDPOINT("0.0.0.0", SERVER_PORT_NUMBER);
		ENDPOINT SourceAddress = new ENDPOINT();
		MESSAGE Message = new MESSAGE();
		String Comment = new String();
		
		// Create socket and await connection ///////////////////////////////////////
		Socket.CreateUDPSocket(ServerAddress);
		
		// Read and return message //////////////////////////////////////////////////
		
		System.err.print("Listening ....");
		while(true)
		{
			
			// Read message ---------------------------------------------------------
		
			int WaitTime = 40; //millisecs
			boolean hasMessageArrived = false; //Initialise boolean test
			
			hasMessageArrived = Socket.RetrieveQueuedMessage(WaitTime, Message, SourceAddress);
			
			// Return message -------------------------------------------------------
			
			if (hasMessageArrived == true)
			{
				System.err.println("Received from (" + SourceAddress.DotNotation + "," + SourceAddress.Port + ") " + Message.Describe(Comment,256));
				
				// Return Message
				Socket.DeliverMessage(Message, SourceAddress);
				
				System.err.println("Delivered to (" + SourceAddress.DotNotation + "," + SourceAddress.Port + ") " + Message.Describe(Comment,256));
			}
		}
	}
	
}
////////////////////////////////////////////////////////////////////////////////
//------------------------------------------------------------------------------
//END
//------------------------------------------------------------------------------
////////////////////////////////////////////////////////////////////////////////