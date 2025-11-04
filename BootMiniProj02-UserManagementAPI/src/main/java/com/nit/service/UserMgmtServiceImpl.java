package com.nit.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.nit.Utils.EmailUtils;

import com.nit.entity.UserDetails;
import com.nit.model.ActivateUser;
import com.nit.model.LoginCredentials;
import com.nit.model.RecoverPassword;
import com.nit.model.UserAccount;
import com.nit.repository.IUserDetailsRepository;

@Service
public class UserMgmtServiceImpl implements IUserMgmtService {
	@Autowired
     private  IUserDetailsRepository   userDetailRepo;
	@Autowired
	private  EmailUtils  emailUtil;
	@Autowired
	private  Environment env;
	
	
	@Override
	public String registerUser(UserAccount user)throws Exception {
		//convert userAccount data to userDetail data
		UserDetails details = new  UserDetails();
		BeanUtils.copyProperties(user, details);
		//set random string of 6 chars as pswd
		String tempPwd = generateRandomPassword(6);
		details.setPassword(tempPwd);
		details.setActive_sw("Inactive");
		//save obj
		UserDetails  savedDetails = userDetailRepo.save(details);
		//perform send mail operation
		String subject = "User Registration success";
		String body = readEmailMessageBody(env.getProperty("mailbody.registeruser.location"),user.getName(), tempPwd);
		emailUtil.sendEmailMessage(user.getEmail(), subject, body);
		return  savedDetails!=null?"User is registered with id value"+savedDetails.getUserId():"Problem is user registration";
	}
	
  

	@Override
	public String ActivateUserAccount(ActivateUser user) {
		//use findBy method
				UserDetails  entity =userDetailRepo.findByEmailAndPassword(user.getEmail(), user.getTempPassword());
				if(entity==null)
					return "user is not found for activation";
				else {
					//set the pwd
					entity.setPassword(user.getConfirmPassword());
					//change the user account status to active
					entity.setActive_sw("active");
					//update the obj
					UserDetails  updatedEntity = userDetailRepo.save(entity);
					return "User is activated with new password";
				}
}

	@Override
	public String login(LoginCredentials credentials) {
		 //convert login credentials obj to user details obj
		UserDetails  details = new UserDetails();
		BeanUtils.copyProperties(credentials, details);
		Example<UserDetails>  example = Example.of(details);
		List<UserDetails>  listEntities = userDetailRepo.findAll(example);
		if(listEntities.size()==0) {
			return "Invalid credentials";
		}
		else {
			//get entity obj
			UserDetails entity = listEntities.get(0);
			if(entity.getActive_sw().equalsIgnoreCase("Active")) {
				return "valid credentials and login successful";
			}
			else {
				return "User Details is not active";
			}
		}
		
	}

	@Override
	public List<UserAccount> listUsers() {
		// load all entities and convert to user entity obj
		List<UserAccount>  listUsers = userDetailRepo.findAll().stream().map(entity -> {
			                                UserAccount  user = new  UserAccount();
			                                BeanUtils.copyProperties(entity,user);
			                                return user;
		                                  }).toList();
		return listUsers;
	}

	@Override
	public UserAccount showUserByUserId(Integer id) {
		// load the user by user id
		Optional<UserDetails> opt = userDetailRepo.findById(id);
		UserAccount  account=null;
		if(opt.isPresent())
			account = new UserAccount();
		    BeanUtils.copyProperties(opt.get(),account);
	    return account;
	}
	

	@Override
	public UserAccount showUserByEmailAndName(String email, String name) {
		// use the custom findBy() method
		UserDetails details = userDetailRepo.findByNameAndEmail(name, email);
		UserAccount account = null;
		if(details!=null)
			account = new  UserAccount();
		    BeanUtils.copyProperties(details, account);
		return account;
	}

	@Override
	public String updateUser(UserAccount user) {
		// use the custom findBy() method
		Optional<UserDetails>  opt= userDetailRepo.findById(user.getUserId());
		    if(opt.isPresent()) {
		    	//get Entity obj
		    	UserDetails  detail = opt.get();
		    	BeanUtils.copyProperties(user, detail);
		    	userDetailRepo.save(detail);
		    	return "User details are updated";
		    }
		    else {
		    	return "user not found for updation";
		    }
		
	}

	@Override
	public String deleteUserById(Integer id) {
		// load the obj
		Optional<UserDetails>  opt = userDetailRepo.findById(id);
		if(opt.isPresent()) {
			userDetailRepo.deleteById(id);
		    return "User is deleted";
		}
		return "user is not found for deletion";
	}

	@Override
	public String changeUserStatus(Integer id, String status) {
		// load the obj
		Optional<UserDetails>  opt = userDetailRepo.findById(id);
		if(opt.isPresent()) {
			UserDetails detail = opt.get();
			detail.setActive_sw(status);
			userDetailRepo.save(detail);
			return "user status changed";
		}
			
		return "user not found for change status";
	}

	@Override
	public String recoverPassword(RecoverPassword recover)throws Exception {
		// get userDetails by name, email
		UserDetails detail = userDetailRepo.findByNameAndEmail(recover.getName(), recover.getEmail());
		if(detail!=null) {
			 String  pwd= detail.getPassword();
			 //send the recovered  pwd to the email
			 String subject = "mail for password recovery";
			 String mailBody = readEmailMessageBody(env.getProperty("mailbody.recoverpwd.location"),recover.getName(),pwd);
			 emailUtil.sendEmailMessage(recover.getEmail(), subject, mailBody);
			 return pwd;
		}
		return "User and email is not found";
	}
    
	 private   String  generateRandomPassword(int length) {
		   //list of charts to choose from in form of string
		   String  AlphaNumericStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		   //creating stringBuffer size of AlphaNumericStr
		   StringBuilder  randomWord = new StringBuilder(length);
		   int i;
		   for(i=0; i<length; i++) {
			   //generating random number using math.random()
			   int ch = (int)(AlphaNumericStr.length()*Math.random());
			   //adding random charts at the end of the randomword
			   randomWord.append(AlphaNumericStr.charAt(ch));
		   }
		   return  randomWord.toString();
	   }
     private String  readEmailMessageBody(String fileName, String fullName, String pwd)throws Exception{
    	 String mailBody = null;
    	 String url ="http://localhost:4041/activate ";
    	 try (FileReader  reader = new FileReader(fileName);
    			           BufferedReader  br = new BufferedReader(reader)){
    		 //read file content to stringBuffer line by line
    		 StringBuffer  buffer = new StringBuffer();
    		 String line=null;
    		 do {
    			 line =br.readLine();
    			 if(line!=null)
    			    buffer.append(line);
    		 }while(line!=null);
    		 
    		 mailBody=buffer.toString();
    		 mailBody=mailBody.replace("{FULL-NAME}", fullName);
    		 mailBody=mailBody.replace("{PWD}", pwd);
    		 mailBody=mailBody.replace("{URL}", url);
    	 }
    	 catch(Exception e) {
    		 e.printStackTrace();
    		 throw e;
    	 }
    	 return mailBody;
     } 
}
