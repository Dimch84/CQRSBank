package client.api.requests

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

fun sendToUrl(url: String, data: Map<String, Any>): String {
    val requestBody: String = ObjectMapper().writeValueAsString(data)
    val con: HttpURLConnection = (URL(url).openConnection() as HttpURLConnection).apply {
        requestMethod = "POST"
        setRequestProperty("Content-Type", "application/json; utf-8")
        setRequestProperty("Accept", "application/json")
        doOutput = true
        outputStream.use { os ->
            val input = requestBody.toByteArray(charset("utf-8"))
            os.write(input, 0, input.size)
        }
    }
    return BufferedReader(InputStreamReader(con.inputStream, "utf-8")).use { br ->
        val response = StringBuilder()
        var responseLine: String?
        while (br.readLine().also { responseLine = it } != null) {
            response.append(responseLine!!)
        }
        response.toString()
    }
}
