package com.xianglin.appserv.core.service.enums;

public enum PrizeEnum{
	prize1("iphone7","S1"),
	prize2("100元话费","H1"),
	prize3("30元话费","H1"),
	prize4("10元话费","H1"),
	prize5("100元优惠券","Y1"),
	prize6("30元优惠券","Y1"),
	prize7("10元优惠券","Y1"),
	prize8("现金红包","H1")
	
	;
	
	private String name;
	private String ptype;
	
	PrizeEnum(String name,String ptype){
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
	
	public static PrizeEnum getEnumByName(String name) {
		for (PrizeEnum prizeEnum : values()) {
			if (prizeEnum.name.equals(name))
				return prizeEnum;
		}
		return null;
	}
	
	public static PrizeEnum getEnumByPtype(String ptype) {
		for (PrizeEnum prizeEnum : values()) {
			if (prizeEnum.ptype.equals(ptype))
				return prizeEnum;
		}
		return null;
	}
	
}
