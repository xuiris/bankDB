import java.io.Serializable;
      /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author kenkh
 */
public class Customer implements Serializable {
    private final int taxID;
    private final String name;
    private final String PIN;
    private final String address;
   
    
  
    Customer(int taxID, String name, String address, String PIN) {
        this.taxID = taxID;
        this.name = name;
        this.PIN = PIN;
        this.address = address;
       
    }
    
    
    @Override
    public String toString(){
        return  "taxID: " + gettaxID() + 
                " Address: " + getaddress() + 
                " Name: " + getname() + 
                " PIN:" + getPIN();
                
    }
      
    public int gettaxID(){
        return taxID;
    }

   
    public String getname() {
        return name;
    }
    
    public String getPIN() {
        return PIN;
    }
    
    public String getaddress(){
        return address;
    }
    
    
}
