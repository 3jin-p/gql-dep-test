package com.example.gqldeptest.application.dgs.client

import com.example.gqldeptest.application.dgs.adaptor.DGSClient
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertAll
import org.springframework.boot.test.context.SpringBootTest

class DGSClientTest {

    @Test
    fun test() {
        //given
        val client = DGSClient()
        //when
        val response = client.request("O")
        //then
        assertAll (
            { assertThat(response).hasSize(2) },
            { assertThat(response[0].showId).isEqualTo(2) },
            { assertThat(response[0].title).isEqualTo("Ozark") },
            { assertThat(response[1].showId).isEqualTo(4) },
            { assertThat(response[1].title).isEqualTo("Orange is the New Black") }
        )
    }
}