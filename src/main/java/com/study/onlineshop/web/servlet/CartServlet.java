package com.study.onlineshop.web.servlet;

import com.study.onlineshop.entity.Cart;
import com.study.onlineshop.entity.Product;
import com.study.onlineshop.security.SecurityService;
import com.study.onlineshop.security.Session;
import com.study.onlineshop.service.CartService;
import com.study.onlineshop.web.templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class CartServlet extends HttpServlet {
    private SecurityService securityService;
    private CartService cartService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PageGenerator pageGenerator = PageGenerator.instance();
        HashMap<String, Object> parameters = new HashMap<>();

        String token = securityService.getValidatedToken(request.getCookies());
        if (token != null) {
            Optional<Session> sessionOptional = securityService.getSession(token);

            Session session = sessionOptional.orElseGet(new Supplier<Session>() {
                @Override
                public Session get() {
                    return Session.EMPTY;
                }
            });

            Session session = sessionOptional.orElseThrow(new::NoSessionExecption);
//            if (sessionOptional.isPresent()) {
//                Session session = sessionOptional.get();
//            }else {
//
//            }


            Cart cart = sessionOptional.getCart();
            if (cart != null) {
                List<Product> cartProducts = cart.getProducts();
                if (!cartProducts.isEmpty()) {
                    parameters.put("cartProducts", cartProducts);
                }
            }
        }

        String page = pageGenerator.getPage("cart", parameters);
        response.getWriter().write(page);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String token = securityService.getValidatedToken(request.getCookies());
        if (token != null) {
            Session session = securityService.getSession(token);
            Cart cart = session.getCart();
            if (cart == null) {
                cart = new Cart();
                session.setCart(cart);
            }
            int productId = Integer.valueOf(request.getParameter("product_id"));
            cartService.addToCart(cart, productId);
            response.sendRedirect("/products");
        } else {
            response.sendRedirect("/login");
        }
    }

    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }
}
