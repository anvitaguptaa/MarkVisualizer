import javafx.geometry.Orientation
import javafx.scene.control.Label
import javafx.scene.control.Separator
import javafx.scene.control.ToolBar
import javafx.scene.layout.HBox

class StatusbarView (val viewModel: Model) : ToolBar(), IView {
    val courseAverage = Label("Course Average: ${viewModel.courseAverage}")
    val averageSeparator = Separator(Orientation.VERTICAL)
    val courseCount = Label("Courses Taken: ${viewModel.coursesTaken}")
    val coursesSeparator = Separator(Orientation.VERTICAL)
    val coursesFailed = Label("Courses Failed: ${viewModel.coursesFailed}")
    val coursesSeparatorTwo = Separator(Orientation.VERTICAL)
    val coursesWD = Label("Courses WD'ed: ${viewModel.coursesWD}")

    init {
        this.items.addAll(
            courseAverage, averageSeparator, courseCount, coursesSeparator, coursesFailed, coursesSeparatorTwo, coursesWD
        )

        viewModel.addViewModel(this)
    }

    override fun update() {
        courseAverage.text = "Course Average: ${viewModel.courseAverage}"
        courseCount.text = "Courses Taken: ${viewModel.coursesTaken}"
        coursesFailed.text = "Courses Failed: ${viewModel.coursesFailed}"
        coursesWD.text = "Courses WD'ed: ${viewModel.coursesWD}"

//        coursesSeparatorTwo.isVisible = viewModel.isWDClicked
//        coursesWD.isVisible = viewModel.isWDClicked
    }
}