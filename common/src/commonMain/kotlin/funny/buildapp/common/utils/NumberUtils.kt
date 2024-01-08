package funny.buildapp.common.utils

public fun Double.toFraction(): Double {
    if (this.isNaN()) return 0.0
    val digital = this.toString().split(".")
    return "${digital[0]}.${
        digital[1].subSequence(
            0,
            if (digital[1].length > 2) 2 else digital[1].length
        )
    }".toDouble()
}