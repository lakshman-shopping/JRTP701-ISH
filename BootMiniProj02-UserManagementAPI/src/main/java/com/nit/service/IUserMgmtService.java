package com.nit.service;

import java.util.List;

import com.nit.model.ActivateUser;
import com.nit.model.LoginCredentials;
import com.nit.model.RecoverPassword;
import com.nit.model.UserAccount;

public interface IUserMgmtService {

	public  String   registerUser(UserAccount user)throws Exception;
	public  String   ActivateUserAccount(ActivateUser user);
	public  String   login(LoginCredentials credentials);
	public  List<UserAccount>  listUsers();
	public  UserAccount   showUserByUserId(Integer id);
	public  UserAccount   showUserByEmailAndName(String email, String name);
	public  String   updateUser(UserAccount user);
	public  String   deleteUserById(Integer id);
	public  String   changeUserStatus(Integer id, String status);
	public  String   recoverPassword(RecoverPassword recover)throws Exception;
}
