package com.martin.plain

import com.netflix.hystrix.HystrixCommand
import com.netflix.hystrix.HystrixCommandProperties
import com.netflix.hystrix.HystrixThreadPoolProperties
import org.springframework.web.client.RestTemplate
import java.net.URI

class HttpCommand(val restClient : RestTemplate, val url : String) : HystrixCommand<String>
(
        Setter.withGroupKey({ "Hello" }).andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                .withExecutionTimeoutInMilliseconds(2000).withCircuitBreakerEnabled(false))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(20005))
)
{
    override fun run(): String = restClient.getForObject(URI(url), String::class.java)
}