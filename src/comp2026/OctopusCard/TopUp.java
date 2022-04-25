package comp2026.OctopusCard;

import comp2026.OctopusCard.Util.*;

public class TopUp extends OCTransaction {
    public static final String TypeHdrStr = "TopUp";
    private final String topUpType;
    private final String agent;

    public TopUp(String dateTimeStr, String transactionID, String amountStr, String topUpType, String agent) throws OCTransactionFormatException {
        super("TopUp", dateTimeStr, transactionID, amountStr);
        this.topUpType = topUpType;
        this.agent = agent;
        setStatus(Status.COMPLETED);
    }

    // using Tokenizer.getToken to break the String record to argument String[], then according to the content of array to build a new
    // TopUp object and return it.
    public static OCTransaction parseTransaction(String record) throws OCTransactionFormatException {
        String [] tokens = Tokenizer.getTokens(record);
        String date = tokens[1];
        String id = tokens[2];
        String amount = tokens[3];
        String topUpType = tokens[4];
        String type = "TopUp";

        String agent = "";
        for(int i=5; i<tokens.length; i++){
            agent += tokens[i];
            if(i!= tokens.length-1){
                agent += " ";
            }
        }

        TopUp newTopUpTrans = new TopUp(date, id, amount, topUpType, agent);
        return newTopUpTrans;
    }

    // return a String line which show the variables of TopUp object
    public String toRecord(){
        return this.getType() +" "+this.getDateStr()+" "+this.getTransactionID()+" "+this.getAmount()+" "+this.topUpType+" "+this.agent;
    }

    // take the String array criteria which expect to be a part of the argument from user.
    // then return boolean to see if this object 's variable match the criteria of the user want
    public boolean match(String[] criteria){
        if(criteria[0].equals("Cash")){
            return this.topUpType.equals("Cash");
        } else if(criteria[0].equals("Bank") && criteria.length==2){
            return this.topUpType.equals("Bank") && this.agent.contains(OCTransaction.stringCombiner(criteria, 1));
        } else if(criteria[0].equals("Bank")){
            return this.topUpType.equals("Bank");
        } else if(criteria[0].equals("date")){
            return this.dateStr().equals(criteria[1]);
        } else {
            return false;
        }
    }

    public String toString(){
        return
                "[Bus Fare]\n"+
                        "  Agent: "+this.agent+"\n"+
                        "  Type: "+this.topUpType+"\n"+
                        "  TransactionID: "+this.getTransactionID()+"\n"+
                        "  Date/Time: "+this.getDateStr()+" at "+this.getTimeStr()+"\n"+
                        "  Amount: "+this.getAmount()+"\n"+
                        "  Status: "+this.getStatusStr();
    }
}
