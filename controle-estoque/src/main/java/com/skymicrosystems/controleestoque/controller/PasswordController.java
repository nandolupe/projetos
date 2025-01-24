package com.skymicrosystems.controleestoque.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.skymicrosystems.controleestoque.forms.UpdatePasswordForm;
import com.skymicrosystems.controleestoque.services.UsuarioServiceImpl;

@Controller
public class PasswordController {

    @Autowired
    private UsuarioServiceImpl usuarioServiceImpl;
 
    // Handles GET requests to "/update-password" to render the password update form.
    @GetMapping("/update-password")
    public String showFormUpdate(Model model, RedirectAttributes redirAttrs) {
    	
    	model.addAttribute("principalObject", new UpdatePasswordForm());
    	
        return "update-password";
    }

    @PostMapping("/usuario/password/update")
    public String updatePassword(Model model, UpdatePasswordForm updatePasswordForm, RedirectAttributes redirAttrs) {
        try {
            usuarioServiceImpl.updatePassword(updatePasswordForm.getCurrentPassword(), updatePasswordForm.getConfirmNewPassword());
            redirAttrs.addFlashAttribute("success", "message.usuario.password.update.success");
        } catch (Exception e) {
        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
        }
        return "redirect:/";
    }
}
