package com.theironyard;

import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.sql.*;
import java.util.HashMap;


public class Main {
    static HashMap<String, User> users = new HashMap<>();

    public static void main(String[] args) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        createTables(conn);
        Spark.init();

        Spark.get(
                "/",
                ((request, response) -> {
                    User user = getUserFromSession(request.session());
                    HashMap m = new HashMap();
                    if (user == null) {
                        return new ModelAndView(m, "login.html");
                    } else {
                        m.put("name", user.userName);
                        m.put("stats", user.stats);

                    }
                    return new ModelAndView(m, "home.html");

                }),
                new MustacheTemplateEngine()
        );
        Spark.post(
                "create-user",
                ((request, response) -> {
                    String userName = request.queryParams("userName");
                    String userPass = request.queryParams("userPass");
                    User user = users.get(userName);
                    if (userName.equals("") || userPass.equals("")) {
                        response.redirect("/");
                    } else if (user == null) {
                        user = new User(userName, userPass);
                        users.put(userName, user);
                        user.userPass = userPass;
                        Session session = request.session();
                        session.attribute("userName", userName);
                    } else if (userPass.equals(user.userPass)) {
                        response.redirect("/");
                        Session session = request.session();
                        session.attribute("userName", userName);
                    } else {
                        Spark.halt(403);
                    }
                    response.redirect("/");

                    return "";
                })
        );
        Spark.post(
                "/logout",
                ((request, response) -> {
                    Session session = request.session();
                    session.invalidate();
                    response.redirect("/");
                    return "";
                })
        );
        Spark.post(
                "/create-record",
                ((request, response) -> {
                    User user = getUserFromSession(request.session());
                    String userCharacter = request.queryParams("Character");
                    String opponentCharacter = request.queryParams("opponentCharacter");
                    String winLoss = request.queryParams("Win/Loss");
                    if (userCharacter == null || opponentCharacter == null || winLoss == null) {
                        throw new Exception("Didn't receive all parameters.");
                    }
                    Stat stat = new Stat(userCharacter, opponentCharacter, winLoss);
                    user.stats.add(stat);

                    response.redirect("/");
                    return "";
                })
        );
        Spark.post(
                "/delete",
                ((request, response) -> {
                    User user = getUserFromSession(request.session());
                    int number = Integer.valueOf(request.queryParams("userDelete"));
                    user.stats.remove(number - 1);
                    response.redirect("/");
                    return "";
                })
        );

        Spark.post(
                "/edit",
                ((request, response) -> {
                    User user = getUserFromSession(request.session());
                    int number = Integer.valueOf(request.queryParams("userEdit"));
                    String userCharEdit = request.queryParams("editChar");
                    String userOppEdit = request.queryParams("editOpponentCharacter");
                    String userWinEdit = request.queryParams("editWin/Loss");
                    if(userCharEdit == null || userOppEdit == null || userWinEdit == null){
                        response.redirect("/");
                    }
                    Stat statEdit = new Stat(userCharEdit, userOppEdit, userWinEdit);
                    user.stats.set(number - 1, statEdit);
                    response.redirect("/");
                    return "";
                })
        );

    }


    static User getUserFromSession(Session session) {
        String name = session.attribute("userName");
        return users.get(name);
    }


    public static void createTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS users (id IDENTITY, name VARCHAR, password VARCHAR)");
        stmt.execute("CREATE TABLE IF NOT EXISTS stats (id IDENTITY, user_character VARCHAR, opponent_character VARCHAR," +
                "win_loss VARCHAR )");
    }
    public static void insertUser(Connection conn, String name, String password) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES (NULL, ?, ?)");
        stmt.setString(1, name);
        stmt.setString(2, password);
        stmt.execute();
    }
    public static User selectUser(Connection conn, String name) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE name = ?");
        stmt.setString(1, name);
        ResultSet results = stmt.executeQuery();
        if(results.next()){
            int id = results.getInt("id");
            String password = results.getString("Password");
            return new User(id, name, password);

        }
        return null;
    }
}
