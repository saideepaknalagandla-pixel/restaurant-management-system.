package com.restaurant.servlet;

import com.restaurant.dao.RestaurantDAO;
import com.restaurant.model.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebServlet("/cart/add")
public class AddToCartServlet extends HttpServlet {
    private final RestaurantDAO dao=new RestaurantDAO();
    @SuppressWarnings("unchecked")
    @Override protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws IOException{
        try{
            int id=Integer.parseInt(req.getParameter("id"));
            MenuItem item=dao.findMenuItem(id);
            if(item==null){resp.sendError(404,"Menu item not found.");return;}
            HttpSession session=req.getSession();
            Map<Integer,CartItem> cart=(Map<Integer,CartItem>)session.getAttribute("cart");
            if(cart==null){cart=new LinkedHashMap<>();session.setAttribute("cart",cart);}
            CartItem existing=cart.get(id);
            if(existing==null)cart.put(id,new CartItem(item,1));
            else if(existing.getQuantity()<50)existing.setQuantity(existing.getQuantity()+1);
            resp.sendRedirect(req.getContextPath()+"/cart");
        }catch(NumberFormatException e){resp.sendError(400,"Invalid menu item.");}
        catch(Exception e){resp.sendError(500,"Could not add item to cart. Check database configuration.");}
    }
}