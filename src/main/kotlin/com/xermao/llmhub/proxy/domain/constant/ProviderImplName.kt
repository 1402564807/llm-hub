package com.xermao.llmhub.proxy.domain.constant

import com.xermao.llmhub.common.domain.constant.GlobalConstant
import com.xermao.llmhub.provider.domain.constant.ProviderName

object ProviderImplName {
    object Chat {
        const val OPEN_AI_SERVICE_PROVIDER = GlobalConstant.CHAT_MODEL_IMPL + ProviderName.OPEN_AI
        const val VOLCENGINE_SERVICE_PROVIDER = GlobalConstant.CHAT_MODEL_IMPL + ProviderName.VOLCENGINE
    }

    object Embed {
        const val OPEN_AI_SERVICE_PROVIDER = GlobalConstant.EMBED_MODEL_IMPL + ProviderName.OPEN_AI
        const val VOLCENGINE_SERVICE_PROVIDER = GlobalConstant.EMBED_MODEL_IMPL + ProviderName.VOLCENGINE
    }

    object Image {
        const val OPEN_AI_SERVICE_PROVIDER = GlobalConstant.IMAGE_MODEL_IMPL + ProviderName.OPEN_AI
        const val VOLCENGINE_SERVICE_PROVIDER = GlobalConstant.IMAGE_MODEL_IMPL + ProviderName.VOLCENGINE
    }

    object Audio {
        const val OPEN_AI_SERVICE_PROVIDER = GlobalConstant.AUDIO_MODEL_IMPL + ProviderName.OPEN_AI
        const val VOLCENGINE_SERVICE_PROVIDER = GlobalConstant.AUDIO_MODEL_IMPL + ProviderName.VOLCENGINE
    }

    object Video {
        const val OPEN_AI_SERVICE_PROVIDER = GlobalConstant.VIDEO_MODEL_IMPL + ProviderName.OPEN_AI
        const val VOLCENGINE_SERVICE_PROVIDER = GlobalConstant.VIDEO_MODEL_IMPL + ProviderName.VOLCENGINE
    }
}
