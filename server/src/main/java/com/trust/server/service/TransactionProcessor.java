package com.trust.server.service;

public interface TransactionProcessor {

	String process(String xmlRequest);
	
	String getId(String xmlRequest);
	
}
