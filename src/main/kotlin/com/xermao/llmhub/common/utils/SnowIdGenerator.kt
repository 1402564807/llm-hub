package com.xermao.llmhub.common.utils

import org.babyfish.jimmer.sql.meta.UserIdGenerator
import java.util.concurrent.atomic.AtomicLong

class SnowIdGenerator : UserIdGenerator<String> {


    private val defaultTwepoch: Long = 1588435200000L

    // 节点ID长度
    private val nodeIdBits: Int = 10

    // 节点ID的最大值，1023
    private val maxNodeId: Int = (-1 shl nodeIdBits).inv()
    private val timestampBits: Int = 41

    // 序列号12位（表示只允许序号的范围为：0-4095）
    private val sequenceBits: Int = 12

    // 时间戳+序号的最大值
    private val timestampAndSequenceMask: Long = (-1L shl (timestampBits + sequenceBits)).inv()
    private var nodeId: Long = 123
    private var timestampAndSequence: AtomicLong

    init {
        val twepoch = defaultTwepoch
        val timestampWithSequence = System.currentTimeMillis() - twepoch shl 12
        this.timestampAndSequence = AtomicLong(timestampWithSequence)
        this.initNodeId(nodeId)
    }


    override fun generate(entityType: Class<*>): String {
        val next = timestampAndSequence.incrementAndGet()
        val timestampWithSequence = next and timestampAndSequenceMask
        return (this.nodeId or timestampWithSequence).toString()
    }

    private fun initNodeId(nodeId: Long) {
        if (nodeId in 0L..maxNodeId) {
            this.nodeId = nodeId shl (timestampBits + sequenceBits)
        } else {
            val message: String =
                String.format("worker Id can't be greater than {} or less than 0", arrayOf<Any>(maxNodeId))
            throw IllegalArgumentException(message)
        }
    }
}