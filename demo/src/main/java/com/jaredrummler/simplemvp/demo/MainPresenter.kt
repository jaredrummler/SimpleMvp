/*
 * Copyright (C) 2018 Jared Rummler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jaredrummler.simplemvp.demo

import android.annotation.SuppressLint
import android.os.AsyncTask
import com.jaredrummler.simplemvp.Presenter

class MainPresenter : Presenter<MainView>() {

  // can't leak activity in presenter
  @SuppressLint("StaticFieldLeak")
  fun doFakeWork() {
    object : AsyncTask<Void, Void, String>() {
      override fun doInBackground(vararg params: Void?): String {
        Thread.sleep(2000)
        return "Done"
      }

      override fun onPostExecute(result: String) {
        super.onPostExecute(result)
        if (isAttached) {
          view?.showMessage(result)
        }
      }
    }.execute()
  }

}