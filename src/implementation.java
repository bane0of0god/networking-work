
import java.io.IOException;
import java.net.InetAddress;
 import org.apache.commons.lang3.ArrayUtils; 

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Roshan
 */
public class implementation {
    public static void main(String [] args) throws IOException 
	{
		// Network Variables
		KUSOCKET ClientSocket = new KUSOCKET();                   
	    ENDPOINT ClientAddress = new ENDPOINT("0.0.0.0",0);         
	    ENDPOINT  BroadcastAddress = new ENDPOINT("255.255.255.255", TokenAccess.SERVER_PORT_NUMBER);  
            		ENDPOINT AnyAddress = new ENDPOINT("0.0.0.0",0); 

	    ENDPOINT ServerAddress = new ENDPOINT();         
	    MESSAGE OutgoingMessage = new MESSAGE();         
	    MESSAGE IncomingMessage = new MESSAGE();        
	      
	    // Output variables         
	    int CommentLength= TokenAccess.COMMENT_LENGTH;         
	    String Comment = new String();

	    // Protocol variables
		Timer firstnode = new Timer();
		Timer tokentimer = new Timer();
		Timer tokenlost = new Timer();
                Timer exit = new Timer();
		int DialogueNumber = 0;
		int numRetries = 0;
		 Integer addressIP[] = new Integer[2];
                 int trigger = 0;
                 String lanAddress= "192.168.1.67";
// Create socket and await connection ///////////////////////////////////////
		

		// --------------------------------------------------------------------------
		// FINITE STATE MACHINE
		// --------------------------------------------------------------------------
		  
		System.err.println("FSM Started ===================" + '\n');

		// FSM variables
		TokenAccess.STATES state = TokenAccess.STATES.STATE_INITIAL; // Initial STATE
		TokenAccess.STATES lastState = state;     // Last STATE
		boolean bContinueEventWait = false; 
		boolean bContinueStateLoop = true; 
                String last =null;
	ClientSocket.CreateUDPSocket(ClientAddress);
                while(bContinueStateLoop)
		{
                    
                    
                     switch(state)
			{
				case STATE_INITIAL: 
                                    
                                    firstnode.Start(TokenAccess.firstnode);
                                    state = TokenAccess.STATES.STATE_STARTED;
                                    bContinueEventWait = false; // Stop Events loop
				    break;
                                    
                                case STATE_STARTED: 
                                    
                                    ClientSocket.MakeConnection(AnyAddress);
                                    
                                    if(firstnode.isExpired()){
                                        tokentimer.Start(TokenAccess.tokentimer);
                                      
                                        System.out.println("implementation.main()");
                                        // adding  local host ip to array
                                         String t =  InetAddress.getLocalHost().getHostAddress();
                                          String[] split = t.split("\\.");
                                        addressIP[0] = Integer.parseInt(split[3]);
                                        System.out.println(split[3]);
                                        state =TokenAccess.STATES.STATE_TOKENED;
                                        bContinueEventWait = false;// Stop Events loop
                                        
                                    }
                                    boolean isMessageQueued = false;
                                    
                                    isMessageQueued = ClientSocket.RetrieveQueuedMessage(TokenAccess.READ_INTERVAL, IncomingMessage, ServerAddress);
                                   
                                    if (isMessageQueued&& (IncomingMessage.Type==TokenAccess.PDU_CONNECT) ){
                                        System.out.println("ring exists");
                                        //pdu_connect used instead of ring exisits 
                                        state = TokenAccess.STATES.STATE_tokenLess;
                                    bContinueEventWait = false;
                                    
                                    }
                                    
                                case STATE_tokenLess:
                                     ClientSocket.MakeConnection(AnyAddress);
                                   while (state == TokenAccess.STATES.STATE_tokenLess){
                                   	// Instructs the socket to accept

                                    // building ip address message
                                    OutgoingMessage.BuildMessage(++DialogueNumber, TokenAccess.PDU_CONNECT,InetAddress.getLocalHost().getHostAddress(),InetAddress.getLocalHost().getHostAddress().length());
                                    //broadcasting ip address
				    ClientSocket.DeliverMessage(OutgoingMessage,BroadcastAddress);
                                  //     System.out.println("broadcasting address");
                                    
                                    
                                      isMessageQueued = ClientSocket.RetrieveQueuedMessage(TokenAccess.READ_INTERVAL, IncomingMessage, ServerAddress);
                                      
                                      //will trigger if used on a 192 network i didnt know how to use regex for any number and a dot
                                       if (isMessageQueued == true && (IncomingMessage.Type==TokenAccess.PDU_CLOSE)){
                                           System.out.println("pdu close");
                                           String ipaddress = IncomingMessage.Buffer;
                                           
                                          for(int n :addressIP){
                                               if (addressIP[n] ==Integer.parseInt(ipaddress)){
                                              String a;
                                                  a = Integer.toString(addressIP[n]);
                                                OutgoingMessage.BuildMessage(++DialogueNumber, TokenAccess.PDU_ACK,InetAddress.getLocalHost().getHostAddress(),InetAddress.getLocalHost().getHostAddress().length());
                                                	    ENDPOINT  exitAddress = new ENDPOINT(a, TokenAccess.SERVER_PORT_NUMBER);         

                                                ClientSocket.DeliverMessage(OutgoingMessage,exitAddress);
                                                  addressIP = ArrayUtils.removeElement( addressIP, n);
                                               }
                                              
                                               
                                          }
                                       }
                                       
                                       
                                       if(last != null && tokenlost.bRunning == false)
                                       {
                                           tokenlost.Start(TokenAccess.tokenlost);
                                           last = null;
                                           System.out.println("token lost timer started");
                                       }
                                       
                                       if(isMessageQueued == true && (IncomingMessage.Type==TokenAccess.CMD_CONNECT))
                                               {
                                                   System.out.println("token lost timer stopped");
                                                   tokenlost.Stop();
                                               }
                                       
                                       if(tokenlost.isExpired()){
                                           
                                           System.out.println("token lost");
                                           
                                            for(int n = 0; n <addressIP.length;n++){
                                             String ipaddress = InetAddress.getLocalHost().toString();
                                            int lastdot =  ipaddress.lastIndexOf(".") + 1;
                                            ipaddress =   ipaddress.substring(lastdot, ipaddress.length());
                                                System.out.println(n);
                                            if (addressIP[n] == null){
                                               
                                            }else{
                                              if (addressIP[n] ==Integer.parseInt(ipaddress)){
                                                  
                                                    String a;
                                                   a = Integer.toString(addressIP[n]);
                                                   System.out.println( "variable a " +a);
                                                OutgoingMessage.BuildMessage(++DialogueNumber, TokenAccess.PDU_ACK,InetAddress.getLocalHost().getHostAddress(),InetAddress.getLocalHost().getHostAddress().length());
                                                	    ENDPOINT  exitAddress = new ENDPOINT(lanAddress, TokenAccess.SERVER_PORT_NUMBER);         

                                                ClientSocket.DeliverMessage(OutgoingMessage,exitAddress);
        
                                                     OutgoingMessage.BuildMessage(++DialogueNumber, TokenAccess.PDU_TOKEN,InetAddress.getLocalHost().getHostAddress(),InetAddress.getLocalHost().getHostAddress().length());
                                                    String b = Integer.toString(addressIP[n]);
                                                    b =lanAddress;
                                                    System.out.println(b);
                                                   
                                                 
                                                        ENDPOINT  exitAddresss = new ENDPOINT(b, TokenAccess.SERVER_PORT_NUMBER);   
                                                    ClientSocket.DeliverMessage(OutgoingMessage,exitAddresss);    
                                                    
                                                    
                                               }
                                       }}
                                            
                                           
                                      }
                                       
                                       if(exit.isExpired())
                                       {
                                               OutgoingMessage.BuildMessage(++DialogueNumber, TokenAccess.PDU_CLOSE,InetAddress.getLocalHost().getHostAddress(),InetAddress.getLocalHost().getHostAddress().length());
                                               exit.Start(30);
                                       }
                                       
                                       
                                       if(isMessageQueued && (IncomingMessage.Type==TokenAccess.PDU_TOKEN)){
                                           tokenlost.Stop();
                                           System.out.println(IncomingMessage.Type);
                                           state = TokenAccess.STATES.STATE_TOKENED;
                                           tokentimer.Start(TokenAccess.tokentimer);
                                           break;
                                       }
                                    
                                   }
                                       
                                         
                              case STATE_TOKENED:
                                   while(state== TokenAccess.STATES.STATE_TOKENED){
                                      
                                      //send data
                                       OutgoingMessage.BuildMessage(0, 0, "ALIVE", CommentLength);
                                       ClientSocket.DeliverMessage(OutgoingMessage,BroadcastAddress); 
                                        tokenlost.Stop();
                                        while(trigger == 0){
                                            System.out.println("TOKENED");
                                            trigger++;
                                        }
                                        
                                        
                                        
                                      if(tokentimer.isExpired()){
                                          trigger = 0 ;
                                            System.out.println("token expired");
                                         String ipaddress = InetAddress.getLocalHost().toString();
                                        //  System.out.println( "System name : "+ipaddress);
                                        
                                            for(int n = 0; n <addressIP.length;n++){
                                               // System.out.println(n);
                                              int lastdot =  ipaddress.lastIndexOf(".") + 1;
                                            ipaddress =   ipaddress.substring(lastdot, ipaddress.length());
                                               // System.out.println("IP address: "+ipaddress);
                                              int address1 = Integer.parseInt(ipaddress);
                                               
                                           
                                                   System.out.println("made it to line 214");
                                                   System.out.println(n);
                                                   System.out.println(addressIP[0]);
                                                   
                                             if (addressIP[n] == address1){
                                                  String a;
                                                a = Integer.toString(addressIP[n]);
                                                 System.out.println("variable a = "+a);
                                                
                                                String ip = InetAddress.getLocalHost().getHostAddress();
                                                 System.out.println("line 226");
                                                 System.out.println(ip);
                                                
                                                   OutgoingMessage.BuildMessage(++DialogueNumber, TokenAccess.PDU_TOKEN,InetAddress.getLocalHost().getHostAddress(),InetAddress.getLocalHost().getHostAddress().length());
                                                 String b;
                                                 
                                                 System.out.println("line 222");
                                                   if (n+1 >= addressIP.length){
                                                       b = (ip);
                                                    //   System.out.println(b);
                                                   }else{
                                                        b = ip;
                                                  
                                                   }
                                                   
                                                  System.out.println(b);
                                                  
                                                  //b = InetAddress.getLocalHost().toString().substring(0, lastdot); 
                                              // int slash =   b.indexOf("/");
                                                 System.out.println(b);
                                                       ENDPOINT  exitAddresss = new ENDPOINT(b, TokenAccess.SERVER_PORT_NUMBER);   
                                                   ClientSocket.DeliverMessage(OutgoingMessage,exitAddresss);     
                                                    System.out.println("exiting tokened state");
                                                   state = TokenAccess.STATES.STATE_tokenLess;
                                                   last = "1";
                                               }
                                            break;
                                            } }
                                        }
                                    
                                    
                                    
                                case STATE_EXIT:
                                    while(state == TokenAccess.STATES.STATE_EXIT)
                                    {
                                            System.exit(0);
                                     
                                    }
                             
                                   
                                    
                                    
                    
                }
                
                
        }
    
}
}
