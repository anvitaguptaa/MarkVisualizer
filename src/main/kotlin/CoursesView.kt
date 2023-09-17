//import StatusbarView
import javafx.beans.binding.Bindings
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.control.*
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.scene.layout.Pane

import java.util.Comparator
import java.util.Arrays

// Returns the color to be used for each coursebar
fun getColor(grade: String): String {
    if (grade == "WD") {
        return "darkslategray"
    }
    var gradeInt = grade.toInt()
    return if (gradeInt < 50) {
        "lightcoral"
    } else if (gradeInt in 50..59) {
        "lightblue"
    } else if (gradeInt in 60..90) {
        "lightgreen"
    } else if (gradeInt in 91..95) {
        "silver"
    } else {
        "gold"
    }
}

// Creates a scrollpane to display added courses
class CoursesView (val viewModel: Model) : ScrollPane(), IView {
    init {
        viewModel.addViewModel(this)
    }

    // Creates a bar holding course information from toolbar input
    fun createCourseBar(course: Course): ToolBar {
//        val initName = course.courseName
        val initGrade = course.grade
        val initTerm = course.term
        val courseBar = ToolBar()
        val courseCode = TextField(course.courseCode)
        courseCode.isEditable = false
//        val courseName = TextField(initName)
        val termOptions = listOf("F20", "W21", "S21", "F21", "W22", "S22", "F22", "W23", "S23", "F23")
        var term = ComboBox(FXCollections.observableList(termOptions))
        term.value = initTerm
        val grade = TextField(initGrade)
        val spacer = Region()
        val deleteButton = Button("Delete")
        val updateButton = Button("Update")
        updateButton.isDisable = true
        var colorString = "-fx-background-color : ${getColor(course.grade)}; -fx-background-radius: 10 10 10 10;"
        val undoButton = Button("Undo")
        undoButton.isVisible = false
        var spacerTwo = Pane()
        var spacerThree = Region()
        HBox.setHgrow(spacerTwo, Priority.ALWAYS)



        val holder = HBox()
        holder.children.addAll(
            spacer, courseCode, term, grade, spacerTwo, updateButton, deleteButton, spacerThree

        )

        // Deletes a course from the view
        deleteButton.onAction = EventHandler {
            viewModel.courses.remove(course)
            viewModel.update()
        }

        // Checks for updated term value
        term.onAction = EventHandler {
            updateButton.isDisable = false
            course.term = term.value


            holder.children.removeAll(deleteButton, spacerThree)
            if (!holder.children.contains(undoButton)) {
                holder.children.addAll(undoButton)
            }
            if (!holder.children.contains(spacerThree)) {
                holder.children.addAll(spacerThree)
            }

            undoButton.isVisible = true
        }

        // Checks for updated grade value
        grade.textProperty().addListener { _, _, newValue ->
            updateButton.isDisable = false
            course.grade = newValue

            holder.children.removeAll(deleteButton, spacerThree)
            if (!holder.children.contains(undoButton)) {
                holder.children.addAll(undoButton)
            }
            if (!holder.children.contains(spacerThree)) {
                holder.children.addAll(spacerThree)
            }
            undoButton.isVisible = true
            colorString = "-fx-background-color : ${getColor(newValue)}; -fx-background-radius: 10 10 10 10;"
        }

        // Checks for update
        updateButton.onMouseClicked = EventHandler {
            holder.style = colorString
            term.style = "-fx-opacity: 3; -fx-text-fill: black;-fx-background-color: white"

            holder.children.removeAll(undoButton, spacerThree)
            holder.children.addAll(deleteButton, spacerThree)
            viewModel.update()

        }

        // Implements undo after update
        undoButton.onAction = EventHandler {
            updateButton.isDisable = true

            grade.text = initGrade
            term.value = initTerm

            colorString = "-fx-background-color : ${getColor(grade.text)}; -fx-background-radius: 10 10 10 10;"
            holder.style = colorString

            holder.children.removeAll(undoButton, spacerThree)
            holder.children.addAll(deleteButton, undoButton, spacerThree)
            undoButton.isVisible = false
        }

//        // Styling
        courseCode.prefWidthProperty().bind(Bindings.divide(holder.widthProperty(), 6))
        term.prefWidthProperty().bind(Bindings.divide(holder.widthProperty(), 5.5))
        grade.prefWidthProperty().bind(Bindings.divide(holder.widthProperty(), 8))
        courseBar.padding = Insets(8.0, 8.0, 8.0, 8.0)
        holder.style = colorString
        holder.padding = Insets(8.0, 5.0, 8.0, 5.0)
        holder.spacing = 10.0
        courseBar.items.add(holder)
        HBox.setHgrow(holder, Priority.ALWAYS)

        return courseBar

    }



    override fun update() {
        var coursesBox = VBox()
        var courses = viewModel.courses

        for (course in courses) {
                val courseBar = createCourseBar(course)
                coursesBox.children.add(courseBar)
//            }
        }

        coursesBox.prefWidthProperty().bind(this.widthProperty())
        this.content = coursesBox
    }
}