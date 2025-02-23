package com.xermao.llmhub.auth.security.utils

import com.xermao.llmhub.provider.domain.model.Provider
import kotlin.random.Random

class ProviderRouter(
    private val items: MutableList<Provider>,
) {

    private val random: Random = Random
    fun route(): Provider {
        // 按照优先级降序排序
        items.sortWith { r1, r2 ->
            r2.priority.compareTo(r1.priority)
        }

        // 根据权重随机选择一个目标
        val totalWeight: Int = items.sumOf { it.weight }
        var randomWeight = random.nextInt(totalWeight) + 1

        //通过概率选择走哪个规则，只哟随机数小于0时才考虑。
        for (rule in items) {
            randomWeight -= rule.weight
            if (randomWeight <= 0) {
                return rule
            }
        }

        // 如果没有匹配到，返回默认目标或者抛出异常
        return items.first()
    }
}
