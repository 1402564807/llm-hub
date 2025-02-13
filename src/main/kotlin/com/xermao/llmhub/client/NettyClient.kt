package com.xermao.llmhub.client

import com.xermao.llmhub.handle.ClientHandler
import io.netty.bootstrap.Bootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelInitializer
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.http.*
import io.netty.handler.logging.LoggingHandler
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import java.net.URI


class NettyClient(private val host: String, private val port: Int) {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)
    private var group: EventLoopGroup? = null
    private var channel: Channel? = null

    /**
     * 初始化 Netty 客户端连接
     */
    fun connectAndSendRequest(request: DefaultFullHttpRequest): Flux<Any> {
        group = NioEventLoopGroup()
        val bootstrap = Bootstrap()
        val sink = Sinks.many().unicast().onBackpressureBuffer<Any>()

        request.headers().set(HttpHeaderNames.HOST, URI(request.uri()).host)
        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE)
        request.headers().set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes())
        request.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)

        bootstrap.group(group)
            .channel(NioSocketChannel::class.java)
            .handler(object : ChannelInitializer<Channel>() {
                override fun initChannel(ch: Channel) {
                    ch.pipeline()
                        .addLast(LoggingHandler())
                        .addLast(HttpClientCodec())
                        .addLast(HttpObjectAggregator(65536))
                        .addLast(HttpContentDecompressor())
                        .addLast(ClientHandler(sink, request))
                }
            })



        this.channel = bootstrap.connect(host, port).sync().channel()

        // 返回 Mono（实际数据在 ClientHandler 中填充）
        return sink.asFlux()
            .doOnComplete{ shutdown() }
    }

    /**
     * 关闭客户端
     */
    private fun shutdown() {
        if (channel != null) {
            channel!!.close()
        }
        if (group != null) {
            group!!.shutdownGracefully()
        }
    }
}
