package lt.itsvaidas.DataAPI;

import lt.itsvaidas.ZCAPI.MysqlConfig;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class DataMysql<T> {

    private final Class<T> classType;
    private final Plugin plugin;
    private final String table;
    private Connection connection;

    public DataMysql(Plugin plugin, String name, Class<T> classType) {
        this.plugin = plugin;
        this.classType = classType;

        String url = MysqlConfig.getUrl();
        String database = MysqlConfig.getDatabase();
        String username = MysqlConfig.getUsername();
        String password = MysqlConfig.getPassword();

        this.table = plugin.getName().toLowerCase() + "_" + name.toLowerCase();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to connect to the database: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            plugin.getLogger().severe("MySQL JDBC Driver not found: " + e.getMessage());
        }

        if (this.connection != null) {
            plugin.getLogger().info("Connected to the database successfully.");
        } else {
            plugin.getLogger().severe("Failed to establish a connection to the database.");
            return;
        }

        try {
            connection.createStatement().execute("CREATE SCHEMA IF NOT EXISTS `" + database + "`");
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to create schema: " + e.getMessage());
        }

        try {
            connection.createStatement().execute("CREATE TABLE IF NOT EXISTS `" + table + "` (id INT AUTO_INCREMENT PRIMARY KEY)");
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to create table: " + e.getMessage());
        }

        Set<String> existingColumns = new HashSet<>();
        try {
            String query = "SHOW COLUMNS FROM `" + table + "`";
            var resultSet = connection.createStatement().executeQuery(query);
            while (resultSet.next()) {
                existingColumns.add(resultSet.getString("Field"));
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to check columns: " + e.getMessage());
        }

        Field[] fields = classType.getClass().getDeclaredFields();

        for (Field field : fields) {
            String fieldName = field.getName();
            if (fieldName.equalsIgnoreCase("id") || existingColumns.contains(fieldName)) continue;

            String sqlType = getSqlType(field.getType());

            String alterQuery = "ALTER TABLE `" + table + "` ADD COLUMN `" + fieldName + "` " + sqlType;
            try {
                connection.createStatement().execute(alterQuery);
                plugin.getLogger().info("Added missing column: " + fieldName + " (" + sqlType + ")");
            } catch (SQLException e) {
                plugin.getLogger().severe("Failed to add column `" + fieldName + "`: " + e.getMessage());
            }
        }
    }

    private String getSqlType(Class<?> type) {
        if (type == String.class) return "VARCHAR(255)";
        if (type == int.class || type == Integer.class) return "INT";
        if (type == long.class || type == Long.class) return "BIGINT";
        if (type == double.class || type == Double.class) return "DOUBLE";
        if (type == boolean.class || type == Boolean.class) return "BOOLEAN";
        if (type == Date.class || type == java.sql.Date.class) return "DATETIME";
        return "TEXT"; // Default fallback
    }

    public void add(T data) {
        Field[] fields = data.getClass().getDeclaredFields();
        List<String> columns = new ArrayList<>();
        List<String> placeholders = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        for (Field field : fields) {
            String name = field.getName();
            if (name.equalsIgnoreCase("id")) continue; // skip id on insert
            field.setAccessible(true);
            try {
                Object value = field.get(data);
                if (value != null) {
                    columns.add("`" + name + "`");
                    placeholders.add("?");
                    values.add(value);
                }
            } catch (IllegalAccessException ignored) {}
        }

        String sql = "INSERT INTO `" + table + "` (" + String.join(", ", columns) + ") VALUES (" + String.join(", ", placeholders) + ")";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < values.size(); i++) {
                stmt.setObject(i + 1, values.get(i));
            }
            stmt.executeUpdate();
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                setField(data, "id", id);
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to insert data: " + e.getMessage());
        }
    }

    public void remove(int id) {
        String sql = "DELETE FROM `" + table + "` WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to delete by id: " + e.getMessage());
        }
    }

    public void remove(T data) {
        Object id = getField(data, "id");
        if (id instanceof Integer) {
            remove((Integer) id);
        } else {
            plugin.getLogger().severe("Cannot delete: Invalid ID in object.");
        }
    }

    public T get(int id) {
        String sql = "SELECT * FROM `" + table + "` WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return resultSetToObject(rs);
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to fetch by id: " + e.getMessage());
        }
        return null;
    }

    public Set<T> get(Map<String, String> conditions) {
        Set<T> results = new HashSet<>();
        StringBuilder query = new StringBuilder("SELECT * FROM `" + table + "` WHERE 1=1");
        for (String key : conditions.keySet()) {
            query.append(" AND `").append(key).append("` = ?");
        }

        try (PreparedStatement stmt = connection.prepareStatement(query.toString())) {
            int index = 1;
            for (String value : conditions.values()) {
                stmt.setString(index++, value);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(resultSetToObject(rs));
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to fetch by conditions: " + e.getMessage());
        }

        return results;
    }

    public void update(T data) {
        Object id = getField(data, "id");
        if (!(id instanceof Integer)) {
            plugin.getLogger().severe("Cannot update: Invalid or missing ID.");
            return;
        }

        Field[] fields = data.getClass().getDeclaredFields();
        List<String> sets = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        for (Field field : fields) {
            String name = field.getName();
            if (name.equalsIgnoreCase("id")) continue;
            field.setAccessible(true);
            try {
                Object value = field.get(data);
                sets.add("`" + name + "` = ?");
                values.add(value);
            } catch (IllegalAccessException ignored) {}
        }

        String sql = "UPDATE `" + table + "` SET " + String.join(", ", sets) + " WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < values.size(); i++) {
                stmt.setObject(i + 1, values.get(i));
            }
            stmt.setObject(values.size() + 1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to update data: " + e.getMessage());
        }
    }

    public Set<T> getAdvanced(String rawConditions, List<Object> values) {
        Set<T> results = new HashSet<>();
        String sql = "SELECT * FROM `" + table + "` WHERE " + rawConditions;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < values.size(); i++) {
                stmt.setObject(i + 1, values.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(resultSetToObject(rs));
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed advanced query: " + e.getMessage());
        }

        return results;
    }

    private T resultSetToObject(ResultSet rs) {
        try {
            T obj = (T) classType.getClass().getDeclaredConstructor().newInstance();
            ResultSetMetaData meta = rs.getMetaData();
            for (int i = 1; i <= meta.getColumnCount(); i++) {
                String colName = meta.getColumnName(i);
                Object value = rs.getObject(colName);
                setField(obj, colName, value);
            }
            return obj;
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to convert ResultSet to object: " + e.getMessage());
            return null;
        }
    }

    private void setField(T obj, String fieldName, Object value) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to set field '" + fieldName + "': " + e.getMessage());
        }
    }

    private Object getField(T obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to get field '" + fieldName + "': " + e.getMessage());
            return null;
        }
    }

    public static <T> DataMysql<T> load(Plugin plugin, String name, Class<T> classType) {
        return new DataMysql<>(plugin, name, classType);
    }
}
