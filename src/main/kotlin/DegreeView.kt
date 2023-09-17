import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.control.*
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.scene.layout.Pane
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.text.Font

var canvasDegree = Canvas()
var gcDegree = canvasDegree.graphicsContext2D

class DegreeVisual (val viewModel: Model) : Pane(), IView {

    // Check the number of credits completed for each category
    fun getNumCredits(courses: ArrayList<Course>): Map<String, Double> {
        var progress = mutableMapOf(
            "CS" to 0.0,
            "MATH" to 0.0,
            "OTHER" to 0.0,
            "TOTAL" to 0.0
        )

        for (course in courses) {
            if (course.grade != "WD") {
                if (course.grade.toInt() >= 50) {
                    val code = course.courseCode.uppercase()
                    if (code.startsWith("CS")) {
                        progress["CS"] = progress["CS"]!! + 0.5

                    } else if (code.startsWith("MATH") ||
                        code.startsWith("CO") ||
                        code.startsWith("STAT")
                    ) {
                        progress["MATH"] = progress["MATH"]!! + 0.5
                    } else if (!(code.startsWith("MATH") ||
                                code.startsWith("CO") ||
                                code.startsWith("STAT") ||
                                code.startsWith("CS"))
                    ) {
                        progress["OTHER"] = progress["OTHER"]!! + 0.5
                    }
                }
            }
        }

        progress["TOTAL"] = progress["OTHER"]!! + progress["MATH"]!! + progress["CS"]!!
        return progress
    }


    fun gcApply() {
        var courseProgresses = getNumCredits(viewModel.courses)
        gcDegree.apply {
            clearRect(0.0, 0.0, canvasDegree.width, canvasDegree.height)
            stroke = Color.BLACK
            lineWidth = 2.0
            font = Font("Arial", 12.0)
            var height = canvasDegree.height - 20

            // YELLOW rectangle [11.0]
            fill = Color.YELLOW.deriveColor(1.0, 1.0, 1.0, 0.2)
            fillRect((canvasDegree.width - 10) / 5, (canvasDegree.height / 5) - 10, ((canvasDegree.width - 10) / 5) * 2.2, 50.0)

            // PINK RECTANGLE [4.0]
            fill = Color.PINK.deriveColor(1.0, 1.0, 1.0, 0.2)
            fillRect((canvasDegree.width - 10) / 5, (canvasDegree.height / 5) * 2 - 10.0, ((canvasDegree.width - 10) / 5) * 0.8, 50.0)

            // GRAY rectangle [10.0]
            fill = Color.GRAY.deriveColor(1.0, 1.0, 1.0, 0.2)
            fillRect((canvasDegree.width - 10) / 5, (canvasDegree.height / 5) * 3 - 10.0, ((canvasDegree.width - 10) / 5) * 2, 50.0)

            // GREEN rectangle [20.0]
            fill = Color.GREEN.deriveColor(1.0, 1.0, 1.0, 0.2)
            fillRect((canvasDegree.width - 10) / 5, (canvasDegree.height / 5) * 4 - 10.0, ((canvasDegree.width - 10) / 5) * 4, 50.0)

            // PROGRESSES
            // MATH
            fill = Color.YELLOW
            var multiplier = (courseProgresses["CS"]!! * 2) / 10
            fillRect((canvasDegree.width - 10) / 5, (canvasDegree.height / 5) - 10, ((canvasDegree.width - 10) / 5) * multiplier, 50.0)
            fill = Color.BLUE

            fill = Color.PINK
            multiplier = (courseProgresses["MATH"]!! * 2) / 10
            fillRect((canvasDegree.width - 10) / 5, (canvasDegree.height / 5) * 2 - 10.0, ((canvasDegree.width - 10) / 5) * multiplier, 50.0)

            fill = Color.GRAY
            multiplier = (courseProgresses["OTHER"]!! * 2) / 10
            fillRect((canvasDegree.width - 10) / 5, (canvasDegree.height / 5) * 3 - 10.0, ((canvasDegree.width - 10) / 5) * multiplier, 50.0)

            fill = Color.GREEN
            multiplier = (courseProgresses["TOTAL"]!! * 2) / 10
            fillRect((canvasDegree.width - 10) / 5, (canvasDegree.height / 5) * 4 - 10.0, ((canvasDegree.width - 10) / 5) * multiplier, 50.0)

            fill = Color.BLACK

            // 1 [0.0]
            strokeLine((canvasDegree.width - 10) / 5, 50.0, (canvasDegree.width - 10) / 5, height) // 1 [0.0]
            fillText("0.0", (canvasDegree.width - 50) / 5, canvasDegree.height)
            fillText("CS", 15.0, (height / 5) + 25)

            // 2 [5.0]
            strokeLine(((canvasDegree.width - 10) / 5) * 2, 50.0, ((canvasDegree.width - 10) / 5) * 2, height)
            fillText("5.0", ((canvasDegree.width - 35) / 5) * 2, canvasDegree.height)
            fillText("MATH", 15.0, (height / 5) * 2 + 30)

            // 3 [10.0]
            strokeLine(((canvasDegree.width - 10) / 5) * 3, 50.0, ((canvasDegree.width - 10) / 5) * 3, height) // 2 10
            fillText("10.0", ((canvasDegree.width - 35) / 5) * 3, canvasDegree.height)
            fillText("OTHER", 15.0, (height / 5) * 3 + 30)

            // 4 [15.0]
            strokeLine(((canvasDegree.width - 10) / 5) * 4, 50.0, ((canvasDegree.width - 10) / 5) * 4, height)
            fillText("15.0", ((canvasDegree.width - 30) / 5) * 4, canvasDegree.height)
            fillText("TOTAL", 15.0, (height / 5) * 4 + 35)

            // 5 [20.0]
            strokeLine(((canvasDegree.width - 10) / 5) * 5, 50.0, ((canvasDegree.width - 10) / 5) * 5, height)
            fillText("20.0", ((canvasDegree.width - 25) / 5) * 5, canvasDegree.height)
        }
    }
    init {
        this.widthProperty().addListener { _, _, newWidth ->
            println("New width is: $newWidth")
            canvasDegree.width = newWidth.toDouble() - 465.0

            gcApply()
        }

        this.heightProperty().addListener { _, _, newHeight ->
            println("New width is: $newHeight")
            canvasDegree.height = newHeight.toDouble() - 35.0

            gcApply()
        }

        this.children.add(canvasDegree)
        viewModel.addViewModel(this)

    }

    override fun update() {
//        println(getNumCredits(viewModel.courses))
        gcApply()
    }
}