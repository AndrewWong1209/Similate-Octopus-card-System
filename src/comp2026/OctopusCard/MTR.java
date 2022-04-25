package comp2026.OctopusCard;

import comp2026.OctopusCard.Util.*;

public class MTR extends OCTransaction{
    public static final String TypeHdrStr = "MTR";
    private String mtrType;
    private String station;
    private MTR reference;              //A reference MTR (checkIn or checkOut) connect with this MTR object

    public MTR (String dateTimeStr, String transactionID, String amountStr, String mtrType, String station) throws OCTransactionFormatException{
        super("MTR", dateTimeStr, transactionID, amountStr);
        this.mtrType = mtrType;
        this.station = station;
        if(mtrType.equals("CheckIn")){
            this.setStatus(Status.MTR_OUTSTANDING);
        } else {
            this.setStatus(Status.MTR_COMPLETED);
        }
    }

    // using Tokenizer.getToken to break the String record to argument String[], then according to the content of array to build a new
    // MTR object and return it.
    public static OCTransaction parseTransaction(String record) throws OCTransactionFormatException {
        String [] tokens = Tokenizer.getTokens(record);
        String date = tokens[1];
        String id = tokens[2];
        String amount = tokens[3];
        String mtrType = tokens[4];
        String type = "MTR";

        String stationStr = "";
        for(int i=5; i<tokens.length; i++){
            stationStr += tokens[i];
            if(i!= tokens.length-1){
                stationStr += " ";
            }
        }

        MTR newMtrTrans = new MTR(date, id, amount, mtrType, stationStr);
        return newMtrTrans;
    }

    // return a String line which show the variables of MTR object
    public String toRecord(){
        return this.getType() +" "+this.getDateStr()+" "+this.getTransactionID()+" "+this.getAmount()+" "+this.mtrType+" "+this.station;
    }

    // take the String array criteria which expect to be a part of the argument from user.
    // then return boolean to see if this object 's variable match the criteria of the user want
    public boolean match(String[] criteria){
        if(criteria[0].equals("station")){
            // String indexOf, contains (partial search); toLower
            return this.station.contains(OCTransaction.stringCombiner(criteria, 1));
        } else if(criteria[0].equals("mtrType")){
            return this.mtrType.equalsIgnoreCase(criteria[1]);
        } else if(criteria[0].equals("status")){
            return this.getStatusStr().equalsIgnoreCase(criteria[1]);
        } else if(criteria[0].equals("date")){
            return this.dateStr().equals(criteria[1]);
        } else {
            return false;
        }
    }

    public String toString(){
        String str="";

                str+=         "[MTR Transaction]\n";
                str+=         "  MTR Type: "+this.mtrType+"\n";
                str+=         "  Station: "+this.station+"\n";
                str+=         "  TransactionID: "+this.getTransactionID()+"\n";
                str+=         "  Date/Time: "+this.getDateStr()+" at "+this.getTimeStr()+"\n";
                str+=         "  Amount: "+this.getAmount()+"\n";
                str+=         "  Status: "+this.getStatusStr()+"\n";
                if(this.reference!=null){
                    str+="    Matched transaction: \n";
                    str+="    ID: "+reference.getTransactionID()+"\n";
                    str+="    Date: "+reference.getDateStr()+"\n";
                    str+="    Amount: "+reference.getAmount()+"\n";

                }
                return str;
    }

    public String getMtrType(){
        return this.mtrType;
    }

    public void setReference(MTR reference){
        this.reference = reference;
    }
}
