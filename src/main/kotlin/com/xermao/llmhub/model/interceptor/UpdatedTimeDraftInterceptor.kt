package com.xermao.llmhub.model.interceptor

import com.xermao.llmhub.model.entity.superclass.UpdatedTime
import com.xermao.llmhub.model.entity.superclass.UpdatedTimeDraft
import org.babyfish.jimmer.kt.isLoaded
import org.babyfish.jimmer.sql.DraftInterceptor
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class UpdatedTimeDraftInterceptor : DraftInterceptor<UpdatedTime, UpdatedTimeDraft> {

    override fun beforeSave(draft: UpdatedTimeDraft, original: UpdatedTime?) {
        if (!isLoaded(draft, UpdatedTime::updatedTime)) {
            draft.updatedTime = LocalDateTime.now()
        }
    }

    override fun beforeSaveAll(items: Collection<DraftInterceptor.Item<UpdatedTime, UpdatedTimeDraft>>) {
        items
            .filter { !isLoaded(it.draft, UpdatedTime::updatedTime) }
            .forEach { it.draft.updatedTime = LocalDateTime.now() }
    }
}