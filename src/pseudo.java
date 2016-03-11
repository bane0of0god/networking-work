
/**
 *
 * @author Roshan k1116774
 */
public class pseudo {

    timer firstnode;
    timer tokentimer;
    timer tokenlost;
    timer exit;

    state current = "intital";
    state LastState;

    
        while(true)
    {
        while (current == "intital") {
            //start of application with initial state 

            //exit action 
            new OPENSOCKET();
            firstnode.set(10000);
            firstnode.start();
            //setting next state 
            current = "Started";
            //exit if
            break;
        }

        while (current == "Started") {
            // entry action 
            int AddressIP[];

            //event timer expired
            if (firstnode.expires) {
                //create a token 
                ititalise token;
                //set token expiry timer to 1 second per spec
                tokentimer.set(1000);
                // add own address to array
                AddressIP[].add(own.ipAddress
                );
            current = "Tokened";
                break;
            }

            if (message.recived("ringExisits")) {
                // no exit action 
                // next state is tokenless 
                current = "TokenLess";
                break;
            }

        }

        // state when joined ring but doesnt have the token
        while (current == "tokenLess") {
            // entry action 
            Broadcast(own.ipAddress);

            // if message recived that has exit header
            if (message.recived(exit(IpAddress))) {

                int Ip = exit(IpAddress);

                // for loop to match addresses
                for (AddressIP[], int  n++;) {

                    if (AddressIP[n] == Ip) // dealete the ipAddress that matches
                    {
                        AddressIP[n].purge();
                    }

                    //broadcast ACK of delete
                    BroadCast(MESSAGE.exit(ACK));

                }

            }

            //if statement 
            // if the previous state was tokened
            if (LastState == "tokend" && tokenlost == null) {
                //setting timer length
                tokenlost.set(6000);
                //starting timer
                tokenlost.start();
                // adding secondary condition to ensure that the if statement doesnt run again
                LastState = null;

            }

            //event if ring ring exsits 
            // extra if included to ensure that the if statement doesnt keep running
            if (message.recived("RING EXSITS") && tokenlost > 0) {
                tokenlost.end();
            }

            //event if the tokenlost timer expires
            if (tokenlost.Expired) {
                int n = own IP location in array;

                //broadcast to remove lost node ip from array
                Broadcast(MESSAGE.exit.Graceful(AddressIP[n + 1]));
                //send token to first entry in array and begin ring again 
                send  new token to addressIp[n + 2];

            }

            // event if user interaction instructs program to exit or exit timer expires
            if (usercmd(exit.graceful()) || exit.expires()) {

                broadcast Message
                (exit(addressip[ownlocation])
                );
                Exit.set(10000);
            }

            // event if the message recievd is ack for exit comand
            if (MESSAGE.recived(exit("ACK"))) {

                //setting next state of end
                current = "end";

                //exit the while loop
                break;
            }

            // event if token is recived
            if (MESSAGE.recived("TOKEN")) {
                //setting token time length
                tokentimer.set(1000);
                // starting timer
                tokentimer.start();

                //setting next state of tokened
                current = "tokened";
                //exit the while loop
                break;
            }
        }

        //state that has the token
        while (current == "tokened") {
            if (tokentimer.expired()) {

                Message.destination(addressIP[own + 1]);
                Message("tokened");
                Message.send();
                current = "tokenLess"
                
            }

            if (DataCreated) {
                use physical resource;
            }
        }
        
        
        while (current == "end") {
            application.close();
        }

    }

}
