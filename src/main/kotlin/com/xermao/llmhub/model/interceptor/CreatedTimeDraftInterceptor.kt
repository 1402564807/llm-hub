package com.xermao.llmhub.model.interceptor

import com.xermao.llmhub.model.entity.superclass.CreatedTime
import com.xermao.llmhub.model.entity.superclass.CreatedTimeDraft
import org.babyfish.jimmer.kt.isLoaded
import org.babyfish.jimmer.sql.DraftInterceptor
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class CreatedTimeDraftInterceptor : DraftInterceptor<CreatedTime, CreatedTimeDraft> {

    override fun beforeSave(draft: CreatedTimeDraft, original: CreatedTime?) {
        if (original != null) return

        if (!isLoaded(draft, CreatedTime::createdTime)) {
            draft.createdTime = LocalDateTime.now()
        }
    }

    override fun beforeSaveAll(items: Collection<DraftInterceptor.Item<CreatedTime, CreatedTimeDraft>>) {
        items
            .filter { it.original == null }
            .filter { !isLoaded(it.draft, CreatedTime::createdTime) }
            .forEach { it.draft.createdTime = LocalDateTime.now() }
    }
}