package td.com.xiaoheixiong.beans.Mechants;

import java.io.Serializable;

public class EntAddress implements Serializable {
	//private String entorgcode;//企业id 组织机构代码
	 private String province = "";
	 private String city = "";
	 private String details;
	 
	 
	 public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	/*public String getEntorgcode() {
		return entorgcode;
	}
	public void setEntorgcode(String entorgcode) {
		this.entorgcode = entorgcode;
	}*/
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	
	@Override
	public String toString() {
		return "EntAddress [province=" + province + ", city=" + city + ", details=" + details + "]";
	}
	
}