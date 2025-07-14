//package com.fwitter.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.messaging.simp.config.MessageBrokerRegistry;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
//import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
//import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
//import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
//
//import java.security.Principal;
//import java.util.Map;
//
//@Configuration
//@EnableWebSocketMessageBroker
//public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        registry.enableSimpleBroker("/user");
//        registry.setApplicationDestinationPrefixes("/app");
//        registry.setUserDestinationPrefix("/user");
//    }
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry){
//        registry.addEndpoint("/ws").setAllowedOrigins("http://localhost:3000").withSockJS();
//    }
//}
//
////    @Override
////    public void registerStompEndpoints(StompEndpointRegistry registry) {
////        registry.addEndpoint("/ws")
////                .setHandshakeHandler(new DefaultHandshakeHandler() {
////                    @Override
////                    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
////                        String token = request.getURI().getQuery().replace("token=", "");
////                        if (token != null && !token.isEmpty()) {
////                            return new UsernamePasswordAuthenticationToken(token, null);
////                        }
////                        return null;
////                    }
////                })
////                .setAllowedOrigins("http://localhost:3000")
////                .withSockJS();
////    }
////}
//
////
////
////package com.fwitter.config;
////
////import org.springframework.context.annotation.Configuration;
////import org.springframework.messaging.simp.config.MessageBrokerRegistry;
////import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
////import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
////import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
////
////@Configuration
////@EnableWebSocketMessageBroker
////public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
////
////    @Override
////    public void configureMessageBroker(MessageBrokerRegistry registry) {
////        registry.enableSimpleBroker("/topic", "/queue"); // Public and Private messaging
////        registry.setApplicationDestinationPrefixes("/app");
////        registry.setUserDestinationPrefix("/user"); // For direct user messages
////    }
////
////    @Override
////    public void registerStompEndpoints(StompEndpointRegistry registry) {
////        registry.addEndpoint("/ws")
////                .setAllowedOrigins("http://localhost:3000")
////                .withSockJS();
////    }
////}
//
//
////import org.springframework.context.annotation.Configuration;
////import org.springframework.messaging.simp.config.MessageBrokerRegistry;
////import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
////import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
////import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
////
////@Configuration
////@EnableWebSocketMessageBroker
////public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
////
////    @Override
////    public void configureMessageBroker(MessageBrokerRegistry config) {
////        config.enableSimpleBroker("/topic", "/queue"); // Enable a broker
////        config.setApplicationDestinationPrefixes("/app"); // Prefix for client messages
////    }
////
////    @Override
////    public void registerStompEndpoints(StompEndpointRegistry registry) {
////        registry.addEndpoint("/ws").setAllowedOrigins("*"); // Enable WebSocket endpoint
////        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS(); // Fallback for older browsers
////    }
////}


package com.fwitter.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry){
        registry.enableSimpleBroker("/user");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/ws").setAllowedOrigins("http://localhost:3000").withSockJS();
    }


}