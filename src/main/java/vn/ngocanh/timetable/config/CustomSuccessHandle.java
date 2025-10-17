package vn.ngocanh.timetable.config;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.ngocanh.timetable.domain.Cart;
import vn.ngocanh.timetable.domain.User;
import vn.ngocanh.timetable.repository.CartRepository;
import vn.ngocanh.timetable.service.UserService;

public class CustomSuccessHandle implements AuthenticationSuccessHandler {
    @Autowired
    private UserService userService;
    @Autowired
    private CartRepository cartRepository;
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    protected String determineTargetUrl(final Authentication authentication) {

        Map<String, String> roleTargetUrlMap = new HashMap<>();
        roleTargetUrlMap.put("ROLE_USER", "/");
        roleTargetUrlMap.put("ROLE_ADMIN", "/admin");

        final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (final GrantedAuthority grantedAuthority : authorities) {
            String authorityName = grantedAuthority.getAuthority();
            if (roleTargetUrlMap.containsKey(authorityName)) {
                return roleTargetUrlMap.get(authorityName);
            }
        }

        throw new IllegalStateException();
    }

    protected void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {

        String targetUrl = determineTargetUrl(authentication);

        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, Authentication authentication) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        String email = authentication.getName();
        User user = this.userService.getUserByEmail(email);
        session.setAttribute("fullName", user.getFullName());
        session.setAttribute("avatar", user.getAvatar());
        session.setAttribute("email", user.getEmail());
        session.setAttribute("id", user.getId());
        Cart cart = this.cartRepository.findOneByUser(user);
        if (cart == null) {
            Cart otherCart = new Cart();
            otherCart.setSum(0);
            otherCart.setUser(user);
            otherCart.setSumCredit(0);
            cart = this.cartRepository.save(otherCart);
        }
        session.setAttribute("sum", cart.getSum());
        session.setAttribute("sumCredit", cart.getSumCredit());
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {
        handle(request, response, authentication);
        clearAuthenticationAttributes(request, authentication);
    }
}
