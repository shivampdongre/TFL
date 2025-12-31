package com.trust.server.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.trust.server.dto.ServerRequestDTO;
import com.trust.server.entities.CustomerDetails;
import com.trust.server.repository.CustomerRepository;
import com.trust.server.service.TransactionProcessor;

@Service
public class TransactionProcessorImpl implements TransactionProcessor {

	private CustomerRepository customerRepository;

	public TransactionProcessorImpl(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	private static final Logger logger = LoggerFactory.getLogger(TransactionProcessorImpl.class);
	private final XmlMapper xmlMapper = new XmlMapper();
	private final Set<String> cache = ConcurrentHashMap.newKeySet();

	@Override
	public String process(String xmlRequest) {
		ServerRequestDTO serverRequestDTO = null;
		try {
			serverRequestDTO = xmlMapper.readValue(xmlRequest, ServerRequestDTO.class);
		} catch (JsonProcessingException e) {
//			logger.error("Not able to read XML request : " + e.getMessage());
			return "Invalid Request";
		}

		if (null != serverRequestDTO) {
			if (!cache.add(serverRequestDTO.trxId())) {
//				logger.info("Duplicate Transcation Id found : " + serverRequestDTO.trxId());
				return "Duplicate Transcation";
			}

			if (serverRequestDTO.amount() <= 0) {
//				logger.info("Amount is less than zero : " + serverRequestDTO.amount() + " for transaction Id : "
//						+ serverRequestDTO.trxId());
				return "Invalid Amount";
			}

			List<String> ids = List.of(serverRequestDTO.fromAccount(), serverRequestDTO.toAccount());
			List<CustomerDetails> customers = customerRepository.findAllById(ids);

			if (customers.size() != 2) {
//				logger.info("Unable to complete transaction as both the account not found");
				return "Invalid Customer";
			}

			CustomerDetails fromAcc = null;
			CustomerDetails toAcc = null;
			for (CustomerDetails cust : customers) {
				if (cust.getAccountNo().equals(serverRequestDTO.fromAccount())) {
					fromAcc = cust;
				} else {
					toAcc = cust;
				}
			}

			if (fromAcc.getBalance() - serverRequestDTO.amount() < 0) {
//				logger.info("Unable to complete transaction due insufficient balance for transaction Id : "
//						+ serverRequestDTO.trxId());
				return "Insufficient Balance";
			}

			fromAcc.setBalance(fromAcc.getBalance() - serverRequestDTO.amount());
			toAcc.setBalance(toAcc.getBalance() + serverRequestDTO.amount());

			customerRepository.saveAll(List.of(fromAcc, toAcc));
//			logger.info("Transaction completed successfully with transaction Id : " + serverRequestDTO.trxId());
			return "Completed";
		}
//		logger.error("Request is Null");
		return "Failed";
	}

	public String getId(String xmlRequest) {
		JsonNode rootNode = null;
		try {
			rootNode = xmlMapper.readTree(xmlRequest.getBytes());
		} catch (IOException e) {
			logger.error("Unable to get transaction Id : " + e.getMessage());
		}
		JsonNode trxIdNode = rootNode.path("TrxId");
		return trxIdNode.asText();
	}

}
