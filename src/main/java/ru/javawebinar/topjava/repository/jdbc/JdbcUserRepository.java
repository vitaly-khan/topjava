package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static UserResultSetExtractor urse = new UserResultSetExtractor();

    private static class UserResultSetExtractor implements ResultSetExtractor<List<User>> {
        @Override
        public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<User> list = new ArrayList<>();
            while (rs.next()) {
                int currentUserId = rs.getInt("id");
                User user = list.stream().filter(x -> x.getId() == currentUserId).findFirst().orElse(null);
                Role role = Role.valueOf(rs.getString("role"));

                if (user == null) {
                    user = new User(currentUserId,
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getInt("calories_per_day"),
                            rs.getBoolean("enabled"),
                            rs.getDate("registered"),
                            EnumSet.of(role));
                    list.add(user);
                } else {
                    user.getRoles().add(role);
                }
            }
            return list;
        }
    }

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());

        } else {
            if (namedParameterJdbcTemplate.update(
                    "UPDATE users SET name=:name, email=:email, password=:password, registered=:registered, " +
                            "enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
                return null;
            }
            jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.getId());
        }
        jdbcTemplate.batchUpdate(
                "INSERT INTO user_roles (user_id, role) values(?,?)",
                new BatchPreparedStatementSetter() {

                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, user.getId());
                        ps.setString(2, new ArrayList<>(user.getRoles()).get(i).name());
                    }

                    public int getBatchSize() {
                        return user.getRoles().size();
                    }
                });
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> list = jdbcTemplate.query("SELECT * FROM users JOIN user_roles " +
                "ON users.id = user_roles.user_id WHERE users.id=?", urse, id);
        return DataAccessUtils.singleResult(list);
    }

    @Override
    public User getByEmail(String email) {
        List<User> list = jdbcTemplate.query("SELECT * FROM users JOIN user_roles " +
                "ON users.id = user_roles.user_id WHERE email=?", urse, email);
        return DataAccessUtils.singleResult(list);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users JOIN user_roles " +
                "ON users.id = user_roles.user_id ORDER BY name, email", urse);
    }
}
