package com.lubomirgeorgiev.meal_planner.security;

import com.lubomirgeorgiev.meal_planner.model.dto.user.UserDto;
import com.lubomirgeorgiev.meal_planner.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Set;
import java.util.UUID;

@Component
public class SessionInterceptor implements HandlerInterceptor {

    private static final Set<String> UNAUTHENTICATED_ENDPOINTS = Set.of("/", "/home", "/dishes", "/login", "/register", "/error");
    private static final String ADMIN_PREFIX_ENDPOINTS ="/admin";
    private final UserService userService;

    public SessionInterceptor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            Object o) throws Exception {

        String endpoints = httpServletRequest.getServletPath();

        if (UNAUTHENTICATED_ENDPOINTS.contains(endpoints)) {
            return true;
        }

        HttpSession session = httpServletRequest.getSession(false);
        if (session == null) {
            httpServletResponse.sendRedirect("/login");
            return false;
        }

        UUID userId = (UUID) session.getAttribute("user_id");
        if (userId == null) {
            session.invalidate();
            httpServletResponse.sendRedirect("/login");
            return false;
        }

        UserDto userDto = userService.getById(userId);

        if (endpoints.startsWith(ADMIN_PREFIX_ENDPOINTS) && !userDto.getRole().name().equals("ADMIN")) {
            httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpServletResponse.getWriter().write("You do not have permission to access this resource");
            return false;
        }

        return true;
    }
}
