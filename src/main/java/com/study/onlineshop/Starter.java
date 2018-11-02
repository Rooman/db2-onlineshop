package com.study.onlineshop;

import com.study.onlineshop.dao.jdbc.JdbcProductDao;
import com.study.onlineshop.service.impl.DefaultProductService;
import com.study.onlineshop.web.filter.UserRoleSecurityFilter;
import com.study.onlineshop.web.servlet.LoginServlet;
import com.study.onlineshop.web.servlet.ProductsApiServlet;
import com.study.onlineshop.web.servlet.ProductsServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.DispatcherType;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class Starter {
    public static void main(String[] args) throws Exception {
        // configure daos
        JdbcProductDao jdbcProductDao = new JdbcProductDao();

        // configure services
        DefaultProductService defaultProductService = new DefaultProductService(jdbcProductDao);

        // store
        List<String> activeTokens = new ArrayList<>();

        // servlets
        ProductsServlet productsServlet = new ProductsServlet();
        productsServlet.setProductService(defaultProductService);
        productsServlet.setActiveTokens(activeTokens);
        ProductsApiServlet productsApiServlet = new ProductsApiServlet(defaultProductService);

        // config web server
        ServletContextHandler servletContextHandler = new ServletContextHandler();
        servletContextHandler.addServlet(new ServletHolder(productsServlet), "/products");
        servletContextHandler.addServlet(new ServletHolder(productsServlet), "/");

        servletContextHandler.addServlet(new ServletHolder(productsApiServlet), "/api/v1/products");
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(activeTokens)), "/login");

        servletContextHandler.addFilter(new FilterHolder(new UserRoleSecurityFilter(securityService)), "/products",
                EnumSet.of(DispatcherType.REQUEST));

        servletContextHandler.addFilter(new FilterHolder(new UserRoleSecurityFilter(securityService)), "/product | /product/add",
                EnumSet.of(DispatcherType.REQUEST));

        Server server = new Server(8080);
        server.setHandler(servletContextHandler);
        server.start();
    }
}
0101010101010101010111110010101010101