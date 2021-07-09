package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private static UserDaoJDBCImpl instance;

    public UserDaoJDBCImpl() {
    }

    public static UserDaoJDBCImpl getInstance() {
        if (instance == null) {
            instance = new UserDaoJDBCImpl();
        }
        return instance;
    }

    public void createUsersTable() {
        String query = "CREATE TABLE IF NOT EXISTS Users " +
                "(id INT NOT NULL AUTO_INCREMENT, " +
                "name VARCHAR (45) NOT NULL, " +
                "lastName VARCHAR (45) NOT NULL, " +
                "age INT NOT NULL, " +
                "PRIMARY KEY (id));";
        try(Statement statement = Util.getConnection().createStatement()) {
            statement.execute(query);
        } catch (SQLException e) {
            System.out.println("Не удалось создать таблицу");
        }
    }

    public void dropUsersTable() {
        String query = "DROP TABLE IF EXISTS Users;";

        try(Statement statement = Util.getConnection().createStatement()) {
            statement.execute(query);
        } catch (SQLException e) {
            System.out.println("Не удалось удалить таблицу");
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String query = "INSERT INTO users(name, lastName, age) VALUES (?,?,?);";
        try(PreparedStatement statement = Util.getConnection().prepareStatement(query)) {
            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setByte(3, age);
            statement.execute();
            System.out.printf("User с именем – %s добавлен в базу данных\n", name);
        } catch (SQLException e) {
            System.out.printf("Не удалось добавить в базу User с именем – %s\n", name);
        }
    }

    public void removeUserById(long id) {
        String query = "DELETE FROM users WHERE id = ?;";

        try(PreparedStatement statement = Util.getConnection().prepareStatement(query)) {
            statement.setLong(1, id);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("Не удалось удалить пользователяиз БД");
        }
    }

    public List<User> getAllUsers() {
        String query = "SELECT * FROM users";
        List<User> result = new ArrayList<>();

        try(Statement statement = Util.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong(1));
                user.setName(resultSet.getString(2));
                user.setLastName(resultSet.getString(3));
                user.setAge(resultSet.getByte(4));
                result.add(user);
            }
    } catch (SQLException e) {
            System.out.println("Не удалось восстановить всех пользователей из БД");}
        return result;
    }

    public void cleanUsersTable() {
        String query = "DELETE FROM users";

        try(Statement statement = Util.getConnection().createStatement()) {
            statement.execute(query);
        }catch (SQLException e) {
            System.out.println("Не удалось очистить таблицу");
        }
    }
}
