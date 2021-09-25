package com.timecat.plugin.impl.record

import android.content.Context
import android.os.Bundle
import com.timecat.data.room.record.RoomRecord
import com.timecat.layout.ui.business.breadcrumb.Path
import com.timecat.middle.block.service.ContainerService
import com.timecat.middle.block.service.HomeService
import com.timecat.plugin.api.record.RecordApiWrapper

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/9/12
 * @description null
 * @usage null
 */
class RecordApiWrapperImpl : RecordApiWrapper {
    override fun loadContext(path: Path, context: Context, parentUuid: String, record: RoomRecord?, homeService: HomeService) {
        homeService.loadContextRecord(null)
    }

    override fun loadContextRecord(path: Path, context: Context, parentUuid: String, homeService: HomeService) {
    }

    override fun loadForVirtualPath(context: Context, parentUuid: String, homeService: HomeService, callback: ContainerService.LoadCallback) {
    }

    override fun loadMoreForVirtualPath(context: Context, parentUuid: String, offset: Int, homeService: HomeService, callback: ContainerService.LoadMoreCallback) {
    }

    override fun onDynamicCreate(bundle: Bundle) {
    }

    override fun onDynamicSaveInstanceState(outState: Bundle) {
    }

    override fun onDynamicDestroy() {
    }
}