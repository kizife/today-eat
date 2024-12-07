package com.ssafy.cafe.controller.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.cafe.model.dto.Comment;
import com.ssafy.cafe.model.service.CommentService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/rest/comment")
@CrossOrigin("*")
public class CommentRestController {

    private static final Logger logger = LoggerFactory.getLogger(CommentRestController.class);

    @Autowired
    CommentService cService;
    
    @PostMapping
    @Operation(summary="comment 객체를 추가한다. 성공하면 true를 리턴한다. 평점은 1.0 ~ 5.0만 허용. 벗어면 false를 리턴한다. ", 
    		description = "<pre>id는 자동생성되는 comment의 id이므로 입력해도 무시된다. \n"
    				+ "아래는 'test' 사용자가 1번 상품에 5점과 comment를 입력한 모습임.\n"
    				+ "{\r\n"
    				+ "  \"comment\": \"It's Good\",\r\n"
    				+ "  \"id\": 0,\r\n"
    				+ "  \"productId\": 1,\r\n"
    				+ "  \"rating\": 5,\r\n"
    				+ "  \"userId\": \"id 01\"\r\n"
    				+ "}"
    				+ "</pre>")
    public Boolean insert(@RequestBody Comment comment ) {
    	logger.info("insert comment : {}", comment);
    	if(comment.getRating() >= 1.0 && comment.getRating() <= 5.0) {
        	int result = cService.addComment(comment);
        	if(result >= 1) {
                return true;
        	}else {
        		return false;
        	}
    	}else {
            return false;
    	}
    }

    @DeleteMapping("/{id}")
    @Operation(summary="{id}에 해당하는 댓글 정보를 삭제한다. 성공하면 true를 리턴한다. ")
    public Boolean delete(@PathVariable Integer id) {
    	logger.info("delete comment : {}", id);

        int result = cService.removeComment(id);
        if(result >= 1) {
	        return true;
        }else {
        	return false;
        }
    }
    
    @PutMapping
    @Operation(summary="comment 객체를 수정한다. 성공하면 true를 리턴한다. 평점은 1.0 ~ 5.0만 허용. 벗어면 false를 리턴한다. ")
    public Boolean update(@RequestBody Comment comment) {
    	logger.info("update comment : {}", comment);

    	if(comment.getRating() >= 1.0 && comment.getRating() <= 5.0) {
	        int result = cService.updateComment(comment);
	        if(result >= 1) {
		        return true;
	        }else {
	        	return false;
	        }
    	}else {
            return false;
    	}
    }
    

}
