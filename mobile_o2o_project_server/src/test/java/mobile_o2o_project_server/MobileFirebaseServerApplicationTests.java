//
//import java.io.IOException;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import com.ssafy.cafe.model.service.FirebasePushNotificationService;
//
//@SpringBootTest
//class MobileFirebaseServerApplicationTests {
//
//    @Autowired
//    private FirebasePushNotificationService service;
//
//    @Test
//    void sendMessage() throws IOException {
//        String token = "eUcjU0pCRXqVSc8qkczP_C:APA91bGaUn-FoTXmx0FlZpRpam7qmTnIVXPJcqkL0uj9QjL-CylSAvd5WeZ1F75-XlIIYu0LPtZ47KdKCYvf9wBre4i5czXgBc9IncSdN4OWIMs41RaNI3s";
//        service.sendMessageTo(token, "from 사무국", "싸피 여러분 화이팅!!");
//    }
//}