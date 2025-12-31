package com.trust.server.dto;

public record ServerResponseDTO(
		String trxId,
		String status,
		String reason,
		Long processingTimeMs
		) {

}
