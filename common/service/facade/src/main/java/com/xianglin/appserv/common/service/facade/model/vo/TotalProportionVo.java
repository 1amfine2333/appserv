package com.xianglin.appserv.common.service.facade.model.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanglei on 2017/7/4.
 */
public class TotalProportionVo extends BaseVo implements Comparable<TotalProportionVo>{
	
	private long partyId;//用户ID
	private String Category;//类目
	private String Percentage;//所占比例总额的百分比
	private String CategoryTotal;//类目总额
	private String total;//所有收入或支出总额
	private String imageColor;//图片颜色
	private String lABEL;//标签
	private List<TotalProportionVo> totalProportionVo = new ArrayList<TotalProportionVo>();//其他标签集合
	
	public long getPartyId() {
		return partyId;
	}
	public void setPartyId(long partyId) {
		this.partyId = partyId;
	}
	public String getCategory() {
		return Category;
	}
	public void setCategory(String category) {
		Category = category;
	}
	public String getPercentage() {
		return Percentage;
	}
	public void setPercentage(String percentage) {
		Percentage = percentage;
	}
	public String getCategoryTotal() {
		return CategoryTotal;
	}
	public void setCategoryTotal(String categoryTotal) {
		CategoryTotal = categoryTotal;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getImageColor() {
		return imageColor;
	}
	public void setImageColor(String imageColor) {
		this.imageColor = imageColor;
	}
	public String getlABEL() {
		return lABEL;
	}
	public void setlABEL(String lABEL) {
		this.lABEL = lABEL;
	}
	
	public List<TotalProportionVo> getTotalProportionVo() {
		return totalProportionVo;
	}
	public void setTotalProportionVo(List<TotalProportionVo> totalProportionVo) {
		this.totalProportionVo = totalProportionVo;
	}
	@Override
	public int compareTo(TotalProportionVo o) {
		return this.getCategoryTotal().compareTo(o.getCategoryTotal());
	}
	
	
	
}
