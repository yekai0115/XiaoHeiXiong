package td.com.xiaoheixiong.beans.Mechants;

import java.io.Serializable;

public class QrmerInf implements Serializable{
	
	private String email;
    private String unittype;
    private String mercid;
    
    public String getMercid() {
		return mercid;
	}
	public void setMercid(String mercid) {
		this.mercid = mercid;
	}

    
    public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUnittype() {
		return unittype;
	}
	public void setUnittype(String unittype) {
		this.unittype = unittype;
	}
	
	
	@Override
	public String toString() {
		return "QrmerInf [email=" + email + ", unittype=" + unittype + ", mercid=" + mercid + "]";
	}
	
}