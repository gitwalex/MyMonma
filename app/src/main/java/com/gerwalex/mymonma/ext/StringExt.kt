package com.gerwalex.mymonma.ext

/**
 * Laedt eine Class anhand des Namens und Typs
 *
 * @param className name der Class
 * @param type      Typ der Klasse. Kann auch Object oder ein Interface sein
 * @return Klasse, die den Parametern entspricht.
 */
fun <T> String.instantiate(type: Class<T>): T {
    return try {
        type.cast(Class.forName(this).newInstance()) as T
    } catch (e: InstantiationException) {
        throw IllegalStateException(e)
    } catch (e: IllegalAccessException) {
        throw IllegalStateException(e)
    } catch (e: ClassNotFoundException) {
        throw IllegalStateException(e)
    }
}
