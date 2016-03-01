package com.theironyard;

import jodd.json.JsonParser;
import jodd.json.JsonSerializer;
import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    static HashMap<String, User> users = new HashMap<>();

    public static void main(String[] args) throws FileNotFoundException {

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


}


//        Create page that shows a list of all created users.
//        Make the user page 10 users long.
//        Link each user to a page that displays their win/loss information.
//        If logged in, give the ability to add/edit/delete win/loss information of current user.
//        Add logout button.
//        *Save information to a text file.
//        *Parse saved information.

//        Choose something you'd like to "track" in a web app.
//        It could be physical objects (beer, books, etc), but doesn't have to be.

//        User authentication
//        If not logged in, show a login form at the top
//        (it can double as your create account form, like in the ForumWeb project).

//        If logged in, display the username and a logout button at the top.
//        Create: If logged in, display a form to create a new entry.

//        Read: Whether logged in or not,
//        list whatever entries were created by the users.

//        Update: If logged in, show an edit link next to the entries created by that user.
//        Display the edit form on a new page and use a hidden field to specify which item to edit.

//        Delete: If logged in, show a delete button next to the entries created by that user.
//        Clicking it should delete the item and refresh the page.
//        Use a hidden field to specify which item to delete.

//        Compile the project as a JAR file and upload it to the "Releases" section of your repo.
//        Optional: Add paging to your list of entries.
//        Optional: Add CSS (served statically via a public folder).