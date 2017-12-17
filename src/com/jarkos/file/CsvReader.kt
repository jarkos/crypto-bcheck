package com.jarkos.file

import au.com.bytecode.opencsv.CSVReader
import ta4jexamples.loaders.CsvTradesLoader
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.text.Charsets.UTF_8

class CsvReader {

    fun getLines(filePath: String): List<*>? {
        var csvReader: CSVReader? = null
        var lines: List<*>? = null
        try {
            val stream = FileInputStream(filePath)

            csvReader = CSVReader(InputStreamReader(stream, UTF_8), ',')
            lines = csvReader.readAll()
        } catch (fnfe: FileNotFoundException) {
            Logger.getLogger(CsvTradesLoader::class.java.name).log(Level.WARNING, "Unable to load trades from CSV. Create new file.", fnfe)
        } finally {
            if (csvReader != null) {
                try {
                    csvReader.close()
                } catch (ignored: IOException) {
                }
            }
        }
        return ArrayList(lines)
    }
}
