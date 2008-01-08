package de.haw.smartshelf.db.data.pers;
// Generated 02.11.2007 15:27:42 by Hibernate Tools 3.2.0.b11



/**
 * ArticleExtensionId generated by hbm2java
 */
public class ArticleExtensionId  implements java.io.Serializable {


     private String rfid;
     private String name;
     private String value;

    public ArticleExtensionId() {
    }

	
    public ArticleExtensionId(String rfid) {
        this.rfid = rfid;
    }
    public ArticleExtensionId(String rfid, String name, String value) {
       this.rfid = rfid;
       this.name = name;
       this.value = value;
    }
   
    public String getRfid() {
        return this.rfid;
    }
    
    public void setRfid(String rfid) {
        this.rfid = rfid;
    }
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public String getValue() {
        return this.value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof ArticleExtensionId) ) return false;
		 ArticleExtensionId castOther = ( ArticleExtensionId ) other; 
         
		 return ( (this.getRfid()==castOther.getRfid()) || ( this.getRfid()!=null && castOther.getRfid()!=null && this.getRfid().equals(castOther.getRfid()) ) )
 && ( (this.getName()==castOther.getName()) || ( this.getName()!=null && castOther.getName()!=null && this.getName().equals(castOther.getName()) ) )
 && ( (this.getValue()==castOther.getValue()) || ( this.getValue()!=null && castOther.getValue()!=null && this.getValue().equals(castOther.getValue()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + ( getRfid() == null ? 0 : this.getRfid().hashCode() );
         result = 37 * result + ( getName() == null ? 0 : this.getName().hashCode() );
         result = 37 * result + ( getValue() == null ? 0 : this.getValue().hashCode() );
         return result;
   }   


}

