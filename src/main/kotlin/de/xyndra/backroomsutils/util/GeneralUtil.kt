package de.xyndra.backroomsutils.util

// idea: -5 % 3 = 1; -5 % 4 = 3
fun positiveModulo(dividend: Int, divisor: Int): Int {
    var result = dividend % divisor
    if (result < 0) {
        result += divisor
    }
    return result
}