package organization;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class AppLauncher extends Application {

    private ConfigurableApplicationContext context;

    @Override
    public void init() {
        context = new SpringApplicationBuilder(TrailersApplication.class).run();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/MainLayout.fxml"));
        loader.setControllerFactory(context::getBean); //is getting bean instead of "new MAinController

        stage.setTitle("Wypożyczalnia przyczep");
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }

    @Override
    public void stop() {
        context.close();
    }
}
