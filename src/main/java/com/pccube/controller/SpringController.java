package com.pccube.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.pccube.entities.Task;
import com.pccube.entities.TaskRepository;
import com.pccube.entities.User;
import com.pccube.entities.UserRepository;

@Controller
public class SpringController {

	@Autowired
	TaskRepository repository;

	@Autowired
	UserRepository userRepository;

	@RequestMapping(value = "/taskTest", method = RequestMethod.POST)
	public String task(Model model, @RequestParam("description") String description, @RequestParam("type") String type,
			@RequestParam("father") String father, @RequestParam("user") String user) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		boolean role = false;
				role = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("admin"));

	
				
		User u = userRepository.findByUsername(user);

		Task task = new Task();
		task.setDescrizione(description);
		task.setType(type);

		if ((u != null) && (u.getType().equals("user")) && (role)) {
			u.getTasks().add(task);
		} else if (u == null || (!role)) {
			model.addAttribute("message", "Error");
			return "task";
		}

		repository.save(task);

		model.addAttribute("message", "Success");

		return "task";

	}

	@RequestMapping(value = "/read", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Task> read(Model model) {

		List<Task> tasks = (List<Task>) repository.findAll();

		return tasks;
	}

	@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<User> user(Model model) {

		List<User> users = (List<User>) userRepository.findAll();

		return users;
	}

}