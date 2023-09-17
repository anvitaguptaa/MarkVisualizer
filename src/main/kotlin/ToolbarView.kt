import javafx.beans.binding.Bindings
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.scene.control.*
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import java.util.*
import kotlin.collections.ArrayList
import javafx.scene.layout.Pane

val sortOptions = listOf("Course Code", "Term", "Grade (Ascending)", "Grade (Descending)")
val filterOptions = listOf("None", "CS Courses", "MATH Courses", "Non-CS/MATH Courses")

class ToolbarView (val viewModel: Model) : VBox(), IView {
//  TOP TOOLBAR
    val topToolbar = ToolBar()
    val sortLabel = Label("Sort By:   ")
    val sortChoice = ComboBox(FXCollections.observableList(sortOptions))
    val sortSeperator = Separator(Orientation.VERTICAL)
    val filterLabel = Label("Filter By:   ")
    val filterChoice = ComboBox(FXCollections.observableList(filterOptions))
    val filterSeperator = Separator(Orientation.VERTICAL)
    val wdCheckBox = CheckBox("Include WD")

//  BOTTOM TOOLBAR
    val bottomToolbar = ToolBar()
    var courseCode = TextField()
//    var courseName = TextField("Course Name")
    val termOptions = listOf("F20", "W21", "S21", "F21", "W22", "S22", "F22", "W23", "S23", "F23")
    var term = ComboBox(FXCollections.observableList(termOptions))
    var grade = TextField()
    val createButton = Button("Create")
    val spacer = Region()
    val spacerTwo = Region()
    val holder = HBox()
    var spacerThree = Pane()

    // Filters values in course array
    fun filterArray(str: String): ArrayList<Course> {
        val array = ArrayList<Course>()
        for (course in viewModel.courses) {
            val code = course.courseCode.uppercase()
            if (str == "CS") {
                if (code.startsWith(str)) {
                    array.add(course)
                }
            } else if (str == "MATH") {
                if (code.startsWith(str) ||
                    code.startsWith("CO") ||
                    code.startsWith("STAT")) {
                    array.add(course)
                }
            } else if (str == "Non") {
                if (!(code.startsWith("MATH") ||
                            code.startsWith("CO") ||
                            code.startsWith("STAT") ||
                            code.startsWith("CS"))) {
                    array.add(course)
                }
            }
        }
        return array
    }

    init {
//        var initCourses = viewModel.courses
//        var filteredArray: ArrayList<Course>

        createButton.onAction = EventHandler {
            var course = Course(courseCode.text, term.value, grade.text, 0)
            viewModel.courses += course

            for (item in viewModel.courses) {
                println(item.courseCode + " " + item.term + " " + item.grade)
            }
            viewModel.update()
        }

        holder.children.addAll(
            spacer, courseCode, term, grade, spacerThree, createButton, spacerTwo
        )


        courseCode.prefWidthProperty().bind(Bindings.divide(holder.widthProperty(), 6))
        term.prefWidthProperty().bind(Bindings.divide(holder.widthProperty(), 5.5))
        grade.prefWidthProperty().bind(Bindings.divide(holder.widthProperty(), 8))
        HBox.setHgrow(bottomToolbar, Priority.ALWAYS)
        HBox.setHgrow(holder, Priority.ALWAYS)
        HBox.setHgrow(spacerThree, Priority.ALWAYS)
//        bottomToolbar.prefWidthProperty().bind(this.widthProperty())
        bottomToolbar.padding = Insets(8.0, 8.0, 8.0, 8.0)
        holder.style = "-fx-background-color : #cfcfcf; -fx-background-radius: 10 10 10 10;"
        holder.padding = Insets(8.0, 5.0, 8.0, 5.0)
        holder.spacing = 10.0
        bottomToolbar.items.add(holder)

//        bottomToolbar.setPrefWidth(200.0)
        this.children.addAll(bottomToolbar)
        viewModel.addViewModel(this)
    }

    override fun update() {
//        courseName.text = "Course Name"
        courseCode.clear()
        term.valueProperty().set(null)
        grade.clear()
    }
}