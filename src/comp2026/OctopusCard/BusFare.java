package comp2026.OctopusCard;

import comp2026.OctopusCard.Util.*;

public class BusFare extends OCTransaction{
    public static final String TypeHdrStr = "BusFare";
    private final String route;
    private final String station;
    private final String terminal;

    public BusFare(String dateTimeStr, String transactionID, String amountStr, String route, String station, String terminal) throws OCTransactionFormatException{
        super("BusFare", dateTimeStr, transactionID, amountStr);
        this.route = route;
        this.station = station;
        this.terminal = terminal;
        setStatus(Status.COMPLETED);
    }

    // using Tokenizer.getToken to break the String record to argument String[], then according to the content of array to build a new
    // BusFare object and return it.
    public static OCTransaction parseTransaction(String record) throws OCTransactionFormatException {
        String [] tokens = Tokenizer.getTokens(record);
        String date = tokens[1];
        String id = tokens[2];
        String amount = tokens[3];
        String route = tokens[4];

        int to_Position = 0;
        for(int i=0; i< tokens.length; i++){
            if(tokens[i].equals("to")){
                to_Position = i;
            }
        }

        String station = "";
        for(int i=5; i<to_Position; i++){
            station += tokens[i];
            if(i!=to_Position-1){
                station+=" ";
            }
        }

        String terminal = "";
        for(int i=to_Position+1; i<tokens.length; i++){
            terminal += tokens[i];
            if(i!=tokens.length-1){
                terminal+=" ";
            }
        }

        BusFare newBusTrans = new BusFare(date, id, amount, route, station, terminal);
        return newBusTrans;
    }

    // return a String line which show the variables of BusFare object
    public String toRecord(){
        return this.getType() +" "+this.getDateStr()+" "+this.getTransactionID()+" "+this.getAmount()+" "+this.route+" "+this.station+" "+this.terminal;
    }

    // take the String array criteria which expect to be a part of the argument from user.
    // then return boolean to see if this object 's variable match the criteria of the user want
    public boolean match(String[] criteria){
        if(criteria[0].equals("route")){
            return this.route.equals(criteria[1]);
        } else if(criteria[0].equals("station")){
            return this.station.contains(OCTransaction.stringCombiner(criteria, 1));
        } else if(criteria[0].equals("terminal")){
            return this.terminal.contains(OCTransaction.stringCombiner(criteria, 1));
        } else if(criteria[0].equals("date")){
            return this.dateStr().equals(criteria[1]);
        } else {
            return false;
        }
    }

    public String toString(){
        return
                "[Bus Fare]\n"+
                "  Route: "+this.route+"\n"+
                "  Terminal: "+this.terminal+"\n"+
                "  TransactionID: "+this.getTransactionID()+"\n"+
                "  Date/Time: "+this.getDateStr()+" at "+this.getTimeStr()+"\n"+
                "  Amount: "+this.getAmount()+"\n"+
                "  Status: "+this.getStatusStr();
    }
}
