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

package com.jaredrummler.simplemvp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;

public abstract class MvpActivity<P extends Presenter> extends Activity implements PresenterFactory<P> {

  private MvpDelegate<P> mvpDelegate;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getMvpDelegate().onCreate(this, savedInstanceState);
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    getMvpDelegate().onSaveInstanceState(outState);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    getMvpDelegate().onDestroy(isFinishing());
  }

  @NonNull public MvpDelegate<P> getMvpDelegate() {
    if (mvpDelegate == null) {
      mvpDelegate = MvpDelegate.create(this);
    }
    return mvpDelegate;
  }

  public P getPresenter() {
    return getMvpDelegate().getPresenter();
  }
}
