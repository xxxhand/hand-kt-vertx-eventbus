package hand.kt

import io.vertx.core.Vertx

fun main(args: Array<String>) {
    val vx = Vertx.vertx()
    vx.deployVerticle(APIServerVerticle::class.java.name)
    vx.deployVerticle(ConsumerVerticle::class.java.name)
}