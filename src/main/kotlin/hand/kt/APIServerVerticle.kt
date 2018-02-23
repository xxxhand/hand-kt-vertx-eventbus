package hand.kt

import io.vertx.core.AbstractVerticle
import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.eventbus.Message
import io.vertx.core.http.HttpServerOptions
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler

class APIServerVerticle: AbstractVerticle() {

    override fun start(startFuture: Future<Void>?) {

        println("Hi, I am server")

        val httpServerOptions = HttpServerOptions()
        httpServerOptions.port = 8080

        val httpServer = vertx.createHttpServer(httpServerOptions)
        val mainRouter = Router.router(vertx)
        mainRouter.route().handler(BodyHandler.create())
        mainRouter.post("/io").handler { ctx ->
            println("Server thread id ${Thread.currentThread().id}")
            vertx.eventBus().send<String>(
                    "event.test",
                    ctx.bodyAsString,
                    { ctx.response().setStatusCode(200).end(it.result().body()) }
            )
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