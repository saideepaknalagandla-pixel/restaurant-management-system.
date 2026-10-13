package com.restaurant.servlet;

import com.restaurant.dao.RestaurantDAO;
import com.restaurant.model.CartItem;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {
    private final RestaurantDAO dao=new RestaurantDAO();
    @SuppressWarnings("unchecked")
    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        Map<Integer,CartItem> cart=(Map<Integer,CartItem>)req.getSession().getAttribute("cart");
        if(cart==null||cart.isEmpty()){resp.sendRedirect(req.getContextPath()+"/cart");return;}
        req.getRequestDispatcher("/checkout.jsp").forward(req,resp);
    }
    @SuppressWarnings("unchecked")
    @Override protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        req.setCharacterEncoding("UTF-8");
        String name=req.getParameter("name"),email=req.getParameter("email");
        if(name==null||name.isBlank()||name.length()>100||email==null||!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")||email.length()>150){
            req.setAttribute("error","Enter a valid name and email address.");doGet(req,resp);return;
        }
        Map<Integer,CartItem> cart=(Map<Integer,CartItem>)req.getSession().getAttribute("cart");
        if(cart==null||cart.isEmpty()){resp.sendRedirect(req.getContextPath()+"/menu");return;}
        Map<Integer,Integer> quantities=new LinkedHashMap<>();cart.forEach((id,item)->quantities.put(id,item.getQuantity()));
        try{
            int orderId=dao.placeOrder(name.trim(),email.trim(),quantities);
            req.getSession().removeAttribute("cart");req.setAttribute("orderId",orderId);
            req.getRequestDispatcher("/success.jsp").forward(req,resp);
        }catch(Exception e){throw new ServletException("Order could not be saved. Check the database settings.",e);}
    }
}