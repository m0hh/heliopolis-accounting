package com.helioplis.accounting.firebase;

import com.google.firebase.messaging.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FirebaseMessagingService {

    private final FirebaseMessaging firebaseMessaging;

    public FirebaseMessagingService(FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
    }


    public String sendNotificationToUser(Note note, String token) throws FirebaseMessagingException {

        Notification notification = Notification
                .builder()
                .setTitle(note.getSubject())
                .setBody(note.getContent())
                .build();

        Message message = Message
                .builder()
                .setToken(token)
                .setNotification(notification)
                .putAllData(note.getData())
                .build();

        return firebaseMessaging.send(message);
    }
    // Send notification to all users subscribed to a topic
    public String sendNotificationToTopic(Note note, String topic) throws FirebaseMessagingException {
        Notification notification = Notification
                .builder()
                .setTitle(note.getSubject())
                .setBody(note.getContent())
                .build();

        Message message = Message
                .builder()
                .setTopic(topic) // Specify the topic here
                .setNotification(notification)
                .putAllData(note.getData())
                .build();

        return firebaseMessaging.send(message);
    }

    public BatchResponse sendNotificationToMultipleUsers(Note note, List<String> tokens) throws FirebaseMessagingException {
        Notification notification = Notification
                .builder()
                .setTitle(note.getSubject())
                .setBody(note.getContent())
                .build();

        MulticastMessage multicastMessage = MulticastMessage
                .builder()
                .addAllTokens(tokens)
                .setNotification(notification)
                .putAllData(note.getData())
                .build();

        return firebaseMessaging.sendMulticast(multicastMessage);
    }

    public TopicManagementResponse subscribeToTopic(String token, String topic) throws FirebaseMessagingException {
        return firebaseMessaging.subscribeToTopic(List.of(token), topic);
    }

    public TopicManagementResponse unsubscribeFromTopic(String token, String topic) throws FirebaseMessagingException {
        return firebaseMessaging.unsubscribeFromTopic(List.of(token), topic);
    }

}