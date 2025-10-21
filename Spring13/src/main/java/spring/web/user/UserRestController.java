package spring.web.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import spring.domain.User;

@RestController
@RequestMapping("/userAPI/*")
public class UserRestController {

	public UserRestController() {
		System.out.println(":: UserRestController default Constructor call");
	}

	@RequestMapping(value = "getUser", method = RequestMethod.GET)
	public User getUser(@RequestParam("name") String name, @RequestParam("age") int age) throws Exception {
		System.out.println();
		System.out.println(name);
		System.out.println(age);

		User returnUser = new User();
		returnUser.setUserId("AAA");
		returnUser.setUserName("GET : 이순신");
		returnUser.setAge(100);
		System.out.println(returnUser);

		return returnUser;
	}

	@RequestMapping(value = "/getUserMore/{value}", method = RequestMethod.GET)
	public Map getUserUserMore(@PathVariable String value, @RequestParam("name") String name,
			@RequestParam("age") int age) throws Exception {
		System.out.println();
		System.out.println(value);
		System.out.println(name);
		System.out.println(age);

		User returnUser = new User();
		returnUser.setUserId("AAA");
		returnUser.setUserName("GET: 이순신");
		returnUser.setAge(100);
		System.out.println(returnUser);

		Map map = new HashMap();
		map.put("user", returnUser);
		map.put("message", "ok");

		return map;
	}

	@RequestMapping(value = "getUser", method = RequestMethod.POST)
	public User getUser(@RequestBody User user) throws Exception {
		System.out.println();
		System.out.println("[FROM Client Data]");
		System.out.println("1 : " + user);

		System.out.println("[TO Client Data]");
		User returnUser = new User();
		returnUser.setUserId("AAA");
		returnUser.setUserName("POST : 이순신");
		System.out.println("2 : " + returnUser);

		return returnUser;
	}

	@RequestMapping(value = "getUserMore/{value}", method = RequestMethod.POST)
	public Map getUserMore(@PathVariable String value, @RequestBody User user) throws Exception {
		System.out.println();
		System.out.println("[FROM Client Data]");
		System.out.println("1 : " + user);

		System.out.println("[TO Client Data]");
		User returnUser = new User();
		returnUser.setUserId("AAA");
		returnUser.setUserName("POST : 이순신");
		System.out.println("2 : " + returnUser);

		Map map = new HashMap();
		map.put("user", returnUser);
		map.put("message", "ok");

		return map;
	}
}
