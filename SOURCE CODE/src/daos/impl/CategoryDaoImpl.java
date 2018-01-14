package daos.impl;

import daos.CategoryDao;
import db.DBManager;
import main.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CategoryDaoImpl implements CategoryDao {
    private Connection con;

    public CategoryDaoImpl() {
        this.con = DBManager.getCon();
    }

    @Override
    public ArrayList<Category> getCategories() {
        ArrayList <Category> categories = new ArrayList<>();
        try {
            PreparedStatement stm = con.prepareStatement("SELECT * FROM category");
            ResultSet rs = stm.executeQuery();
            categories = extractCategoriesFromResultSet(rs);
            return categories;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    private ArrayList<Category> extractCategoriesFromResultSet(ResultSet rs) {
        ArrayList<Category> categories = new ArrayList<>();
        try {
            while(rs.next()){
                Category c = new Category();
                c.setCategoryName(rs.getString("CategoryName"));
                //System.out.println("Categoria: " + c.getCategoryName());
                categories.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }
}
