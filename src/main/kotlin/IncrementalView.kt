import javafx.beans.binding.Bindings
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


var canvasIncremental = Canvas()
var gcIncremental = canvasIncremental.graphicsContext2D

class IncrementalVisual (val viewModel: Model) : Pane(), IView {
    fun getYCoordinate(grade: Int) : Double {
        var multiplier = ""
        var lastDigit = grade % 10
        if (grade == 0) {
            multiplier += "-10"
        } else if (grade in 1..10) {
            multiplier += "10"
            if (grade < 10) {
                multiplier += ".${10 - lastDigit}"
            }
        } else if (grade in 11..20) {
            multiplier += "9"
            if (grade < 20) {
                multiplier += ".${10 - lastDigit}"
            }
        } else if (grade in 21..30) {
            multiplier += "8"
            if (grade < 30) {
                multiplier += ".${10 - lastDigit}"
            }
        } else if (grade in 31..40) {
            multiplier += "7"
            if (grade < 40) {
                multiplier += ".${10 - lastDigit}"
            }
        } else if (grade in 41..50) {
            multiplier += "6"
            if (grade < 50) {
                multiplier += ".${10 - lastDigit}"
            }
        } else if (grade in 51..60) {
            multiplier += "5"
            if (grade < 60) {
                multiplier += ".${10 - lastDigit}"
            }
        } else if (grade in 61..70) {
            multiplier += "4"
            if (grade < 70) {
                multiplier += ".${10 - lastDigit}"
            }
        } else if (grade in 71..80) {
            multiplier += "3"
            if (grade < 80) {
                multiplier += ".${10 - lastDigit}"
            }
        } else if (grade in 81..90) {
            multiplier += "2"
            if (grade < 90) {
                multiplier += ".${10 - lastDigit}"
            }
        } else {
            if (grade < 100) {
                multiplier += "1"
                multiplier += ".${10 - lastDigit}"
            } else if (grade == 100) {
                multiplier += "1"
            } else if (grade > 100) {
                multiplier += "0.${10 - lastDigit}"
            }
        }
        return multiplier.toDouble()
    }
    fun getTermAverages(courses: ArrayList<Course>):  Map<String, Int> {
        val termDict = mutableMapOf<String, Int>()
        val coursesCount = mutableMapOf<String, Int>()

        for (course in courses) {
            val term = course.term
            var grade = course.grade

            if (term !in termDict) {
                termDict[term] = 0
            }

            if (term !in coursesCount) {
                coursesCount[term] = 0
            }

            if (grade != "WD") {
                termDict[term] = termDict.getOrDefault(term, 0) + grade.toInt()
                coursesCount[term] = coursesCount.getOrDefault(term, 0) + 1
            } else {
                termDict[term] = termDict.getOrDefault(term, 0) + 0
                coursesCount[term] = coursesCount.getOrDefault(term, 0) + 0
            }
        }

        for ((key, value) in termDict) {
            val courseCount = coursesCount[key]
            var averageGrade : Int = value
            if (averageGrade != 0) {
                averageGrade /= courseCount ?: 1
            } else {
                averageGrade = 0
            }
            termDict[key] = averageGrade
        }

        return termDict
    }

    fun calcStdDev(grades: IntArray): Double {
        val size = grades.size
        val mean = grades.average()
        val sumOfSquares = grades.map { (it - mean) * (it - mean) }.sum()

        println("STD DEVIATION IS ${Math.sqrt(sumOfSquares / (size - 1))}")
        return ((Math.sqrt(sumOfSquares / (size - 1)))) // mean -
    }

    fun gcIncrementalApply() {
        gcIncremental.apply {
            fill = Color.BLACK
            clearRect(0.0, 0.0, canvasIncremental.width, canvasIncremental.height)
            var height = canvasIncremental.height - 20

            stroke = Color.gray(0.85)
            for (i in 1..10) {
                strokeLine(50.0, (height / 11) * i - 4, canvasIncremental.width, (height / 11) * i - 4)
            }

            stroke = Color.BLACK
            lineWidth = 2.0
            strokeLine(50.0, canvasIncremental.height - 20, canvasIncremental.width, canvasIncremental.height - 20) // X Axis
            strokeLine(50.0, canvasIncremental.height / 15, 50.0, canvasIncremental.height) // Y Axis

            font = Font("Arial", 12.0)
            fillText("0", 25.0, height)
            fillText("10", 25.0, (height / 11) * 10)
            fillText("20", 25.0, (height / 11) * 9)
            fillText("30", 25.0, (height / 11) * 8)
            fillText("40", 25.0, (height / 11) * 7)
            fillText("50", 25.0, (height / 11) * 6)
            fillText("60", 25.0, (height / 11) * 5)
            fillText("70", 25.0, (height / 11) * 4)
            fillText("80", 25.0, (height / 11) * 3)
            fillText("90", 25.0, (height / 11) * 2)
            fillText("100", 20.0, height / 11)

            update()
        }
    }

    init {
        var termDict = getTermAverages(viewModel.courses)
        var xAxisValues: List<String> = mutableListOf()

        for ((key, value) in termDict) {
            xAxisValues += key
        }

        this.widthProperty().addListener { _, _, newWidth ->
            canvasIncremental.width = newWidth.toDouble() - 465.0

            gcIncrementalApply()
        }

        this.heightProperty().addListener { _, _, newHeight ->
            canvasIncremental.height = newHeight.toDouble() - 35.0
            var termDict = getTermAverages(viewModel.courses)
            var xAxisValues: List<String> = mutableListOf()

            gcIncrementalApply()
        }

        this.children.add(canvasIncremental)
        viewModel.addViewModel(this)

    }

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

    fun getTermGrades(courses: ArrayList<Course>): Map<String, Array<Int>> {
        val termGrades = mutableMapOf(
            "F20" to emptyArray<Int>(),
            "W21" to emptyArray<Int>(),
            "S21" to emptyArray<Int>(),
            "F21" to emptyArray<Int>(),
            "W22" to emptyArray<Int>(),
            "S22" to emptyArray<Int>(),
            "F22" to emptyArray<Int>(),
            "W23" to emptyArray<Int>(),
            "S23" to emptyArray<Int>(),
            "F23" to emptyArray<Int>()
        )

        for (course in courses) {
            if (course.grade != "WD") {
                termGrades[course.term] = termGrades[course.term]!! + course.grade.toInt()
            }
        }

        for ((key, value) in termGrades) {
            println("$key: ${value.contentToString()}")
        }

        return termGrades
    }
    override fun update() {
        var termDict = getTermAverages(viewModel.courses)
        var xAxisValues: List<String> = mutableListOf()
        var termGrades = getTermGrades(viewModel.courses)
        var counter = 50.0

        gcIncremental.apply {
            fill = Color.BLACK
            for ((key, value) in termDict) {
                xAxisValues += key
            }

            clearRect(0.0, 0.0, canvasIncremental.width, canvasIncremental.height)
            var height = canvasIncremental.height - 20

            stroke = Color.gray(0.85)
            for (i in 1..10) {
                strokeLine(50.0, (height / 11) * i - 4, canvasIncremental.width, (height / 11) * i - 4)
            }

            stroke = Color.BLACK
            lineWidth = 2.0
            strokeLine(50.0, canvasIncremental.height - 20, canvasIncremental.width, canvasIncremental.height - 20) // X Axis
            strokeLine(50.0, canvasIncremental.height / 15, 50.0, canvasIncremental.height - 20.0) // Y Axis

//            fill = Color.BLACK
            font = Font("Arial", 12.0)
            fillText("0", 25.0, height)
            fillText("10", 25.0, (height / 11) * 10)
            fillText("20", 25.0, (height / 11) * 9)
            fillText("30", 25.0, (height / 11) * 8)
            fillText("40", 25.0, (height / 11) * 7)
            fillText("50", 25.0, (height / 11) * 6)
            fillText("60", 25.0, (height / 11) * 5)
            fillText("70", 25.0, (height / 11) * 4)
            fillText("80", 25.0, (height / 11) * 3)
            fillText("90", 25.0, (height / 11) * 2)
            fillText("100", 20.0, height / 11)

            // Create oval for average value and deviation line
            for (i in 0 until xAxisValues.size) {
                fill = Color.BLACK
                fillText(xAxisValues[i], (((canvasIncremental.width - 30) / xAxisValues.size) * i) + 50.0, canvasIncremental.height)

                var avgYCoordinate = getYCoordinate(termDict[xAxisValues[i]]!!.toInt())
                var color = getColor(termDict[xAxisValues[i]]!!.toString())
                fill = Color.web(color)
                lineWidth = 1.0
                stroke = Color.BLACK

                var maxGrade = termGrades[xAxisValues[i]]!!.maxOrNull()
                var minGrade = termGrades[xAxisValues[i]]!!.minOrNull()
                var yMaxCoordinate = getYCoordinate(maxGrade!!.toInt())
                var yMinCoordinate = getYCoordinate(minGrade!!.toInt())

                val standardDeviation = calcStdDev(termGrades[xAxisValues[i]]!!.toIntArray())
                val mean =  termGrades[xAxisValues[i]]!!.average()
                val meanCoord = getYCoordinate(mean.toInt())
                val maxDevCoord = getYCoordinate(mean.toInt() - standardDeviation.toInt())
                val minDevCoord = getYCoordinate(mean.toInt() + standardDeviation.toInt())

                for (grade in termGrades[xAxisValues[i]]!!) {
                    if (grade != maxGrade || grade != minGrade) {
                        var gradeColor = getColor(grade.toString())
                        var gradeYCoordinate = getYCoordinate(grade.toInt())
                        stroke = Color.web(gradeColor)
                        lineWidth = 1.0

                        strokeOval(
                            (((canvasIncremental.width - 30) / xAxisValues.size) * i) + 56.0,
                            (height / 11) * gradeYCoordinate - 10,
                            10.0,
                            10.0
                        )

                        stroke = Color.BLACK

                        // Min Std dev
                        strokeLine (
                            (((canvasIncremental.width - 30) / xAxisValues.size) * i) + 56.0,
                            (height / 11) * maxDevCoord - 5,
                            (((canvasIncremental.width - 30) / xAxisValues.size) * i) + 66.0,
                            (height / 11) * maxDevCoord - 5
                        )

                        // Max Std dev
                        strokeLine(
                            (((canvasIncremental.width - 30) / xAxisValues.size) * i) + 56.0,
                            (height / 11) * minDevCoord - 5,
                            (((canvasIncremental.width - 30) / xAxisValues.size) * i) + 66.0,
                            (height / 11) * minDevCoord - 5
                        )

                        strokeLine(
                            (((canvasIncremental.width - 30) / xAxisValues.size) * i) + 61.0,
                            (height / 11) * maxDevCoord - 5,
                            (((canvasIncremental.width - 30) / xAxisValues.size) * i) + 61.0,
                            (height / 11) * minDevCoord - 5
                        )
                    }
                }
                strokeOval((((canvasIncremental.width - 30) / xAxisValues.size) * i) + 56.0, (height / 11 ) * avgYCoordinate - 10, 10.0, 10.0)
                fillOval((((canvasIncremental.width - 30) / xAxisValues.size) * i) + 56.0, (height / 11 ) * avgYCoordinate - 10, 10.0, 10.0)
            }
        }
    }

}