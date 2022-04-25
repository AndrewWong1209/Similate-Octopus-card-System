package comp2026.OctopusCard;

import comp2026.OctopusCard.Util.*;

public class Retail extends OCTransaction {
    public static final String TypeHdrStr = "Retail";
    private final String retailer;
    private final String description;

    public Retail(String dateTimeStr, String transactionID, String amountStr, String retailer, String description) throws OCTransactionFormatException{
        super("Retail", dateTimeStr, transactionID, amountStr);
        this.retailer = retailer;
        this.description = description;
        setStatus(Status.COMPLETED);
    }

    // using Tokenizer.getToken to break the String record to argument String[], then according to the content of array to build a new
    // Retail object and return it.
    public static OCTransaction parseTransaction(String record) throws OCTransactionFormatException {
        String[] tokens = Tokenizer.getTokens(record);
        String date = tokens[1];
        String id = tokens[2];
        String amount = tokens[3];
        String type = "Retail";
        String route = tokens[4];

        String line = "";
        for(int i=5; i<tokens.length; i++){
            line += tokens[i];
            if(i!= tokens.length-1){
                line += " ";
            }                                    // e.g "Paper & Coffee, Cappuccino"
        }

        String retailer = "";
        int comerPosition = 0;
        for(int i=0; i<line.length(); i++){
            char c = line.charAt(i);
            if(c==','){
                comerPosition = i;
                break;
            }
            retailer += c;
        }

        String description = "";
        for(int i=comerPosition+2; i<line.length(); i++){
            char c = line.charAt(i);
            description += c;
        }

        Retail newRetailTrans = new Retail(date, id, amount, retailer, description);
        return newRetailTrans;
    }

    // return a String line which show the variables of Retail object
    public String toRecord(){
        return this.getType() +" "+this.getDateStr()+" "+this.getTransactionID()+" "+this.getAmount()+" "+this.retailer+' '+this.description;
    }

    // take the String array criteria which expect to be a part of the argument from user.
    // then return boolean to see if this object 's variable match the criteria of the user want
    public boolean match(String[] criteria){
        if(criteria[0].equals("retailer")){
            return this.retailer.contains(OCTransaction.stringCombiner(criteria, 1));
        } else if(criteria[0].equals("description")){
            return this.description.contains(OCTransaction.stringCombiner(criteria, 1));
        }  else if(criteria[0].equals("date")){
            return this.dateStr().equals(criteria[1]);
        } else {
            return false;
        }
    }

    public String toString(){
        return
                "[Retail]\n"+
                        "  Retailer: "+this.retailer+"\n"+
                        "  Description: "+this.description+"\n"+
                        "  TransactionID: "+this.getTransactionID()+"\n"+
                        "  Date/Time: "+this.getDateStr()+" at "+this.getTimeStr()+"\n"+
                        "  Amount: "+this.getAmount()+"\n"+
                        "  Status: "+this.getStatusStr();
    }
}
