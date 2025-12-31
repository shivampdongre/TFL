package com.trust.client_bank_b.dto;

public record ClientResponseDTO (	
		String trxId,
		String status,
		String message
) {}