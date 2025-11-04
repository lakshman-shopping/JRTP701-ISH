package com.nit.ms;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nit.bindings.PlanData;
import com.nit.service.IPlanMgmtService;

//import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("Admin/api")  //global path is optional
public class PlanOperationsController {
	@Autowired
     private IPlanMgmtService planService;
     
	@GetMapping("/categories")
	public ResponseEntity<?> showTravelPlanCategories(){
		//invoke service class methods
		try {
			Map<Integer, String> mapCategories = planService.getPlanCategories();
			return new ResponseEntity<Map<Integer, String>>(mapCategories, HttpStatus.OK);
		}
		catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/register")
	public ResponseEntity<String> saveTravelPlan(@RequestBody PlanData plan){
		//use service
		try {
			System.out.println("plan details :: " +plan.toString());
			String msg = planService.registerPlan(plan);
			return new ResponseEntity<String>(msg, HttpStatus.CREATED);
		}
		catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	@GetMapping("/all")
	public ResponseEntity<?> getAllTravelPlans(){
		//use service
	    try {
			List<PlanData> list = planService.showAllPlans();
			return new ResponseEntity<List<PlanData>>(list, HttpStatus.OK);
		}
		catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/find/{planId}")
	public ResponseEntity<?> getTravelPlanById(@PathVariable Integer planId){
		//use service
		try {
			 PlanData plan = planService.showPlanById(planId);
			 return new ResponseEntity<PlanData>(plan, HttpStatus.OK);
		}
		catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
	@PutMapping("/update")
	public ResponseEntity<?> updateTravelPlan(@RequestBody PlanData plan){
		//use service
		try {
			String msg = planService.updatePlan(plan);
			return new ResponseEntity<String>(msg, HttpStatus.OK);
		}
		catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/delete/{planId}")
	public ResponseEntity<?> removeTravelPlanByPlanId(@PathVariable Integer planId){
		//use service
		try {
			String msg = planService.deletePlan(planId);
			return new ResponseEntity<String>(msg, HttpStatus.OK);
		}
		catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/status-chang/{planId}/{status}")
	public ResponseEntity<?>  removeTravelPlanByPlanId(@PathVariable Integer planId,
			                                                                                                   @PathVariable String status){
	//use service
	    try {
		     String msg = planService.changePlanStatus(planId, status);
		     return new ResponseEntity<String>(msg, HttpStatus.OK);
	    }
	    catch(Exception e) {
		     e.printStackTrace();
		     return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
}//class
