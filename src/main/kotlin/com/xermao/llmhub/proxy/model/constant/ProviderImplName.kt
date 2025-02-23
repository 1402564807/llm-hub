package com.xermao.llmhub.proxy.model.constant

import com.xermao.llmhub.common.domain.constant.GlobalConstant
import com.xermao.llmhub.provider.domain.constant.ProviderName

object ProviderImplName {
    const val OPEN_AI_SERVICE_PROVIDER = GlobalConstant.CHAT_MODEL_IMPL + ProviderName.OPEN_AI
    const val VOLCENGINE_SERVICE_PROVIDER = GlobalConstant.CHAT_MODEL_IMPL + ProviderName.VOLCENGINE
}
