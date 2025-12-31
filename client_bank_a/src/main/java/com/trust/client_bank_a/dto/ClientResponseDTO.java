package com.trust.client_bank_a.dto;

public record ClientResponseDTO (	
		String trxId,
		String status,
		String message
) {}