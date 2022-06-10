package com.web.controller;

import com.google.gson.Gson;
import com.web.vo.order.OrderVO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "OrderServlet", urlPatterns = "/api/v1/order")
public class OrderServlet extends HttpServlet {

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        Cookie[] cookies = request.getCookies();
        HttpSession session = request.getSession();
        OrderVO orderVO = new OrderVO();
        orderVO.setId("1");
        orderVO.setMoney(100);
        orderVO.setAddress("北京");
        orderVO.setUserName("巴黎站前");
        response.getWriter().write(new Gson().toJson(orderVO));
    }
}
