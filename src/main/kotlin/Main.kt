import Model
import CoursesView
import ToolbarView
import StatusbarView
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import javafx.scene.layout.*
//import javafx.scene.layout.ScrollPane
import javafx.scene.control.SplitPane
import javafx.scene.control.*
import javafx.stage.Stage



class Main : Application() {
    override fun start(stage: Stage) {
        val model = Model()
        val adminSection = BorderPane()
        adminSection.setPrefWidth(420.0)

        val visualization = Visualization(model)
        visualization.prefWidthProperty().bind(stage.widthProperty())

        val root = BorderPane()

        val courses = CoursesView(model)
        adminSection.top = ToolbarView(model)
        adminSection.center = courses

        root.left = adminSection
        root.bottom = StatusbarView(model)
        root.right = visualization

        stage.scene = Scene(root)
        stage.setMinWidth(900.0);
        stage.setMinHeight(450.0);
        stage.title = "Mark Visualization"
        stage.show()
    }
}