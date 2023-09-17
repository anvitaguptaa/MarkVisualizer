import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.control.*
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.scene.layout.BorderPane
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.text.Font
import kotlin.math.PI

var canvasOutcomes = Canvas()
var gcOutcomes = canvasOutcomes.graphicsContext2D
class OutcomesVisual (val viewModel: Model) : BorderPane(), IView {
    var includeMissing = CheckBox("Include Missing Courses")
    fun getCoursesCount(courses: ArrayList<Course>): Map<String, Int> {
        val gradesCount = mutableMapOf(
            "WD" to 0,
            "Failed" to 0,
            "Low" to 0,
            "Good" to 0,
            "Great" to 0,
            "Excellent" to 0,
            "Missing" to 0
        )

        for (course in courses) {
            var grade = course.grade

            if (grade == "WD") {
                gradesCount["WD"] = gradesCount["WD"]!! + 1
//                courseLists["WD"]!!.plus(course.courseCode)
            } else if (grade.toInt() in 0..49) {
                gradesCount["Failed"] = gradesCount["Failed"]!! + 1
//                courseLists["Failed"]!!.plus(course.courseCode)
            } else if (grade.toInt() in 50..59) {
                gradesCount["Low"] = gradesCount["Low"]!! + 1
//                courseLists["Low"]!!.plus(course.courseCode)
            } else if (grade.toInt() in 60..90) {
                gradesCount["Good"] = gradesCount["Good"]!! + 1
//                courseLists["Good"]!!.plus(course.courseCode)
            } else if (grade.toInt() in 91..95) {
                gradesCount["Great"] = gradesCount["Great"]!! + 1
//                courseLists["Great"]!!.plus(course.courseCode)
            } else if (grade.toInt() in 96..100) {
                gradesCount["Excellent"] = gradesCount["Excellent"]!! + 1
//                courseLists["Excellent"]!!.plus(course.courseCode)
            } else {
                return gradesCount
            }
        }

        if (includeMissing.isSelected) {
            gradesCount["Missing"] = (40 - gradesCount["Excellent"]!! - gradesCount["Great"]!!
                    - gradesCount["Good"]!! - gradesCount["Low"]!! - gradesCount["Failed"]!!
                    - gradesCount["WD"]!!)
        }

        return gradesCount
    }

    fun calcAngles(grades: Map<String, Int>): Map<String, Double> {
        val totalCourses = grades.values.sum()
        val angles = mutableMapOf<String, Double>()

        for (entry in grades) {
            val category = entry.key
            val count = entry.value
            val angle = if (totalCourses > 0) count.toDouble() / totalCourses * 360.0 else 0.0
            angles[category] = angle
        }

        return angles
    }


    fun gcApply() {
        var grades = getCoursesCount(viewModel.courses)

        var anglesCategories = calcAngles(grades)
        var angles = anglesCategories.values.toDoubleArray()

        gcOutcomes.apply {
            clearRect(0.0, 0.0, canvas.width, canvas.height)

            val centerX = (canvasOutcomes.width + 50) / 2
            val centerY = (canvasOutcomes.height + 20) / 2
            val radius = Math.min(centerX, centerY) * 0.8
            var startAngle = 0.0

            for ((index, angle) in angles.withIndex()) {
                val percentage = angle / angles.sum()
                val endAngle = startAngle + percentage * 360.0

                val color = when(index) {
                    0 -> Color.DARKSLATEGREY
                    1 -> Color.LIGHTCORAL
                    2 -> Color.LIGHTBLUE
                    3 -> Color.LIGHTGREEN
                    4 -> Color.SILVER
                    5 -> Color.GOLD
                    6 -> Color.WHITE
                    else -> Color.BLACK
                }
                fill = color

                fillArc(
                    centerX - radius, centerY - radius, radius * 2, radius * 2, startAngle, angle, javafx.scene.shape.ArcType.ROUND
                )

                stroke = Color.BLACK
                lineWidth = 1.0
                startAngle = endAngle
            }

//            var hoverSegment = -1
//            setOnMouseMoved { event ->
//                val x = event.x
//                val y = event.y
//                val dx = x - centerX
//                val dy = y - centerY
//                val angle = Math.toDegrees(Math.atan2(dy, dx))
//                val segmentIndex = ((angle - startAngle) / 360 * angles.size).toInt()
//            }

        }

    }

    init {
        includeMissing.setOnAction {
            gcApply()
        }

        this.widthProperty().addListener { _, _, newWidth ->
            canvasOutcomes.width = newWidth.toDouble() - 465.0
            gcApply()
        }

        this.heightProperty().addListener { _, _, newHeight ->
            canvasOutcomes.height = newHeight.toDouble() - 35.0

            gcApply()
        }

        val holder = HBox()
        val spacer1 = Region()
        val spacer2 = Region()
        spacer2.prefWidth = 420.0
        holder.children.addAll(spacer1, includeMissing, spacer2)
        holder.alignment = javafx.geometry.Pos.CENTER
        holder.padding = Insets(0.0, 0.0, 17.0, 0.0)

        this.bottom = holder
        this.left = canvasOutcomes
        viewModel.addViewModel(this)

    }

    override fun update() {
        gcApply()
    }
}