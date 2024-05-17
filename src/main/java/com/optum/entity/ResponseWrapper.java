package com.optum.entity;

import com.optum.dao.ReqRes;

public class ResponseWrapper<T> {
    private T data;
    private ReqRes reqRes;

    public ResponseWrapper(T data, ReqRes reqRes) {
        this.data = data;
        this.reqRes = reqRes;
    }

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public ReqRes getReqRes() {
		return reqRes;
	}

	public void setReqRes(ReqRes reqRes) {
		this.reqRes = reqRes;
	}

    
}

