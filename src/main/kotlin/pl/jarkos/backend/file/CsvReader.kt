package pl.jarkos.backend.file

import com.opencsv.CSVReader
import org.springframework.stereotype.Component
import java.io.*
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.text.Charsets.UTF_8

@Component
class CsvReader {

    fun getLines(filePath: String): ArrayList<Array<String>> {
        var csvReader: CSVReader? = null
        var lines: MutableList<Array<String>>? = null
        try {
            val file = File(filePath)
            if (!file.isFile) {
                file.createNewFile()
            }
            val stream = FileInputStream(filePath)
            csvReader = CSVReader(InputStreamReader(stream, UTF_8), ',')
            lines = csvReader.readAll()
        } catch (fnfe: FileNotFoundException) {
            Logger.getLogger(CsvReader::class.java.name).log(Level.WARNING, "Unable to load trades from CSV. Create new file.", fnfe)

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
