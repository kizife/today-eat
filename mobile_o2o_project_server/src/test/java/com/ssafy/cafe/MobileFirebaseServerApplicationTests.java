package com.ssafy.cafe;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ssafy.cafe.model.service.FirebaseCloudMessageServiceWithData;
import com.ssafy.cafe.model.service.FirebaseCloudMessageService;

@SpringBootTest(classes =  MobileFirebaseServerApplication.class)
class MobileFirebaseServerApplicationTests {

    @Autowired
    FirebaseCloudMessageService service;

    @Autowired
    FirebaseCloudMessageServiceWithData dataService;

    //기본적으로 data를 보내면 foreground에 있을때만 받을 수 있다. 
    //background에서도 대응하기 위해서는 data에 담아서 전달해야 하는데, 아래 dataMessage로 호출해야 한다.  
    @Test
    void sendMessage() throws IOException {
    	
        String token = "eUcjU0pCRXqVSc8qkczP_C:APA91bGaUn-FoTXmx0FlZpRpam7qmTnIVXPJcqkL0uj9QjL-CylSAvd5WeZ1F75-XlIIYu0LPtZ47KdKCYvf9wBre4i5czXgBc9IncSdN4OWIMs41RaNI3s";
//        String token1 = "cHI9bL4QQTKQI_nJVtoUt7:APA91bF-VKnKY0NxeikBXORc0FhmS3iRHFYZgH5yBF34GNt-tYaUCvABVal1CSibhAEWkj9DDcmpZWlD4lA481JoC_UjVnykSYhyd5OHi3-91E-kVlX4YjEEK2v0fTQlqoW35yjNnlrd";
//		한건 메시지
        service.sendMessageTo(token, "from 사무국", "싸피 여러분 화이팅!!");
        
//		멀티 메시지        
//        service.addToken(token);
//        service.addToken(token1);
        
//        service.broadCastMessage("from 사무국1", "싸피 여러분 화이팅!!!!!!!!");
    }

    
}
