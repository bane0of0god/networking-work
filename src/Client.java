

// //////////////////////////////////////////////////////////////////////////////
// ------------------------------------------------------------------------------
// CLIENT
// ------------------------------------------------------------------------------
// //////////////////////////////////////////////////////////////////////////////

import java.io.IOException;


public class Client {
	
	 // ------------------------------------------------------------------------------
	// Miscellaneous
	// ------------------------------------------------------------------------------
	
	static void waitKeyPress(String MESSAGE)
	{
	  System.out.println(MESSAGE + ": Press ENTER to continue...");
	  try {
		while (System.in.available() == 0);
	  } catch (IOException e) {
		System.err.println(e.getMessage());
		System.exit(1);
	  }
	}
	
	// ------------------------------------------------------------------------------
	// MAIN
	// ------------------------------------------------------------------------------

	static final int SERVER_PORT_NUMBER = 63336;

	public static void main(String [] args) throws IOException
	{
		KUSOCKET Socket = new KUSOCKET();
		
		ENDPOINT ClientAddress = new ENDPOINT();
		ENDPOINT ServerAddress = new ENDPOINT("255.255.255.255",SERVER_PORT_NUMBER);
		ENDPOINT SourceAddress = new ENDPOINT();
		MESSAGE Message = new MESSAGE();
		Timer ListenWindow = new Timer();
		String Comment = new String();
		
		Message.BuildMessage(0, 0, "My test message", ("My test message").length());
		
		/// Create socket and make connection ////////////////////////////////////////			
		Socket.CreateUDPSocket(ClientAddress);
		          System.out.println(ClientAddress.DotNotation);
		// Send and receive message /////////////////////////////////////////////////
		Socket.DeliverMessage(Message, ServerAddress);
		
		
		System.err.println("Delivered to (" + ServerAddress.DotNotation + "," +  ServerAddress.Port + ") " + Message.Describe(Comment,256));
		

		int WaitTime = 40; // millisecs
		boolean hasMessageArrived; // Initialise boolean test
		ListenWindow.Start(5); // Period to listen for all replies
		
		while (ListenWindow.isExpired() == false)
		{
			hasMessageArrived = Socket.RetrieveQueuedMessage(WaitTime, Message, SourceAddress);
		
			if (hasMessageArrived)
				System.err.println("Received from (" + SourceAddress.DotNotation + "," + SourceAddress.Port + ") " + Message.Describe(Comment,256));
		}
		
		waitKeyPress("Completed");
		
	}
}

////////////////////////////////////////////////////////////////////////////////
//------------------------------------------------------------------------------
//END
//------------------------------------------------------------------------------
////////////////////////////////////////////////////////////////////////////////