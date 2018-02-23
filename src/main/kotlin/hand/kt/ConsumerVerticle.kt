package hand.kt

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future

class ConsumerVerticle: AbstractVerticle() {
    override fun start(startFuture: Future<Void>?) {
        println("Hi, I am consumer")

        val cons = vertx.eventBus().consumer<String>("event.test")
        cons.handler { msg ->
            println("Consumer thread id ${Thread.currentThread().id}")
            println("Consumer received: ${msg.body()}")
            msg.reply("Hi, server. I've received your message")
        }

        startFuture?.complete()
    }
}