package com.trust.client_bank_a.service;

import com.trust.client_bank_a.dto.ClientResponseDTO;

public interface ServerCommunicationService {

	ClientResponseDTO sendXmlRequest(String xmlRequest);
	
}
