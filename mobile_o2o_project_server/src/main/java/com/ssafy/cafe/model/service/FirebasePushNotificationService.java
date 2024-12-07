//package com.ssafy.cafe.model.service;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.auth.oauth2.GoogleCredentials;
//import okhttp3.*;
//import org.springframework.stereotype.Service;
//import org.springframework.http.HttpHeaders;
//import org.springframework.core.io.ClassPathResource;
//
//import com.ssafy.cafe.model.dto.FcmMessage;
//import com.ssafy.cafe.model.dto.FcmMessage.Message;
//import com.ssafy.cafe.model.dto.FcmMessage.Notification;
//import com.ssafy.cafe.util.Constants;
//
//import java.io.IOException;
//import java.util.List;
//
//@Service
//public class FirebasePushNotificationService {
//
//	private final ObjectMapper objectMapper;
//
//	public FirebasePushNotificationService(ObjectMapper objectMapper) {
//		this.objectMapper = objectMapper;
//	}
//
//	/**
//	 * FCM에 push 요청을 보낼 때 인증을 위해 Header에 포함시킬 AccessToken 생성
//	 *
//	 * @return Access Token String
//	 * @throws IOException
//	 */
//	private String getAccessToken() throws IOException {
//		GoogleCredentials googleCredentials = GoogleCredentials
//				.fromStream(new ClassPathResource(Constants.FIREBASE_KEY_FILE).getInputStream())
//				.createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
//
//		googleCredentials.refreshIfExpired();
//		return googleCredentials.getAccessToken().getTokenValue();
//	}
//
//	private final String API_URL = "https://fcm.googleapis.com/v1/projects/finalproject-5328a/messages:send";
//
//	/**
//	 * targetToken에 해당하는 device로 FCM 푸시 알림 전송
//	 *
//	 * @param targetToken Device Token
//	 * @param title       Notification Title
//	 * @param body        Notification Body
//	 * @throws IOException
//	 */
//	public void sendMessageTo(String targetToken, String title, String body) throws IOException {
//		String message = makeMessage(targetToken, title, body);
//		OkHttpClient client = new OkHttpClient();
//		RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
//		Request request = new Request.Builder().url(Constants.API_URL).post(requestBody)
//				// 전송 토큰 추가
//				.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
//				.addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8").build();
//
//		Response response = client.newCall(request).execute();
//		System.out.println(response.body().string());
//	}
//
//	/**
//	 * FCM 알림 메시지 생성
//	 *
//	 * @param targetToken
//	 * @param title
//	 * @param body
//	 * @return JSON 형태의 메시지
//	 * @throws JsonProcessingException
//	 */
//	private String makeMessage(String targetToken, String title, String body) throws JsonProcessingException {
//		Notification noti = new FcmMessage.Notification(title, body, null);
//		Message message = new FcmMessage.Message(noti, targetToken);
//		FcmMessage fcmMessage = new FcmMessage(false, message);
//
//		return objectMapper.writeValueAsString(fcmMessage);
//	}
//}