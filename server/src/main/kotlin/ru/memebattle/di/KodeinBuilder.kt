package ru.memebattle.di

import com.google.gson.Gson
import io.ktor.application.ApplicationEnvironment
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.eagerSingleton
import org.kodein.di.generic.instance
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import ru.memebattle.auth.BasicAuth
import ru.memebattle.auth.JwtAuth
import ru.memebattle.common.dto.game.MemeResponse
import ru.memebattle.db.data.DatabaseFactory
import ru.memebattle.repository.*
import ru.memebattle.route.RoutingV1
import ru.memebattle.service.*
import java.net.URI

class KodeinBuilder(private val environment: ApplicationEnvironment) {

    @Suppress("EXPERIMENTAL_API_USAGE")
    fun setup(builder: Kodein.MainBuilder) {
        with(builder) {
            bind<DatabaseFactory>() with eagerSingleton {
                val dbUri = URI(environment.config.property("db.jdbcUrl").getString())

                val username: String = dbUri.userInfo.split(":")[0]
                val password: String = dbUri.userInfo.split(":")[1]
                val dbUrl = ("jdbc:postgresql://${dbUri.host}:${dbUri.port}${dbUri.path}")

                DatabaseFactory(
                    dbUrl = dbUrl,
                    dbPassword = password,
                    dbUser = username
                ).apply {
                    init()
                }
            }
            bind<PasswordEncoder>() with eagerSingleton { BCryptPasswordEncoder() }
            bind<JWTTokenService>() with eagerSingleton { JWTTokenService() }
            bind<PostRepository>() with eagerSingleton { PostRepositoryInMemoryWithMutexImpl() }
            bind<PostService>() with eagerSingleton { PostService(instance()) }
            bind<UserRepository>() with eagerSingleton { UserRepositoryImpl() }
            bind<BroadcastChannel<MemeResponse>>("memes") with eagerSingleton {
                @Suppress("RemoveExplicitTypeArguments")
                BroadcastChannel<MemeResponse>(Channel.CONFLATED)
            }
            bind<Gson>() with eagerSingleton { Gson() }
            bind<RateusersRepository>() with eagerSingleton { RateusersRepositoryImpl() }
            bind<MemeService>() with eagerSingleton { MemeService(instance<BroadcastChannel<MemeResponse>>("memes"), instance(), instance()) }
            bind<MemeRepository>() with eagerSingleton { MemeRepositoryImpl() }
            bind<ParserService>() with eagerSingleton { ParserService(instance()) }
            bind<UserService>() with eagerSingleton { UserService(instance(), instance(), instance()) }
            bind<RoutingV1>() with eagerSingleton {
                RoutingV1(
                    instance(),
                    instance(),
                    instance(),
                    instance(),
                    instance<BroadcastChannel<MemeResponse>>("memes"),
                    instance()
                )
            }
            bind<BasicAuth>() with eagerSingleton { BasicAuth(instance(), instance()) }
            bind<JwtAuth>() with eagerSingleton { JwtAuth(instance(), instance()) }
        }
    }
}