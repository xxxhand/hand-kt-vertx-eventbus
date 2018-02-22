package hand.kt

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.http.HttpServerOptions
import io.vertx.ext.web.Router

class APIServerVerticle: AbstractVerticle() {

    override fun start(startFuture: Future<Void>?) {

        println("Hi, I am verticle")

        val httpServerOptions = HttpServerOptions()
        httpServerOptions.port = 8080

        val httpServer = vertx.createHttpServer(httpServerOptions)
        val mainRouter = Router.router(vertx)
        mainRouter.route("/io").handler { ctx ->
            println("Route io called")
            ctx.response().setStatusCode(200).end("Hello this is io")
        }

        httpServer.requestHandler { mainRouter.accept(it) }

        httpServer.listen { evt ->
            if (evt.failed()) {
                evt.cause().printStackTrace()
                println(evt.cause().message)
                startFuture?.fail(evt.cause())
                return@listen
            }
            println("Server up on ${httpServer.actualPort()}")
            startFuture?.complete()
        }
    }
}