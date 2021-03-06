package org.jetbrains.dataframe.benchmarks

import org.jetbrains.dataframe.DataFrame
import org.jetbrains.dataframe.DataFrameBase
import org.jetbrains.dataframe.DataRowBase
import org.jetbrains.dataframe.filter
import org.jetbrains.dataframe.filterFast
import org.jetbrains.dataframe.io.read
import org.jetbrains.dataframe.neq
import org.jetbrains.dataframe.typed
import org.junit.Test
import kotlin.system.measureTimeMillis

class FilterTests {

    val path = "data/census-clean.csv"
    val df = DataFrame.read(path)

    interface DataRecord {
        val Referer: String?
    }

    val DataFrameBase<DataRecord>.Referer: org.jetbrains.dataframe.api.columns.DataColumn<kotlin.String?> @JvmName("DataRecord_Referer") get() = this["Referer"] as org.jetbrains.dataframe.api.columns.DataColumn<kotlin.String?>
    val DataRowBase<DataRecord>.Referer: String? @JvmName("DataRecord_Referer") get() = this["Referer"] as String?

    val typed = df.typed<DataRecord>()

    val n = 100

    @Test
    fun slow() {
        measureTimeMillis {
            for (i in 0..n)
                typed.filter { Referer != null }
        }.let { println(it) }
    }

    @Test
    fun fast() {
        measureTimeMillis {
            for (i in 0..n)
                typed.filterFast { Referer neq null }
        }.let { println(it) }
    }
}