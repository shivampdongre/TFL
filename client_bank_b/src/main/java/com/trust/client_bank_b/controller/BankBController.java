package com.trust.client_bank_b.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trust.client_bank_b.dto.ClientResponseDTO;
import com.trust.client_bank_b.service.ServerCommunicationService;

@RestController
@RequestMapping("/clientB/bank")
public class BankBController {
	private static final Logger logger = LoggerFactory.getLogger(BankBController.class);

	private ServerCommunicationService serverCommunicationService;
	
	public BankBController(ServerCommunicationService serverCommunicationService) {
		this.serverCommunicationService = serverCommunicationService;
	}

	@PostMapping("/transaction")
	public ResponseEntity<ClientResponseDTO> doWork(@RequestBody String input) {
		logger.info("Transaction Started Working.");
		ClientResponseDTO clientResponseDTO = null;
		if (null == input || input.isBlank() || input.isEmpty()) {
			clientResponseDTO = new ClientResponseDTO("", "FAILED", "Request is NULL or Blank or Empty");
		} else {
			clientResponseDTO = serverCommunicationService.sendXmlRequest(input);
		}
		logger.info("Transaction Work Completed with Transaction ID : " + clientResponseDTO.trxId() + " and status is : " + clientResponseDTO.status());
		return ResponseEntity.ok(clientResponseDTO);
	}

}
