package database;

import java.sql.Connection;

public class DBTest
{
    public static void main(String[] args)
    {
        try (Connection connection = DBConnection.getConnection())
        {
            System.out.println("Connected to database!");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}