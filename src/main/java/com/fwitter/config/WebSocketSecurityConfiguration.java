//package com.fwitter.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
//import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
//
//@Configuration
//public class WebSocketSecurityConfiguration extends AbstractSecurityWebSocketMessageBrokerConfigurer {
//
//    @Override
//    protected boolean sameOriginDisabled() {
//        return true; // Allow cross-origin WebSocket connections
//    }
//
//    @Override
//    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
//        messages
//                .simpDestMatchers("/ws/**").authenticated()  // Require authentication for WebSocket
//                .anyMessage().authenticated();
//    }
//}
//
//
//
////package com.fwitter.config;
//
////import org.springframework.context.annotation.Configuration;
////import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
////import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
////
////@Configuration
////public class WebSocketSecurityConfiguration extends AbstractSecurityWebSocketMessageBrokerConfigurer {
////
////    @Override
////    protected boolean sameOriginDisabled() {
////        return true; // Allow cross-origin WebSocket connections
////    }
////
////    @Override
////    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
////        messages
////                .simpDestMatchers("/app/**").authenticated()  // Require authentication for app messages
////                .simpDestMatchers("/user/**").authenticated() // Secure private messages
////                .anyMessage().denyAll();  // Deny all other messages by default
////    }
////}


package com.fwitter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketSecurityConfiguration extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected boolean sameOriginDisabled(){
        return true;
    }

    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages){
        messages.simpDestMatchers("/ws").permitAll()
                .anyMessage().permitAll();
    }


}