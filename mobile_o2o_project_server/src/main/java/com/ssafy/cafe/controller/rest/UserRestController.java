package com.ssafy.cafe.controller.rest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ssafy.cafe.model.dto.Order;
import com.ssafy.cafe.model.dto.User;
import com.ssafy.cafe.model.service.OrderService;

import com.ssafy.cafe.model.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/rest/user")
@CrossOrigin("*")
public class UserRestController {

	private static final Logger logger = LoggerFactory.getLogger(UserRestController.class);

	@Autowired
	UserService uService;

	@Autowired
	OrderService oService;

	@PostMapping
	@Operation(summary = "사용자 정보를 추가한다. 성공하면 true를 리턴한다. ", description = "모든 정보를 입력해야 회원가입이 가능하다")
	public Boolean insert(@RequestBody User user) {
		logger.debug("user.insert", user);
		int result = 0;
		try {
			result = uService.join(user);
		} catch (Exception e) {
			result = -1;
		}

		if (result == 1) {
			return true;
		} else {
			return false;
		}
	}

	@GetMapping("/isUsed")
	@Operation(summary = "request parameter로 전달된 id가 이미 사용중인지 반환한다.")
	public Boolean isUsedId(String id) {
		return uService.isUsedId(id);
	}

	@PostMapping("/login")
	@Operation(summary = "로그인 처리 후 성공적으로 로그인 되었다면 loginId라는 쿠키를 내려보낸다.", description = "<pre>id와 pass 두개만 넘겨도 정상동작한다. \n 아래는 id, pass만 입력한 샘플코드\n"
			+ "{\r\n" + "  \"id\": \"aa12\",\r\n" + "  \"pass\": \"aa12\"\r\n" + "}" + "</pre>")

	public User login(@RequestBody User user, HttpServletResponse response) throws UnsupportedEncodingException {
		User selected = uService.login(user.getId(), user.getPass());
		if (selected != null) {
			Cookie cookie = new Cookie("loginId", URLEncoder.encode(selected.getId(), "utf-8"));
//            Cookie cookie = new Cookie("loginId", selected.getId());
			cookie.setMaxAge(60 * 60 * 24 * 30); // 30일
			response.addCookie(cookie);
		}
		return selected;
	}

	@GetMapping("/info")
	@Operation(summary = "사용자의 정보를 반환한다.", description = "아이디를 쿼리 파라미터로 넘기면 해당 유저의 정보를 반환한다.")
	public User getInfo(@RequestParam String id) {
		// ID로 사용자 정보 조회
		User selected = uService.selectUser(id);

		if (selected == null) {
			return null; // 유저 정보가 없으면 null 반환
		} else {
			return selected; // 유저 정보만 반환
		}
	}
	
	
	
	@PutMapping("/update")
	@Operation(summary = "회원 정보를 업데이트", description = "사용자 정보를 업데이트하고 업데이트된 사용자 객체를 반환한다.")
	public User updateUserInfo(@RequestParam String id, @RequestParam String address) {
		User user = uService.selectUser(id);
		if(user==null) {
			return null;
		}
		
		user.setAddress(address);
		uService.updateUser(user);
		
		return user;
	}
	// 이게 왜 필요할까????????? 주석처리함 : 2023.08.12
	// TODO :없애야 할 듯....
//    @GetMapping("/info")
//    @ApiOperation(value = "사용자의 정보와 함께 사용자의 주문 내역, 사용자 등급 정보를 반환한다.",
//    	notes = "6단계에서 사용됨.", response = Map.class)
//    public Map<String, Object> getInfo(String id) {
//        User selected = uService.selectUser(id);
//        if (selected == null) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("user", new User());
//            return map;
//        } else {
//            Map<String, Object> info = new HashMap<>();
//            info.put("user", selected);
//            List<Order> orders = oService.getOrdreByUser(selected.getId());
//            info.put("order", orders);
//            info.put("grade", getGrade(selected.getStamps()));
//            return info;
//        }
//    }

//    // 위에 꺼 대신해서 이걸 만들었다. 
//    // password를 sharedpreference에 저장하면 안되니, id만 받는데, 
//    // 이 id와 쿠키에 있는 id가 같은지 확인해서 로그인 사용자를 조회해서 리턴함. 
//    @GetMapping("/info")
//    @Operation(summary = "사용자의 정보와 함께 사용자의 주문 내역, 사용자 등급 정보를 반환한다.",
//    	description = "관통 6단계(Android app)에서 사용됨. 로그인 성공한 cookie 정보가 없으면 전체 null이 리턴됨")
//    public Map<String, Object> getInfo(HttpServletRequest request, String id) {
//  	    String idInCookie = "";
//        Cookie [] cookies = request.getCookies();
//        if(cookies != null) {
//       	  for (Cookie cookie : cookies) {
//  			try {
//  			  if("loginId".equals(cookie.getName())){
//  				  idInCookie = URLDecoder.decode(cookie.getValue(), "utf-8");
//  				  System.out.println("value : "+URLDecoder.decode(cookie.getValue(), "utf-8"));
//  			  }
//  			} catch (UnsupportedEncodingException e) {
//  				// TODO Auto-generated catch block
//  				e.printStackTrace();
//  			}
//  		  }
//        }
//        
//        User selected = uService.selectUser(id);
//
//        if(!id.equals(idInCookie)) {
//  		  logger.info("different cookie value : inputValue : {}, inCookie:{}", id, idInCookie);
//  		  selected = null; // 사용자 정보 삭제.
//  	    }else {
//  		  logger.info("valid cookie value : inputValue : {}, inCookie:{}", id, idInCookie);
//  	    }
//  	  
//  	    if (selected == null) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("user", new User());
//            return map;
//        } else {
//            Map<String, Object> info = new HashMap<>();
//            info.put("user", selected);
//            List<Order> orders = oService.getOrderByUser(selected.getId());
//            info.put("order", orders);
//            info.put("grade", getGrade(selected.getStamps()));
//            return info;
//        }
//    }

//    @PostMapping("/info")
//    @Operation(summary = "사용자의 정보와 함께 사용자의 주문 내역, 사용자 등급 정보를 반환한다.",
//    description = "아래 User객체에서 id, pass 두개의 정보만 json으로 넘기면 정상동작한다.")
//    public Map<String, Object> getInfo(@RequestBody User user) {
//        User selected = uService.login(user.getId(), user.getPass());
//        if (selected == null) {
//            return null;
//        } else {
//            Map<String, Object> info = new HashMap<>();
//            info.put("user", selected);
//            List<Order> orders = oService.getOrderByUser(user.getId());
//            info.put("order", orders);
//            info.put("grade", getGrade(selected.getStamps()));
//            return info;
//        }
//    }

//   기존 작성 로직인데, 갯수가 잘못 카운팅 되는 듯 해서 아래 calculateGrade 로직으로 변경. 2023.08.12    
	// 23.11.10 원복.

}
