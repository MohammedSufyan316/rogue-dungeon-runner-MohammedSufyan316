module com.example.team_trenches_rogue_game {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires javafx.graphics;

    opens trench_rogue_game to javafx.fxml;
    exports trench_rogue_game;
    exports trench_rogue_game.player;
    opens trench_rogue_game.player to javafx.fxml;
}