package com.jeppesen.brightspark.lib.network;

public class RestResponse {
	private int Code;
	private String Message;
	private String Data;
	private boolean Successful;
	
	public void setSuccessful(boolean success) {
		Successful = success;
	}
	
	public boolean isSuccessful() {
		return Successful;
	}
	
	public int getCode() {
		return Code;
	}

	public void setCode(int code) {
		Code = code;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public String getData() {
		return Data;
	}

	public void setData(String data) {
		Data = data;
	}
		
	public RestResponse()
	{
	
	}
}
