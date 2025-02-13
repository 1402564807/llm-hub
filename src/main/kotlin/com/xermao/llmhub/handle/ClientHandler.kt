package com.xermao.llmhub.handle

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.*
import org.babyfish.jimmer.kt.new
import reactor.core.publisher.Sinks

class ClientHandler(
    private val sink: Sinks.Many<Any>,
    private val request: DefaultFullHttpRequest
) : SimpleChannelInboundHandler<Any>() {

    private val buffer = StringBuilder()

    override fun channelActive(ctx: ChannelHandlerContext) {
        ctx.writeAndFlush(request)
    }

    override fun channelRead0(ctx: ChannelHandlerContext, msg: Any) {
        if (msg is FullHttpResponse) {
            if (msg.status().code() != 200) {
                sink.tryEmitError(RuntimeException(msg.content().toString(Charsets.UTF_8)))
                return
            }
            val contentType = msg.headers().get(HttpHeaderNames.CONTENT_TYPE)!!

            if (contentType.contains(HttpHeaderValues.APPLICATION_JSON)) {
                sink.tryEmitNext(msg.duplicate().content().toString(Charsets.UTF_8))
                sink.tryEmitComplete()
                return
            }
        }

        if (msg is HttpContent) {
            val chuck = msg.content().toString(Charsets.UTF_8)
            buffer.append(chuck)

            parseEvents()
        }
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        sink.tryEmitComplete()
    }



    private fun parseEvents() {
        var index: Int
        while ((buffer.indexOf("\n\n").also { index = it }) != -1) {
            val eventData = buffer.substring(0, index)
            buffer.delete(0, index + 2) // 移除已处理的数据

            // 解析事件字段（data、event、id等）
            val lines = eventData.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (line in lines) {
                sink.tryEmitNext(line)
                if (line.contentEquals("[DONE]")) {
                    sink.tryEmitComplete()
                }
                if (line.startsWith("data:")) {
                    val data = line.substring(5).trim { it <= ' ' }
                } else if (line.startsWith("event:")) {
                    val eventType = line.substring(6).trim { it <= ' ' }
                    // 处理事件类型
                } else if (line.startsWith("id:")) {
                    val eventId = line.substring(3).trim { it <= ' ' }
                    // 处理事件 ID
                }
            }
        }
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        sink.tryEmitError(cause)
        ctx.close()
    }
}