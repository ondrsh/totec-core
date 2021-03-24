
inline fun repeatAndMeasure(times: Long, action: (Long) -> Unit): Long {
	val start = System.currentTimeMillis()
	for (index in 0 until times) {
		action(index)
	}
	return System.currentTimeMillis() - start
}

fun Long.println(name: String) {
	kotlin.io.println("$name took $this ms")
}

