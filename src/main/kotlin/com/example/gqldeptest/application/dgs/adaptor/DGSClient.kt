package com.example.gqldeptest.application.dgs.adaptor

import com.netflix.graphql.dgs.client.GraphQLClient
import com.netflix.graphql.dgs.client.HttpResponse
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest
import gql.client.ShowsGraphQLQuery
import gql.client.ShowsProjectionRoot
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.web.client.RestTemplate

class DGSClient {

    private fun createClient(): GraphQLClient {
        val restTemplate = RestTemplate()
        return GraphQLClient.createCustom("http://localhost:8081/graphql") {
            url, headers, body ->
            val httpHeaders = HttpHeaders()
            headers.forEach { httpHeaders.addAll(it.key, it.value) }

            val exchange = restTemplate.exchange(url, HttpMethod.POST, HttpEntity(body, httpHeaders), String::class.java)

            HttpResponse(exchange.statusCodeValue, exchange.body)
        }
    }

    fun request(titleFilter: String?): List<ShowResponse> {
        val query = GraphQLQueryRequest(
            ShowsGraphQLQuery.Builder()
                .titleFilter(titleFilter)
                .build(),

            ShowsProjectionRoot()
                .showId()
                .title()
        )

        return createClient().executeQuery(query.serialize()).extractValue("shows")
    }
}