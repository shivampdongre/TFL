package com.trust.client_bank_b.service;

import com.trust.client_bank_b.dto.ClientResponseDTO;

public interface ServerCommunicationService {

	ClientResponseDTO sendXmlRequest(String xmlRequest);
	
}
