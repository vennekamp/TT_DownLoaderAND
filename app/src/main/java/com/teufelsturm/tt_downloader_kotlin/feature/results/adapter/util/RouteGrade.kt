package com.teufelsturm.tt_downloader_kotlin.feature.results.adapter.util

private val arrayOfRouteGrades = arrayOf(
    // RouteGrade.NA,
    RouteGrade.MIN,
    RouteGrade.I,
    RouteGrade.II,
    RouteGrade.III,
    RouteGrade.IV,
    RouteGrade.V,
    RouteGrade.VI,
    RouteGrade.VIIa,
    RouteGrade.VIIb,
    RouteGrade.VIIc,
    RouteGrade.VIIIa,
    RouteGrade.VIIIb,
    RouteGrade.VIIIc,
    RouteGrade.IXa,
    RouteGrade.IXb,
    RouteGrade.IXc,
    RouteGrade.Xa,
    RouteGrade.Xb,
    RouteGrade.Xc,
    RouteGrade.XIa,
    RouteGrade.XIb,
    RouteGrade.XIc,
    RouteGrade.XIIa,
    RouteGrade.XIIb,
    RouteGrade.XIIc,
    RouteGrade.XIIIa,
    RouteGrade.Sprung1,
    RouteGrade.Sprung2,
    RouteGrade.Sprung3,
    RouteGrade.Sprung4,
    RouteGrade.MAX
)

val float2Grade: ((Float) -> String) =
    {
        arrayOfRouteGrades.firstOrNull { grade -> grade.ordinal == it.toInt() }?.text
            ?: "keine"
    }

fun getRouteGradeByOrdinal(ordinal: Int): String =
    arrayOfRouteGrades.firstOrNull { it.ordinal == ordinal }?.text ?: "keine"


private var mOrdinal = 0

sealed class RouteGrade(val grade: Int, val text: String, val ordinal: Int) {
//    object NA : RouteGrade(-1, "keine", mOrdinal++)
    object MIN : RouteGrade(0, "min", mOrdinal++)
    object I : RouteGrade(10, "I", mOrdinal++)
    object II : RouteGrade(20, "II", mOrdinal++)
    object III : RouteGrade(30, "III", mOrdinal++)
    object IV : RouteGrade(40, "IV", mOrdinal++)
    object V : RouteGrade(50, "V", mOrdinal++)
    object VI : RouteGrade(60, "VI", mOrdinal++)
    object VIIa : RouteGrade(71, "VIIa", mOrdinal++)
    object VIIb : RouteGrade(72, "VIIb", mOrdinal++)
    object VIIc : RouteGrade(73, "VIIc", mOrdinal++)
    object VIIIa : RouteGrade(81, "VIIIa", mOrdinal++)
    object VIIIb : RouteGrade(82, "VIIIb", mOrdinal++)
    object VIIIc : RouteGrade(83, "VIIIC", mOrdinal++)
    object IXa : RouteGrade(91, "IXa", mOrdinal++)
    object IXb : RouteGrade(92, "IXb", mOrdinal++)
    object IXc : RouteGrade(93, "IXc", mOrdinal++)
    object Xa : RouteGrade(101, "Xa", mOrdinal++)
    object Xb : RouteGrade(102, "Xb", mOrdinal++)
    object Xc : RouteGrade(103, "Xc", mOrdinal++)
    object XIa : RouteGrade(111, "XIa", mOrdinal++)
    object XIb : RouteGrade(112, "XIb", mOrdinal++)
    object XIc : RouteGrade(113, "XIc", mOrdinal++)
    object XIIa : RouteGrade(121, "XIIa", mOrdinal++)
    object XIIb : RouteGrade(122, "XIIb", mOrdinal++)
    object XIIc : RouteGrade(123, "XIIc", mOrdinal++)
    object XIIIa : RouteGrade(130, "XIIIa", mOrdinal++)
    object Sprung1 : RouteGrade(151, "Sp1", mOrdinal++)
    object Sprung2 : RouteGrade(152, "Sp2", mOrdinal++)
    object Sprung3 : RouteGrade(153, "Sp3", mOrdinal++)
    object Sprung4 : RouteGrade(154, "Sp4", mOrdinal++)
    object MAX : RouteGrade(999, "max", mOrdinal++)
    companion object {
        fun values() = arrayOfRouteGrades

        fun getMinOrdinal(): Int = values().minOf { it -> it.ordinal}
        fun getMaxOrdinal(): Int = values().maxOf { it -> it.ordinal}
        // fun getMax(): Float = values().maxOf { it -> it.grade}.toFloat()

        fun getRouteGradeByOrdinal(value: Int?): Int? =
            values().firstOrNull { it -> it.ordinal == value }?.grade

        fun getOrdinalByRouteGrade(value: Int?): Int? =
            values().firstOrNull { it -> it.grade == value }?.ordinal

        fun getRouteGradeByString(value: String?): RouteGrade? =
            values().firstOrNull { it -> it.text == value }

        val float2Grade: ((Float) -> String) =
            { value ->
                values().firstOrNull { grade -> grade.ordinal == value.toInt() }?.text
                    ?: "keine"
            }
    }
}
