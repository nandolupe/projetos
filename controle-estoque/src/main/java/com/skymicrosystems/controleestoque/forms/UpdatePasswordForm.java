package com.skymicrosystems.controleestoque.forms;

public class UpdatePasswordForm {
	
	private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;
	
	public UpdatePasswordForm() {}

	public UpdatePasswordForm(String currentPassword, String newPassword, String confirmNewPassword) {
		super();
		this.currentPassword = currentPassword;
		this.newPassword = newPassword;
		this.confirmNewPassword = confirmNewPassword;
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmNewPassword() {
		return confirmNewPassword;
	}

	public void setConfirmNewPassword(String confirmNewPassword) {
		this.confirmNewPassword = confirmNewPassword;
	}
	
}
