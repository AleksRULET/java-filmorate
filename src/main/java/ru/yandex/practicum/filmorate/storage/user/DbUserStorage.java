package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Component
@Qualifier
public class DbUserStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DbUserStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> findAll() {
        String sqlQuery = "SELECT USER_ID, LOGIN, NAME, EMAIL, BIRTHDAY FROM USERS";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeUser(rs));
    }

    public User create(User user) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        String sqlQuery = "INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY) VALUES(?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            final LocalDate birthday = user.getBirthday();
            if (birthday == null) {
                stmt.setNull(4, Types.DATE);
            } else {
                stmt.setDate(4, Date.valueOf(birthday));
            }
            return stmt;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

        return user;
    }

    public User update(User user) {
        String sqlQuery = "UPDATE USERS SET LOGIN = ?, NAME = ?, EMAIL = ?, BIRTHDAY = ? WHERE USER_ID = ?";
        int i = jdbcTemplate.update(sqlQuery
                , user.getLogin()
                , user.getName()
                , user.getEmail()
                , user.getBirthday()
                , user.getId());
        if (i == 0) {
            throw new ObjectNotFoundException("Такого пользователя не существует");
        }

        return user;
    }

    public Optional<User> findUserById(Long id) {
        final String sqlQuery = "SELECT USER_ID, LOGIN, NAME, EMAIL, BIRTHDAY FROM USERS WHERE USER_ID = ?";
        final List<User> users = jdbcTemplate.query(sqlQuery,  (rs, rowNum) -> makeUser(rs), id);
        if (users.size() != 1) {
            return Optional.empty();
        }

        return Optional.ofNullable(users.get(0));
    }

    static User makeUser(ResultSet rs) throws SQLException {
        return new User(rs.getLong("USER_ID"),
                rs.getString("LOGIN"),
                rs.getString("NAME"),
                rs.getString("EMAIL"),
                rs.getDate("BIRTHDAY").toLocalDate());
    }

    public void addFriend(Long id, Long otherId) {
        String sqlQuery = "INSERT INTO USER_FRIEND (USER_ID, FRIEND_ID) VALUES (?,?)";
        jdbcTemplate.update(sqlQuery
                    , id
                    , otherId
        );
    }

    public List<User> findFriends(Long id) {
        String sqlQuery = "SELECT UU.USER_ID, UU.LOGIN, UU.NAME, UU.EMAIL, UU.BIRTHDAY " +
                "FROM USERS U JOIN USER_FRIEND UF on U.USER_ID = UF.USER_ID " +
                "JOIN USERS UU on UF.FRIEND_ID = UU.USER_ID WHERE U.USER_ID = ?";

        return  jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeUser(rs), id);
    }

    public void delete(Long id) {
        String sqlQuery = "DELETE FROM USERS WHERE USER_ID = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    public List<User> findMutualFriends(Long id, Long otherID) {
        String sqlQuery = "SELECT DISTINCT U.USER_ID, U.LOGIN, U.NAME, U.EMAIL, U.BIRTHDAY FROM USER_FRIEND UF " +
                "JOIN USER_FRIEND UF2 on UF.FRIEND_ID = UF2.FRIEND_ID JOIN USERS U on UF.FRIEND_ID = U.USER_ID "
                + " WHERE UF.USER_ID = (?) and UF2.USER_ID = (?)";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeUser(rs), id, otherID);
    }

    public void deleteFriend(Long id, Long friendID) {
        String sqlQuery = "DELETE FROM USER_FRIEND WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery, id, friendID);
    }
}


