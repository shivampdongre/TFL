package com.trust.server.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "TransactionRequest")
public record ServerRequestDTO(
		@JacksonXmlProperty(localName = "TrxId") String trxId,
		@JacksonXmlProperty(localName = "BankId") String bankId,
		@JacksonXmlProperty(localName = "CustomerId") Long customerId,
		@JacksonXmlProperty(localName = "FromAccount") String fromAccount,
		@JacksonXmlProperty(localName = "ToAccount") String toAccount,
		@JacksonXmlProperty(localName = "Amount") Double amount,
		@JacksonXmlProperty(localName = "Currency") String currency,
		@JacksonXmlProperty(localName = "Timestamp") String timestamp
		) {}