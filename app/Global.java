import java.util.List;

import models.*;

import com.avaje.ebean.Ebean;

import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.libs.Yaml;

public class Global extends GlobalSettings {
    @Override
    public void onStart(Application app) {
        Logger.info("Application " + app.toString() + " startup...");

        // Check if the database is empty and if yes load the test user data.
        if (models.User.find.findRowCount() == 0) {
            Ebean.save((List) Yaml.load("test-user-data.yml"));
        }
    }

    @Override
    public void onStop(Application app) {
        Logger.info("Application " + app.toString() + " shutdown...");
    }
}
