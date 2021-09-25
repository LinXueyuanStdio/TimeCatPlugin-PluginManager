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
package com.tencent.shadow.sample.host

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.timecat.identity.readonly.PluginHub
import com.timecat.module.plugin.PluginCloudActivity
import com.timecat.module.plugin.PluginRouterActivity
import com.timecat.module.plugin.PluginUpdateActivity
import com.timecat.module.plugin.database.Plugin
import com.xiaojinzi.component.impl.*

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val plugin = Plugin(
            0, "com.timecat.plugin.assets", 0, "测试插件",
            1, "1.0.0",
            1, "1.0.0",
            "com.tencent.shadow.sample.plugin.app.lib.gallery.splash.SplashActivity"
        )
        AssetsPlugin.getInstance().init(this, plugin)

        val rootView = LinearLayout(this)
        rootView.orientation = LinearLayout.VERTICAL

        val infoTextView = TextView(this)
        infoTextView.setText(
            "这是一个全动态的测试程序，插件管理（test-dynamic-manager）," +
                "插件框架（test-dynamic-loader及test-dynamic-runtime），" +
                "以及插件本身,都是动态加载的"
        )
        rootView.addView(infoTextView)

        fun startByPartKey(partKey: String) {
            val intent = Intent(this@MainActivity, PluginRouterActivity::class.java)
            intent.putExtra(PluginHub.KEY_PLUGIN_PART_KEY, partKey)
            intent.putExtra("plugin", plugin)
            intent.putExtra(PluginHub.KEY_CLASSNAME, "com.tencent.shadow.sample.plugin.app.lib.gallery.splash.SplashActivity")
            startActivity(intent)
        }
        rootView.addView(createButton("启动插件 ${PART_KEY_PLUGIN_MAIN_APP}") {
            startByPartKey(PART_KEY_PLUGIN_MAIN_APP)
        })
        rootView.addView(createButton("启动插件 ${PART_KEY_PLUGIN_ANOTHER_APP}") {
            startByPartKey(PART_KEY_PLUGIN_ANOTHER_APP)
        })

        rootView.addView(createButton("本地已安装插件") {
            startActivity(Intent(this, PluginUpdateActivity::class.java))
        })
        rootView.addView(createButton("云端插件市场") {
            startActivity(Intent(this, PluginCloudActivity::class.java))
        })

        setContentView(rootView)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 1) {
            for (i in permissions.indices) {
                if (permissions[i] == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        throw RuntimeException("必须赋予权限.")
                    }
                }
            }
        }
    }

    private fun createButton(name: String, path: String): Button {
        val button = createButton(name)
        button.setOnClickListener { go(path) }
        return button
    }

    private fun createButton(name: String, onClickListener: View.OnClickListener): Button {
        val button = createButton(name)
        button.setOnClickListener(onClickListener)
        return button
    }

    private fun createButton(name: String): Button {
        val button = Button(this)
        button.text = name
        button.isAllCaps = false
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        button.layoutParams = layoutParams
        button.gravity = Gravity.CENTER
        return button
    }

    private fun go(path: String) {
        Router.with().hostAndPath(path)
            .forward(object : Callback {
                override fun onSuccess(result: RouterResult) {}
                override fun onEvent(
                    successResult: RouterResult?,
                    errorResult: RouterErrorResult?
                ) {
                }

                override fun onCancel(originalRequest: RouterRequest?) {}
                override fun onError(errorResult: RouterErrorResult) {
                    Log.e("ui", errorResult.error.toString())
                }
            })
    }

    companion object {
        const val PART_KEY_PLUGIN_MAIN_APP = "plugin-shadow-app"
        const val PART_KEY_PLUGIN_ANOTHER_APP = "plugin-shadow-app2"
    }
}