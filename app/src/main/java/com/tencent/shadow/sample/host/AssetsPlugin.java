/*
 * Tencent is pleased to support the open source community by making Tencent Shadow available.
 * Copyright (C) 2019 THL A29 Limited, a Tencent company.  All rights reserved.
 *
 * Licensed under the BSD 3-Clause License (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *     https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.tencent.shadow.sample.host;

import android.content.Context;

import com.timecat.module.plugin.database.Plugin;
import com.timecat.module.plugin.database.PluginDao;
import com.timecat.module.plugin.database.PluginDatabase;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AssetsPlugin {
    public ExecutorService singlePool = Executors.newSingleThreadExecutor();

    private static AssetsPlugin sInstance = new AssetsPlugin();

    public static AssetsPlugin getInstance() {
        return sInstance;
    }

    private AssetsPlugin() {
    }

    public void init(Context context, Plugin plugin) {
        singlePool.execute(new Runnable() {
            @Override
            public void run() {
                preparePlugin(context.getApplicationContext(), plugin);
            }
        });
    }

    private static void preparePlugin(Context context, Plugin p) {
        File pluginManagerFile = p.managerApkFile(context);
        File pluginZipFile = p.pluginZipFile(context);
        try {
            InputStream is = context.getAssets().open("plugin-manager.apk");
            FileUtils.copyInputStreamToFile(is, pluginManagerFile);

            InputStream zip = context.getAssets().open(BuildConfig.DEBUG ? "plugin-debug.zip" : "plugin-release.zip");
            FileUtils.copyInputStreamToFile(zip, pluginZipFile);

        } catch (IOException e) {
            throw new RuntimeException("从assets中复制apk出错", e);
        }
        PluginDao dao = PluginDatabase.forFile(context).pluginDao();
        dao.replace(p);
    }
}
