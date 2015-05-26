package com.bank.mvc.controllers;



import com.bank.mvc.domain.validation.UserValidator;
import com.bank.mvc.models.User;
import com.bank.mvc.domain.service.UserService;
import com.bank.mvc.models.UserRole;
import com.bank.mvc.models.enums.ListRole;
import com.bank.mvc.utils.JsonResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@Controller
class MainController {

    private final static Logger logger = Logger.getLogger(MainController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    MessageSource msgSrc;


    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.HEAD})
    public String login(Model model) {
        return "login";
    }

    @RequestMapping(value = "/access_denied", method = {RequestMethod.GET, RequestMethod.HEAD})
    public String accessDenied() {
        return "access_denied";
    }

    @RequestMapping(value = "/dashboard/redirect", method = {RequestMethod.GET, RequestMethod.HEAD})
    public String dashboardRedirect() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user == null) return "redirect:/";
        Set<UserRole> roles = user.getUserRoles();
        for (UserRole role : roles) {
            if (role.getListRole() == ListRole.ROLE_CLIENT) {
                return "redirect:/dashboard/client/main";
            } else if (role.getListRole() == ListRole.ROLE_EMPLOYEE) {
                return "redirect:/dashboard/employee/main";
            } else if (role.getListRole() == ListRole.ROLE_ADMIN) {
                return "redirect:/dashboard/admin/main";
            }
        }
        return "access_denied";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public @ResponseBody JsonResponse createNewUser(@RequestBody User user) {
        logger.info("POST: /register");
        Map<String, String> data = userValidator.validate(user);
        if (!data.isEmpty()) {
            return new JsonResponse("ERROR", data);
        }

        userService.saveUser(user);
        data.put("message", msgSrc.getMessage("registerform.successMessage", null, Locale.getDefault()));
        return new JsonResponse("OK", data);
    }

}
