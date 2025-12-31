package com.trust.client_bank_b.service.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.trust.client_bank_b.dto.ClientResponseDTO;
import com.trust.client_bank_b.dto.ServerRequestDTO;
import com.trust.client_bank_b.service.ServerCommunicationService;

@Service
public class ServerCommunicationServiceImpl implements ServerCommunicationService {
	
	private static final Logger logger = LoggerFactory.getLogger(ServerCommunicationServiceImpl.class);
	private final ObjectMapper objMapper = new ObjectMapper();
	private final RestTemplate restTemplate = new RestTemplate();
	private final XmlMapper xmlMapper = new XmlMapper();
	private final AtomicLong count = new AtomicLong(0);

	@Override
	public ClientResponseDTO sendXmlRequest(String input) {
		String xmlRequest = null;
		ClientResponseDTO clientResponseDTO = null;
		try {
			xmlRequest = convertToXml(input);
		} catch (Exception e) {
			clientResponseDTO = new ClientResponseDTO("", "FAILED", "Unable to convert to XML");
			logger.error("Unable to convert to XML : " + e.getMessage());
		}

		if (null != xmlRequest) {
			sendAsyncRequest(xmlRequest);
			clientResponseDTO = new ClientResponseDTO(getId(xmlRequest), "FORWARDED",
					"Transaction forwarded to server");
			logger.info("Transaction forwared to server with transaction Id : " + clientResponseDTO.trxId());
		}
		return clientResponseDTO;
	}

	@Async
	private void sendAsyncRequest(String xmlRequest) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_XML);
		restTemplate.postForLocation("http://localhost:8083/server/transaction/process",
				new HttpEntity<>(xmlRequest, httpHeaders));
	}

	private String getId(String xmlRequest) {
		JsonNode rootNode = null;
		try {
			rootNode = xmlMapper.readTree(xmlRequest.getBytes());
		} catch (IOException e) {
			logger.error("Unable to get transaction Id : " + e.getMessage());
		}
		JsonNode trxIdNode = rootNode.path("TrxId");
		return trxIdNode.asText();
	}

	private String generateTransactionId() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
		String requiredDate = LocalDate.now().format(dtf);
		StringBuilder sb = new StringBuilder("TRX-");
		sb.append(requiredDate).append("-").append(String.format("%06d", count.incrementAndGet()));

		return sb.toString();
	}

	private String convertToXml(String input) throws JsonProcessingException {
		JsonNode json = objMapper.readTree(input);
		ServerRequestDTO serverRequestDTO = new ServerRequestDTO(
				generateTransactionId(),
				"BANK_A_ID",
				json.get("customerId").asLong(),
				json.get("fromAccount").asText(),
				json.get("toAccount").asText(),
				json.get("amount").asDouble(),
				json.get("currency").asText(),
				OffsetDateTime.now().toString()
		);
		xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
		return xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(serverRequestDTO);
	}

}
