package com.example.gqldeptest.application

import com.netflix.graphql.dgs.*
import graphql.schema.GraphQLObjectType

@DgsComponent
class DGSResolver {

    companion object {
        val shows = listOf(
            Show("1", "Stranger Things", 2016),
            Show("2","Ozark", 2016),
            Show("3","The Crown", 2016),
            Show("4","Orange is the New Black", 2016)
        )

        val companies = listOf (
            Company("1","지그재그" ),
            Company("2","무신사" )
        )


        val actors = listOf(
            Actor("1", "Stranger A"),
            Actor("1", "Stranger B"),
            Actor("1", "Stranger C"),
            Actor("2", "Ozark"),
            Actor("4", "Mr.Orange"),
            Actor(null,"윤여정", "1"),
            Actor(null, "유아인", "2"),
        )
    }


    @DgsQuery
    fun shows(@InputArgument titleFilter: String?): List<Show> =
        if (titleFilter == null) {
            shows
        } else {
            shows.filter { s -> s.title.contains(titleFilter) }
        }

//    @DgsData(parentType = "Query", field = "shows") // 붙인 애는 DataFetcher 가 됨. default (ptype = Query, field = $methodName)
//    parantType 은 Query, Mutation 보단 명시적으로 특정 쿼리나 뮤테이션을 지정하는게 좋을듯

    @DgsQuery
    fun companies(): List<Company> = companies

    @DgsData.List(
        DgsData(parentType = "Show", field = "actors"),
        DgsData(parentType = "Company", field = "actors")
    )
    fun actors(dfe: DgsDataFetchingEnvironment): List<Actor> {
        return ActorDataFetcher.fetch(dfe)
    }

    class ActorDataFetcher {
        enum class ActorParentType {
            COMPANY,
            SHOW;

            companion object {
                fun findByName(name: String): ActorParentType =
                    values().find { it.name == name.uppercase() } ?: throw java.lang.IllegalArgumentException()

            }
        }

        companion object {
            fun fetch(dfe: DgsDataFetchingEnvironment): List<Actor> {
                val type = dfe.parentType as GraphQLObjectType

                return when (ActorParentType.findByName(type.name)) {
                    ActorParentType.COMPANY -> {
                        val source = dfe.getSource<Company>()
                        actors.filter { it.companyId ==  source.companyId}.toList()
                    }
                    ActorParentType.SHOW -> {
                        val source = dfe.getSource<Show>()
                        actors.filter { it.showId ==  source.showId}.toList()
                    }
                }
            }
        }
    }

    class Show(
        val showId: String,
        val title: String,
        val releaseYear: Int
    )

    class Company(
        val companyId: String,
        val name: String
    )

    class Actor(
        val showId: String? = null,
        val actorName: String,
        val companyId: String? = null
    ) {
    }
}