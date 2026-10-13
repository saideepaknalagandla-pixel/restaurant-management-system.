package com.restaurant.dao;

import com.restaurant.model.MenuItem;
import com.restaurant.util.DBConnection;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RestaurantDAO {
    public List<MenuItem> getAvailableMenu() throws SQLException {
        String sql="SELECT id,name,description,category,price,image_url FROM menu_items WHERE available=TRUE ORDER BY category,name";
        List<MenuItem> items=new ArrayList<>();
        try(Connection c=DBConnection.getConnection(); PreparedStatement ps=c.prepareStatement(sql); ResultSet rs=ps.executeQuery()){
            while(rs.next()) items.add(new MenuItem(rs.getInt("id"),rs.getString("name"),rs.getString("description"),rs.getString("category"),rs.getBigDecimal("price"),rs.getString("image_url")));
        }
        return items;
    }
    public MenuItem findMenuItem(int id) throws SQLException {
        try(Connection c=DBConnection.getConnection(); PreparedStatement ps=c.prepareStatement("SELECT id,name,description,category,price,image_url FROM menu_items WHERE id=? AND available=TRUE")){
            ps.setInt(1,id);
            try(ResultSet rs=ps.executeQuery()){if(rs.next()) return new MenuItem(rs.getInt("id"),rs.getString("name"),rs.getString("description"),rs.getString("category"),rs.getBigDecimal("price"),rs.getString("image_url"));}
        }
        return null;
    }
    public int placeOrder(String name, String email, Map<Integer, Integer> cart) throws SQLException {
        if(cart.isEmpty()) throw new SQLException("Your cart is empty.");
        try(Connection c=DBConnection.getConnection()){
            c.setAutoCommit(false);
            try {
                BigDecimal total=BigDecimal.ZERO;
                List<MenuItem> items=new ArrayList<>();
                for(var entry:cart.entrySet()){
                    if(entry.getValue()==null || entry.getValue()<1 || entry.getValue()>50) throw new SQLException("Invalid item quantity.");
                    MenuItem item=findMenuItemUsingConnection(c,entry.getKey());
                    if(item==null) throw new SQLException("A menu item is no longer available.");
                    items.add(item);
                    total=total.add(item.getPrice().multiply(BigDecimal.valueOf(entry.getValue())));
                }
                int orderId;
                try(PreparedStatement ps=c.prepareStatement("INSERT INTO restaurant_orders(customer_name,customer_email,total) VALUES(?,?,?)",Statement.RETURN_GENERATED_KEYS)){
                    ps.setString(1,name);ps.setString(2,email);ps.setBigDecimal(3,total);ps.executeUpdate();
                    try(ResultSet keys=ps.getGeneratedKeys()){if(!keys.next()) throw new SQLException("Could not create order.");orderId=keys.getInt(1);}
                }
                try(PreparedStatement ps=c.prepareStatement("INSERT INTO order_items(order_id,menu_item_id,item_name,quantity,unit_price) VALUES(?,?,?,?,?)")){
                    int i=0;
                    for(var entry:cart.entrySet()){
                        MenuItem item=items.get(i++);
                        ps.setInt(1,orderId);ps.setInt(2,item.getId());ps.setString(3,item.getName());ps.setInt(4,entry.getValue());ps.setBigDecimal(5,item.getPrice());ps.addBatch();
                    }
                    ps.executeBatch();
                }
                c.commit(); return orderId;
            } catch(Exception e){c.rollback();if(e instanceof SQLException se) throw se;throw new SQLException(e);}
            finally {c.setAutoCommit(true);}
        }
    }
    private MenuItem findMenuItemUsingConnection(Connection c,int id) throws SQLException {
        try(PreparedStatement ps=c.prepareStatement("SELECT id,name,description,category,price,image_url FROM menu_items WHERE id=? AND available=TRUE")){
            ps.setInt(1,id);try(ResultSet rs=ps.executeQuery()){if(rs.next())return new MenuItem(rs.getInt("id"),rs.getString("name"),rs.getString("description"),rs.getString("category"),rs.getBigDecimal("price"),rs.getString("image_url"));}
        }return null;
    }
    public List<String[]> getRecentOrders() throws SQLException {
        List<String[]> rows=new ArrayList<>();
        String sql="SELECT id,customer_name,customer_email,total,status,DATE_FORMAT(created_at,'%d-%b-%Y %H:%i') created FROM restaurant_orders ORDER BY id DESC LIMIT 100";
        try(Connection c=DBConnection.getConnection();PreparedStatement ps=c.prepareStatement(sql);ResultSet rs=ps.executeQuery()){
            while(rs.next()) rows.add(new String[]{rs.getString("id"),rs.getString("customer_name"),rs.getString("customer_email"),rs.getBigDecimal("total").toPlainString(),rs.getString("status"),rs.getString("created")});
        }return rows;
    }
    public void updateOrderStatus(int id,String status) throws SQLException {
        if(!List.of("PLACED","PREPARING","READY","DELIVERED","CANCELLED").contains(status)) throw new SQLException("Invalid order status.");
        try(Connection c=DBConnection.getConnection();PreparedStatement ps=c.prepareStatement("UPDATE restaurant_orders SET status=? WHERE id=?")){
            ps.setString(1,status);ps.setInt(2,id);ps.executeUpdate();
        }
    }
    public void addMenuItem(String name,String description,String category,BigDecimal price,String imageUrl) throws SQLException {
        try(Connection c=DBConnection.getConnection();PreparedStatement ps=c.prepareStatement("INSERT INTO menu_items(name,description,category,price,image_url) VALUES(?,?,?,?,?)")){
            ps.setString(1,name);ps.setString(2,description);ps.setString(3,category);ps.setBigDecimal(4,price);ps.setString(5,imageUrl);ps.executeUpdate();
        }
    }
    public void setMenuItemAvailable(int id,boolean available) throws SQLException {
        try(Connection c=DBConnection.getConnection();PreparedStatement ps=c.prepareStatement("UPDATE menu_items SET available=? WHERE id=?")){
            ps.setBoolean(1,available);ps.setInt(2,id);ps.executeUpdate();
        }
    }
}