module lab13 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.scripting;
    requires jdk.dynalink;
    requires java.base;
//    requires spring.boot.autoconfigure;
//    requires spring.context;
//    requires spring.boot;
//    requires spring.beans;
//    requires spring.ai.core;
//    requires spring.ai.openai;

    opens pl.e_science.git.ivahan0788 to javafx.fxml;
    exports pl.e_science.git.ivahan0788;
}