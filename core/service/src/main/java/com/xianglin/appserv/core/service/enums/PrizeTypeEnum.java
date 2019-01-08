package com.xianglin.appserv.core.service.enums;

public enum PrizeTypeEnum{
	R1("红包","R1"),
	Y1("优惠劵","Y1"),
	H1("话费","H1"),
	S1("手机","S1")
	
	;
	
	private String name;
	private String ptype;
	
	PrizeTypeEnum(String name,String ptype){
		this.name = name;
		this.ptype = ptype;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPtype() {
		return ptype;
	}
	
	public void setPtype(String ptype) {
		this.ptype = ptype;
	}
	
	public static PrizeTypeEnum getEnumByPtype(String ptype) {
		for (PrizeTypeEnum prizeEnum : values()) {
			if (prizeEnum.ptype.equals(ptype))
				return prizeEnum;
		}
		return null;
	}
	
}
