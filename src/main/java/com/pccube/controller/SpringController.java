package com.pccube.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.pccube.entities.Task;
import com.pccube.entities.TaskRepository;
import com.pccube.entities.User;
import com.pccube.entities.UserRepository;

@RestController
public class SpringController {

	@Autowired
	TaskRepository taskRepository;

	@Autowired
	UserRepository userRepository;

	
	//@RequestMapping(value = "/taskTest", method = RequestMethod.POST)
	@PostMapping("/taskTest")
	public void task(Model model, @RequestParam("description") String description, @RequestParam("type") String type,
			@RequestParam("father") String father, @RequestParam("user") String user) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		boolean role = false;
				role = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("admin"));
				
		User u = userRepository.findByUsername(user);
		System.out.println("Usernameeeeeeeeeee: "+u.getUsername());
		Task task = new Task();
		task.setDescrizione(description);
		task.setType(type);
		task.setUser(u);

		if ((u != null) && (u.getType().equals("user")) && (role)) {
			u.getTasks().add(task);
		} else if (u == null || (!role)) {
			model.addAttribute("message", "Error");
			return;// "taskError";
		}

		taskRepository.save(task);

		model.addAttribute("message", "Success");

		//return "task";

	}

	@RequestMapping(value = "/read", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Task> read(Model model) {

		List<Task> tasks = (List<Task>) taskRepository.findAll();

		return tasks;
	}
	
	@RequestMapping(value = "/readTaskUser", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Task> readTaskUser(Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		User u = userRepository.findByUsername(username);
		System.out.println("USERN: "+u.getUsername());
		System.out.println("USERN: "+u.getType());
		List<Task> tasks = (List<Task>) taskRepository.findByUser(u);//findAll();
		System.out.println("tasks size: "+tasks.size());
		List<Task> tasks2 = (List<Task>) taskRepository.findAll();
		System.out.println("tasks2 all size: "+tasks2.size());
		System.out.println("tasks2.get(0).getUser().getUsername(): "+tasks2.get(0).getUser().getUsername());
		return tasks;
	}

	@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<User> user(Model model) {

		List<User> users = (List<User>) userRepository.findAll();

		return users;
	}

}