package repository;

import database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TraceabilityRepositoryImpl
        implements TraceabilityRepository
{

    @Override
    public List<String> getRegistrationNumbersForProduct(int productId)
    {
        List<String> animals = new ArrayList<>();

        String sql = """
            SELECT DISTINCT a.registration_number
            FROM Animal a

            JOIN Part part
            ON a.animal_id = part.animal_id

            JOIN Tray t
            ON part.tray_id = t.tray_id

            JOIN ProductTray pt
            ON t.tray_id = pt.tray_id

            WHERE pt.product_id = ?;
            """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        )
        {
            statement.setInt(1, productId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next())
            {
                animals.add(
                        resultSet.getString("registration_number"));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return animals;
    }

    @Override
    public List<Integer> getProductIdsForAnimal(int animalId)
    {
        List<Integer> products = new ArrayList<>();

        String sql = """
            SELECT DISTINCT p.product_id
            FROM Product p

            JOIN ProductTray pt
            ON p.product_id = pt.product_id

            JOIN Tray t
            ON pt.tray_id = t.tray_id

            JOIN Part part
            ON t.tray_id = part.tray_id

            WHERE part.animal_id = ?;
            """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        )
        {
            statement.setInt(1, animalId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next())
            {
                products.add(resultSet.getInt("product_id"));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return products;
    }
}