package com.martin.plain

import com.netflix.hystrix.exception.HystrixRuntimeException
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate
import java.util.concurrent.Future

fun main(args: Array<String>)
{
    val commands = mutableListOf<Future<String>>()
    val poolingHttpClientConnectionManager = PoolingHttpClientConnectionManager()
    poolingHttpClientConnectionManager.maxTotal = 50
    poolingHttpClientConnectionManager.defaultMaxPerRoute = 20
    val httpClient = HttpClients.custom().setConnectionManager(poolingHttpClientConnectionManager).build()

    val httpComponentsClientHttpRequestFactory = HttpComponentsClientHttpRequestFactory(httpClient)

    httpComponentsClientHttpRequestFactory.setReadTimeout(8000)

    val restClient = RestTemplate(httpComponentsClientHttpRequestFactory)

    println(poolingHttpClientConnectionManager.totalStats)


    repeat(10)
    {
        commands.add(HttpCommand(restClient, "http://getstatuscode.com/200").queue())
        Thread.sleep(1000)
        println(poolingHttpClientConnectionManager.totalStats)
    }

    commands.forEachIndexed { index, command ->
        try
        {
            command.get()?.takeIf { it.isNotBlank() } ?. takeWhile { it != '\n' } ?. also { println("$index: $it") }
        } catch(e: HystrixRuntimeException)
        {
            println(e.failureType)
        } catch(e: Exception)
        {
            val cause = e.cause as HystrixRuntimeException
            println(cause.failureType)
        }
    }
}
