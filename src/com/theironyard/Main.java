package com.theironyard;

import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;

public class Main {

    public static void main(String[] args) {


        Spark.init();

        Spark.get(
                "/",
                ((request, response) -> {
                    HashMap m = new HashMap();
                    return new ModelAndView(m, "login.html");
                }),
                new MustacheTemplateEngine()
        );




    }
}


//        Create login page.
//        Display name of current logged in user.
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