package com.trust.server.controller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trust.server.dto.ServerResponseDTO;
import com.trust.server.service.TransactionProcessor;

@RestController
@RequestMapping("/server/transaction")
public class ServerController {
//	private static final Logger logger = LoggerFactory.getLogger(ServerController.class);

	private ExecutorService executorService;
	private TransactionProcessor transactionProcessor;

	public ServerController(ExecutorService executorService, TransactionProcessor transactionProcessor) {
		this.executorService = executorService;
		this.transactionProcessor = transactionProcessor;
	}

	@PostMapping("/process")
	public CompletableFuture<ResponseEntity<ServerResponseDTO>> process(@RequestBody String xml) throws Exception {
//		logger.info("Process started on server for request : " + xml);
		long start = System.currentTimeMillis();
		
		return CompletableFuture.supplyAsync(() -> {
			String result = transactionProcessor.process(xml);
//			logger.info("Result : " + result);
			return ResponseEntity.ok(new ServerResponseDTO(transactionProcessor.getId(xml),
				result.equals("Completed") ? "SUCCESS" : "FAILED", result, System.currentTimeMillis() - start));
		}, executorService);

	}

}
