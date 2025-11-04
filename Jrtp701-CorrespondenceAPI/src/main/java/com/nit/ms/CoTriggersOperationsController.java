package com.nit.ms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nit.binding.COSummary;
import com.nit.service.ICorrespondenceMgmtService;

@RestController
@RequestMapping("/co-trigger-api")
public class CoTriggersOperationsController {
	@Autowired
	private ICorrespondenceMgmtService  coService;
	
	@GetMapping("/process")
	public ResponseEntity<COSummary>  processAndUpdateTriggers()throws Exception{
		COSummary  summary = coService.proccessPendingTriggers();
		return new ResponseEntity<COSummary>(summary,HttpStatus.OK);
	}
}
