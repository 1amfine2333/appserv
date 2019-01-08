
package com.xianglin.appserv;

import com.xianglin.gateway.common.service.spi.model.ServiceResponse;

/**
 * 网关响应结果
 * 
 * <pre>
 *  <b>不可轻易改变此类中的属性名称，因为客户端是根据此类中的属性名称进行json反序列的。</b>
 * </pre>
 * 
 * @author pengpeng 2016年1月27日下午7:55:53
 */
public class SingleResponseBody {

	/** 请求id */
	private int id;

	/** 结果Object */
	private Object result;

	/** 操作结果，1000为成功，其他为失败 */
	private int resultStatus = 1001;

	/** 备注 */
	private String memo;

	/** 提示文案 */
	private String tips;

	public static SingleResponseBody valueOf(ServiceResponse<?> serviceResponse) {
		SingleResponseBody result = new SingleResponseBody();
		result.setTips(serviceResponse.getTips());
		result.setMemo(serviceResponse.getMemo());
		result.setResultStatus(serviceResponse.getCode());
		result.setResult(serviceResponse.getResult());
		return result;
	}

	/**
	 * Getter method for property <tt>result</tt>.
	 * 
	 * @return property value of result
	 */
	public Object getResult() {
		return result;
	}

	/**
	 * Setter method for property <tt>result</tt>.
	 * 
	 * @param result
	 *            value to be assigned to property result
	 */
	public void setResult(Object result) {
		this.result = result;
	}

	/**
	 * Getter method for property <tt>resultStatus</tt>.
	 * 
	 * @return property value of resultStatus
	 */
	public int getResultStatus() {
		return resultStatus;
	}

	/**
	 * Setter method for property <tt>resultStatus</tt>.
	 * 
	 * @param resultStatus
	 *            value to be assigned to property resultStatus
	 */
	public void setResultStatus(int resultStatus) {
		this.resultStatus = resultStatus;
	}

	/**
	 * Getter method for property <tt>memo</tt>.
	 * 
	 * @return property value of memo
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * Setter method for property <tt>memo</tt>.
	 * 
	 * @param memo
	 *            value to be assigned to property memo
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * Getter method for property <tt>tips</tt>.
	 * 
	 * @return property value of tips
	 */
	public String getTips() {
		return tips;
	}

	/**
	 * Setter method for property <tt>tips</tt>.
	 * 
	 * @param tips
	 *            value to be assigned to property tips
	 */
	public void setTips(String tips) {
		this.tips = tips;
	}

	/**
	 * Getter method for property <tt>id</tt>.
	 * 
	 * @return property value of id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Setter method for property <tt>id</tt>.
	 * 
	 * @param id
	 *            value to be assigned to property id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[id=" + id + ", resultStatus=" + resultStatus + ", memo=" + memo + ", tips=" + tips + "]";
	}

}
