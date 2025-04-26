import com.koleff.kare_android.utils.Repeat
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class RepeatRule : TestRule {

    private class RepeatStatement(
        private val statement: Statement,
        private val repeat: Int
    ) : Statement() {
        @Throws(Throwable::class)
        override fun evaluate() {
            repeat(repeat) {
                statement.evaluate()
            }
        }
    }

    override fun apply(base: Statement, description: Description): Statement {
        val repeat = description.getAnnotation(Repeat::class.java)
        return if (repeat != null) {
            RepeatStatement(base, repeat.value)
        } else {
            base
        }
    }
}
