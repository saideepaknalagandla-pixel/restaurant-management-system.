package com.restaurant.servlet;

import com.restaurant.dao.RestaurantDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
    private final RestaurantDAO dao=new RestaurantDAO();
    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        try{req.setAttribute("orders",dao.getRecentOrders());req.setAttribute("menuItems",dao.getAvailableMenu());req.getRequestDispatcher("/admin.jsp").forward(req,resp);}
        catch(Exception e){throw new ServletException("Could not load admin page. Check MySQL settings.",e);}
    }
    @Override protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        req.setCharacterEncoding("UTF-8");
        try{
            String action=req.getParameter("action");
            if("status".equals(action)){
                dao.updateOrderStatus(Integer.parseInt(req.getParameter("id")),req.getParameter("status"));
            }else if("add".equals(action)){
                String name=req.getParameter("name"),desc=req.getParameter("description"),category=req.getParameter("category"),priceText=req.getParameter("price"),image=req.getParameter("imageUrl");
                if(name==null||name.isBlank()||name.length()>100||category==null||category.isBlank())throw new IllegalArgumentException("Name and category are required.");
                BigDecimal price=new BigDecimal(priceText);
                if(price.signum()<=0||price.compareTo(new BigDecimal("100000"))>0)throw new IllegalArgumentException("Enter a valid price.");
                dao.addMenuItem(name.trim(),desc,category.trim(),price,image);
            }else if("hide".equals(action)){
                dao.setMenuItemAvailable(Integer.parseInt(req.getParameter("id")),false);
            }
            resp.sendRedirect(req.getContextPath()+"/admin");
        }catch(Exception e){throw new ServletException("Admin action failed: "+e.getMessage(),e);}
    }
}