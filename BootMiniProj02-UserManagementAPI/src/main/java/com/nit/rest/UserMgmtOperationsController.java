package com.nit.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nit.model.ActivateUser;
import com.nit.model.LoginCredentials;
import com.nit.model.RecoverPassword;
import com.nit.model.UserAccount;
import com.nit.service.IUserMgmtService;

@RestController
@RequestMapping("/user-api")
public class UserMgmtOperationsController {
	@Autowired
    private  IUserMgmtService  userService;
    
    @PostMapping("/save")
    public ResponseEntity<String>  saveUser(@RequestBody UserAccount account){
    	try {
    		//use service
    		String resultMsg = userService.registerUser(account);
    		return new ResponseEntity<String>(resultMsg, HttpStatus.CREATED);
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    		return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }
    
    @PostMapping("/activate")
    public ResponseEntity<String>  activateUser(@RequestBody  ActivateUser user){
    	try {
    		//use service
    		String  resultMsg = userService.ActivateUserAccount(user);
    		return  new ResponseEntity<String>(resultMsg, HttpStatus.OK);
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    		return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }

    @PostMapping("/login")
    public ResponseEntity<String>  performLogin(@RequestBody LoginCredentials credentials){
    	try {
    		//use service
    		String resultMsg = userService.login(credentials);
    		return  new ResponseEntity<String>(resultMsg, HttpStatus.OK);
    	}
    	catch(Exception  e) {
    		e.printStackTrace();
    		return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }
    
    @GetMapping("/report")
    public  ResponseEntity<?>  showUsers(){
    	try {
    		//use service
    		List<UserAccount> list = userService.listUsers();
    		return  new ResponseEntity<List<UserAccount>>(list, HttpStatus.OK);
    	}
    	catch(Exception  e) {
    		e.printStackTrace();
    		return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }
    
    @GetMapping("/find/{id}")
    public  ResponseEntity<?> showUserById(@PathVariable Integer id){
    	try {
    		//use service
    		UserAccount account =userService.showUserByUserId(id);
    		return  new ResponseEntity<UserAccount>(account, HttpStatus.OK);
    	}
    	catch(Exception  e) {
    		e.printStackTrace();
    		return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }
    
    @GetMapping("/find/{email}/{name}")
    public  ResponseEntity<?>  showUserByEmailAndName(@PathVariable String email, @PathVariable String name){
    	try {
    		//use service
    		UserAccount  account = userService.showUserByEmailAndName(email, name);
    		return  new ResponseEntity<UserAccount>(account, HttpStatus.OK);
    	}
    	catch(Exception  e) {
    		e.printStackTrace();
    		return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }
    
    @PutMapping("/update")
    public ResponseEntity<String>  updateUserDetails(@RequestBody UserAccount account){
    	try {
    		//use service
    		String resultMsg = userService.updateUser(account);
    		return  new ResponseEntity<String>(resultMsg, HttpStatus.OK);
    	}
    	catch(Exception  e) {
    		e.printStackTrace();
    		return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?>  deleteUserById(@PathVariable Integer id){
    	try {
    		//use service
    		String resultMsg = userService.deleteUserById(id);
    		return  new ResponseEntity<String>(resultMsg, HttpStatus.OK);
    	}
    	catch(Exception  e) {
    		e.printStackTrace();
    		return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }
    
    @PatchMapping("/changeStatus/{id}/{status}")
    public ResponseEntity<?>  changeStatus(@PathVariable Integer id, @PathVariable String status){
    	try {
    		//use service
    		String resultMsg = userService.changeUserStatus(id, status);
    		return  new ResponseEntity<String>(resultMsg, HttpStatus.OK);
    	}
    	catch(Exception  e) {
    		e.printStackTrace();
    		return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }
    
    @PostMapping("/recoverPassword")
    public  ResponseEntity<String>  recoverPassword(@RequestBody RecoverPassword recover){
    	try {
    		//use service 
    		String resultMsg = userService.recoverPassword(recover);
    		return  new ResponseEntity<String>(resultMsg, HttpStatus.OK);
    	}
    	catch(Exception  e) {
    		e.printStackTrace();
    		return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }
}