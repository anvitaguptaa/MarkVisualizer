import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.control.*
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.scene.layout.Pane
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.stage.Stage
import javafx.scene.Group
import javafx.beans.binding.Bindings


class Visualization (val viewModel: Model) : TabPane(), IView {

    init {
        this.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE)
        val termTab = Tab("Average by Term")
        val degreeTab = Tab("Progress Towards Degree")
        val outcomesTab = Tab("Course Outcomes")
        val incrementalTab = Tab("Incremental Average")

        val termView = TermVisual(viewModel)
        val degreeView = DegreeVisual(viewModel)
        val outcomesView = OutcomesVisual(viewModel)
        val incrementalView = IncrementalVisual(viewModel)

        termTab.content = termView
        degreeTab.content = degreeView
        outcomesTab.content = outcomesView
        incrementalTab.content = incrementalView

        termTab.setStyle("-fx-font-size: 12px;")
        degreeTab.setStyle("-fx-font-size: 12px;")
        outcomesTab.setStyle("-fx-font-size: 12px;")
        incrementalTab.setStyle("-fx-font-size: 12px;")

        this.getTabs().addAll(termTab, degreeTab, outcomesTab, incrementalTab)

        viewModel.addViewModel(this)
    }

    override fun update() {

//        println(this.height)
    }
}