package ru.itis.handlers;

import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeFailureException;
import org.springframework.web.socket.server.HandshakeHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import ru.itis.security.JwtUtils;

import java.util.Map;

@Component
public class AuthHandshakeHandler implements HandshakeHandler {

    private final DefaultHandshakeHandler defaultHandshakeHandler;
    private final JwtUtils jwtUtils;

    public AuthHandshakeHandler(JwtUtils jwtUtils) {
        this.defaultHandshakeHandler = new DefaultHandshakeHandler();
        this.jwtUtils = jwtUtils;
    }

    @SneakyThrows
    @Override
    public boolean doHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse,
                               WebSocketHandler webSocketHandler, Map<String, Object> map) throws HandshakeFailureException {
        ServletServerHttpRequest request = (ServletServerHttpRequest) serverHttpRequest;
        ServletServerHttpResponse response = (ServletServerHttpResponse) serverHttpResponse;
        boolean isAuthenticated = jwtUtils.validateToken(request.getServletRequest(), response.getServletResponse());
        if (isAuthenticated) {
            return defaultHandshakeHandler.doHandshake(serverHttpRequest, serverHttpResponse, webSocketHandler, map);
        } else {
            serverHttpResponse.setStatusCode(HttpStatus.FORBIDDEN);
            return false;
        }
    }
}
