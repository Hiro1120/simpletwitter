package chapter6.beans;

import java.io.Serializable;

public class SelectTest implements Serializable {
	private static final long serialVersionUID = 1L;

	//beans部分
	private String account;
	private int user_id;
	private int id;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}