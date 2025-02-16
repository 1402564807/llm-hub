package com.xermao.llmhub.common.utils

import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

@Component
object BeanManager: ApplicationContextAware {

    private lateinit var applicationContext: ApplicationContext

    /**
     * 通过Bean名称和类型获取
     */
    fun <T> getBean(name: String, clazz: Class<T>): T? {
        return try {
            applicationContext.getBean(name, clazz)
        } catch (e: NoSuchBeanDefinitionException) {
            null
        }
    }

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        BeanManager.applicationContext = applicationContext
    }
}