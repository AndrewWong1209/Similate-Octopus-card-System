package comp2026.OctopusCard;

import comp2026.OctopusCard.Util.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Date;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class OCTransactionDB {
    List<OCTransaction> transactionList;
    //============================================================
    // constructor
    public OCTransactionDB() {
        transactionList = new ArrayList<>();
    }


    //============================================================
    // loadDB
    public void loadDB(String fName) throws OCTransactionDBException, ParseException{
        int cnt = 0;
        int lineNo = 0;

        try {
            System.out.println("Loading transaction db from " + fName + "...");
            Scanner in = new Scanner(new File(fName));

            // fixme: the number of records read, and the line number of corrupted records are not reported corrected!
            while (in.hasNextLine()) {
                String line = in.nextLine();
                lineNo++;
                try {
                    if (addTransaction(line) == null) {
                        System.out.println("OCTransactionDB.loadDB: no loading record from line " + lineNo + " of " + fName);
                    }
                    cnt++;
                } catch (OCTransactionDBException | OCTransaction.OCTransactionFormatException e) {
                    System.out.println("OCTransactionDB.loadDB: error loading record from line " + lineNo + " of " + fName + " -- " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            throw new OCTransactionDBException("loadDB failed: File not found (" + fName + ")!");
        }
        System.out.println(cnt + " Octopus card transactions loaded.");
    }


    //============================================================
    // saveDB
    public void saveDB(String fName) throws FileNotFoundException {
        int cnt = 0;

        // fixme: how to handle the file not found exception??
        try {
            PrintWriter out = new PrintWriter(fName);
            for (OCTransaction ocTransaction : transactionList) {
                out.println(ocTransaction.toRecord());
                cnt++;
            }
            out.close();
            System.out.println(cnt + " Octopus card transactions saved to " + fName + ".");
        } catch (FileNotFoundException e){
            System.out.println("FileNotFoundException error: "+fName+" is not exist");
        }
    }


    //============================================================
    // list
    public void list(String type) {
        int cnt = 0;

        // fixme: go through transactionList, and print transactions
        // with matching type (or print all if type is an empty string)
        // Note: (1) should ignore letter case for the type; and
        //       (2) should count the number of records correctly.
        if(type.equals("MTR")){
            for(OCTransaction ocTransaction: transactionList){
                if(ocTransaction.getType().equals("MTR")){
                    System.out.println(ocTransaction);
                    cnt++;
                }
            }
        } else if(type.equals("BusFare")){
            for(OCTransaction ocTransaction: transactionList){
                if(ocTransaction.getType().equals("BusFare")){
                    System.out.println(ocTransaction);
                    cnt++;
                }
            }
        } else if(type.equals("Retail")){
            for(OCTransaction ocTransaction: transactionList){
                if(ocTransaction.getType().equals("Retail")){
                    System.out.println(ocTransaction);
                    cnt++;
                }
            }
        } else if(type.equals("TopUp")){
            for(OCTransaction ocTransaction: transactionList){
                if(ocTransaction.getType().equals("TopUp")){
                    System.out.println(ocTransaction);
                    cnt++;
                }
            }
        } else if(type.equals("")){
            for(OCTransaction ocTransaction: transactionList){
                System.out.println(ocTransaction);
                cnt++;
            }
        }
        System.out.println(cnt + " record(s) found.");
    }

    public void list() {
        list("");
    }


    //============================================================
    // addTransaction
    public void addTransaction(OCTransaction newTransaction) throws OCTransactionDBException {
        // fixme: revised this so that (1) new transactions will be added to
        // transactionList in a chronological manner, with older transactions
        // listed first; and (2) duplicated transactions will not be added to
        // transactionList (duplicated transaction = the same transaction type,
        // with the same transaction date & time, and the same transaction
        // number).
        //System.out.println(transactionList.size());
        //transactionList.add(newTransaction);

        if(transactionList.size()==0){
            transactionList.add(newTransaction);
        } else {
            for(int i=0; i<transactionList.size(); i++){
                if(newTransaction.getTransactionID().equals(transactionList.get(i).getTransactionID())  &&  newTransaction.getType().equals(transactionList.get(i).getType()) &&  newTransaction.getDate()==transactionList.get(i).getDate()){
                    throw new OCTransactionDBException("OCTransactionDBException: error, duplicated transaction record: \n"+newTransaction);
                }
            }
            transactionList.add(newTransaction);
        }
    }

    public OCTransaction addTransaction(String record) throws OCTransactionDBException, OCTransaction.OCTransactionFormatException, ParseException {
        OCTransaction transaction = OCTransaction.parseTransaction(record);

        // skip blank lines
        if (transaction == null) {
            return null;
        }

        addTransaction(transaction);

        // fixme: Do this last when we try to match MTR checkout transactions
        // with their corresponding checkin transaction.
        /*
        // is this a mtr checkout?

         */
        if (transaction.getType().equalsIgnoreCase("MTR") && ((MTR) transaction).getMtrType().equals("CheckOut")) {
            mtrCheckOut((MTR) transaction);
        }

        return transaction;
    }


    //============================================================
    // mtrCheckOut
    private void mtrCheckOut(MTR chkOutTransaction) throws OCTransactionDBException {
        // fixme: from the newest to the oldest transactions, search for
        // an outstanding MTR checkin transaction.

        // if the checkin transaction is found, match them so that
        // they keep a reference pointing to each other.

        // if no outstanding checkin transaction can be found, throw
        // a OCTransactionDBException.

        boolean found = false;

        for(int i=transactionList.size()-1; i>0; i--){
            if(transactionList.get(i).getType().equals("MTR")  &&  ((MTR)transactionList.get(i)).getMtrType().equals("CheckIn")  &&  ((MTR)transactionList.get(i)).getStatus()==Status.MTR_OUTSTANDING){
                ((MTR)transactionList.get(i)).setReference(chkOutTransaction);
                ((MTR)transactionList.get(i)).setStatus(Status.MTR_COMPLETED);
                ((MTR)transactionList.get(transactionList.size()-1)).setReference(((MTR)transactionList.get(i)));
                found=true;
            }
        }

        if(!found){
            throw new OCTransactionDBException("OCTransactionDBException: no outstanding checkIn transaction found");
        }
    }

    //============================================================
    // search
    public OCTransaction [] search(String type, String[] criteria) throws OCTransaction.OCTransactionSearchException {
        OCTransaction [] searchResult = new OCTransaction[0];

        // fixme: should check and confirm that the specified type is valid.
        // If not valid, throw the OCTransactionSearchException.
        // Hint: use "typeIsValid" of OCTransaction.
        if(!OCTransaction.typeIsValid(type)){
            throw new OCTransaction.OCTransactionSearchException("OCTransactionSearchException error: Invalid type "+type);
        }

        // search through the transactions now
        for (OCTransaction transaction : transactionList) {
            if (transaction.getType().equalsIgnoreCase(type) && transaction.match(criteria)) {
                // fixme: the transaction matches the criteria.  Should put
                // the transaction into the searchResult array.
                OCTransaction[] newSearchResult = new OCTransaction[searchResult.length+1];
                for(int i=0; i<searchResult.length; i++){
                    newSearchResult[i]=searchResult[i];
                }
                newSearchResult[newSearchResult.length-1]=transaction;
                searchResult = newSearchResult;
            }
        }
        return searchResult;
    }


    //============================================================
    // searchIdx
    private int searchIdx(String type, Date date, String transactionID) {
        for (int i = 0; i < transactionList.size(); i++) {
            // fixme: does this record have the type, the date and the
            // transactionID as specified in the parameters?  If so,
            // return its index.
            if(transactionList.get(i).getType().equals(type)  &&  transactionList.get(i).getDate()==date  &&  transactionList.get(i).getTransactionID().equals(transactionID)){
                return i;
            }
        }
        return -1;
    }


    //============================================================
    // OCTransactionDBException
    public static class OCTransactionDBException extends Exception {
        public OCTransactionDBException(String ocTransactionDBExMsg) {
            super(ocTransactionDBExMsg);
        }
    }
}
