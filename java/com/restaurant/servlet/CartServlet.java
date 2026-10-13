package com.restaurant.servlet;

import com.restaurant.model.CartItem;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    @SuppressWarnings("unchecked")
    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        HttpSession session=req.getSession();
        Map<Integer,CartItem> cart=(Map<Integer,CartItem>)session.getAttribute("cart");
        if(cart==null)cart=new LinkedHashMap<>();
        BigDecimal total=BigDecimal.ZERO;
        for(CartItem item:cart.values())total=total.add(item.getSubtotal());
        req.setAttribute("cart",cart.values());req.setAttribute("total",total);
        req.getRequestDispatcher("/cart.jsp").forward(req,resp);
    }
    @SuppressWarnings("unchecked")
    @Override protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws IOException{
        Map<Integer,CartItem> cart=(Map<Integer,CartItem>)req.getSession().getAttribute("cart");
        if(cart!=null){
            try{
                String action=req.getParameter("action");int id=Integer.parseInt(req.getParameter("id"));
                if("remove".equals(action))cart.remove(id);
                else if("update".equals(action)&&cart.containsKey(id)){
                    int qty=Integer.parseInt(req.getParameter("quantity"));
                    if(qty<=0)cart.remove(id);else if(qty<=50)cart.get(id).setQuantity(qty);
                }
            }catch(Exception ignored){}
        }
        resp.sendRedirect(req.getContextPath()+"/cart");
    }
}