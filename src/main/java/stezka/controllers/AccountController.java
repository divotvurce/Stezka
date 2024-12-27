package stezka.controllers;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import stezka.models.dto.UserDTO;
import stezka.models.exceptions.DuplicateEmailException;
import stezka.models.exceptions.PasswordsDoNotEqualException;
import stezka.models.services.UserService;

@Controller
@RequestMapping("/ucet")
public class AccountController {

    @Autowired
    private UserService userService;

    @GetMapping("/prihlaseni")
    public String renderLogin() {
        return "/pages/account/login";
    }
    @GetMapping("/registrace")
    public String renderRegister(@ModelAttribute UserDTO userDTO) {
        return "/pages/account/register";
    }
    @PostMapping("/registrace")
    public String register(
            @Valid @ModelAttribute UserDTO userDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors())
            return renderRegister(userDTO);

        try {
            userService.create(userDTO, false);
        } catch (DuplicateEmailException e) {
            result.rejectValue("email", "error", "Email je již používán.");
            return "/pages/account/register";
        } catch (PasswordsDoNotEqualException e) {
            result.rejectValue("password", "error", "Hesla se nerovnají.");
            result.rejectValue("confirmPassword", "error", "Hesla se nerovnají.");
            return "/pages/account/register";
        }

        redirectAttributes.addFlashAttribute("success", "Uživatel zaregistrován.");
        return "redirect:/ucet/prihlaseni";
    }
    @GetMapping("/zapomenuteheslo")
    public String renderPassword() {
        return "/pages/account/password";
    }
}
