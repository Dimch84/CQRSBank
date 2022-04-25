package client.api.requests

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import client.api.requests.RequestType.*

fun sendToUrl(url: String, data: Map<String, Any?>, type: RequestType = POST): String {
    val con: HttpURLConnection = when(type) {
        POST -> (URL(url).openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            setRequestProperty("Content-Type", "application/json; utf-8")
            setRequestProperty("Accept", "application/json")
            doOutput = true
            outputStream.use { os ->
                val input = ObjectMapper().writeValueAsString(data).toByteArray(charset("utf-8"))
                os.write(input, 0, input.size)
            }
        }
        GET -> (URL(url).openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            doOutput = true
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
