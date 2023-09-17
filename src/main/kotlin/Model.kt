import java.text.DecimalFormat
import java.util.*
import kotlin.math.roundToInt

class Model {
    var courses: ArrayList<Course> = ArrayList()
    var viewModels = ArrayList<IView>()

    var courseAverage: Double = 0.0
    var coursesTaken: Int = 0
    var coursesFailed: Int = 0
    var coursesWD: Int = 0
    var isWDClicked: Boolean = false

    fun addViewModel(viewModel: IView) {
        viewModels.add(viewModel)
    }

    // Gets the amount of courses taken
    fun getNumCoursesTaken(): Int {
        var numCourses = 0

        for (course in courses) {
            numCourses++
            print(numCourses)
        }

        return numCourses
    }


    // Calculates the grade average from courses "taken"
     fun calcAverage(): Double {
        var numCourses = 0
        var avg: Double = 0.0

        if (courses.isEmpty()) {
            return 0.0
        }

        for (course in courses) {
            if (course.grade == "WD") {
                avg += 0.0
            } else {
                var gradeNum = course.grade.toDouble()
                avg += gradeNum
                numCourses++
            }
        }

        if (avg == 0.0) {
            return 0.0
        }

        return (avg / numCourses)
    }

    // Gets the failed courses from courses "taken"
    fun getFailedCourses(): Int {
        var failedCourses = 0

        for (course in courses) {
            if (course.grade != "WD") {
                var grade = course.grade.toInt()
                if (grade < 50) {
                    failedCourses++
                }
            }
        }
        return failedCourses
    }

    // Calculates the number of courses taken
    fun getNumCoursesWD(): Int {
        var coursesWD = 0

        for (course in courses) {
            if (course.grade == "WD") {
                coursesWD++
            }
        }
        println("WD'd: $coursesWD\n")
        return coursesWD
    }

    fun assignTermRank(term: String) : Int {
        val season = term[0]
        val year = term[2]
        val rankTerm : String = "$season$year"
        println("RANK: $rankTerm")

        if (rankTerm == "F0") {
            return 1
        } else if (rankTerm == "W1") {
            return 2
        } else if (rankTerm == "S1") {
            return 3
        } else if (rankTerm == "F1") {
            return 4
        } else if (rankTerm == "W2") {
            return 5
        } else if (rankTerm == "S2") {
            return 6
        } else if (rankTerm == "F2") {
            return 7
        } else if (rankTerm == "W3") {
            return 8
        } else if (rankTerm == "S3") {
            return 9
        } else if (rankTerm == "F3") {
            return 10
        }
        return 0
    }



    fun sortByTerm(courses: ArrayList<Course>) {
//        val array = viewModel.courses

        for (course in courses) {
            val initTerm = course.term
            val rank = assignTermRank(initTerm)

            course.rank = rank
        }

        courses.sortBy { it.rank }
        println(courses)
    }

    fun update() {
        courseAverage = (calcAverage() * 100.0).roundToInt() / 100.0
        coursesTaken = getNumCoursesTaken()
        coursesFailed = getFailedCourses()
        coursesWD = getNumCoursesWD()
        sortByTerm(courses)

        for (viewModel in viewModels) {
            viewModel.update()
        }
    }
}