package com.restaurant.servlet;

import com.restaurant.dao.RestaurantDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/menu")
public class MenuServlet extends HttpServlet {
    private final RestaurantDAO dao=new RestaurantDAO();
    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        try{req.setAttribute("menuItems",dao.getAvailableMenu());req.getRequestDispatcher("/menu.jsp").forward(req,resp);}
        catch(Exception e){throw new ServletException("Could not load the menu. Check MySQL settings.",e);}
    }
}