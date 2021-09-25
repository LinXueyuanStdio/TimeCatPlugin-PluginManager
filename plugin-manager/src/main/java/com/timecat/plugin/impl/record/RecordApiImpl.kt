package com.timecat.plugin.impl.record

import android.content.Context
import com.timecat.plugin.api.record.RecordApiFactory
import com.timecat.plugin.api.record.RecordApiWrapper

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/9/12
 * @description null
 * @usage null
 */
class RecordApiImpl : RecordApiFactory {
    override fun build(context: Context): RecordApiWrapper {
        return RecordApiWrapperImpl()
    }
}