package client.api.requests

import client.api.requests.RequestType.GET
import client.api.requests.RequestType.POST
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.xml.bind.DatatypeConverter


fun sendToUrl(url: String, data: Map<String, Any?>, type: RequestType = POST, login: String?=null, password: String?=null): String {
    val con: HttpURLConnection = when(type) {
        POST -> (URL(url).openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            setRequestProperty("Content-Type", "application/json; utf-8")
            setRequestProperty("Accept", "application/json")
            if (login != null && password != null)
                setRequestProperty("Authorization", "Basic " + DatatypeConverter.printBase64Binary("$login:$password".toByteArray()))
            doOutput = true
            outputStream.use { os ->
                val input = ObjectMapper().writeValueAsString(data).toByteArray(charset("utf-8"))
                os.write(input, 0, input.size)
            }
        }
        GET -> (URL(url).openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            if (login != null && password != null)
                setRequestProperty("Authorization", "Basic " + DatatypeConverter.printBase64Binary("$login:$password".toByteArray()))
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
