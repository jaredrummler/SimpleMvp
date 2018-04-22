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

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class PresenterManager {

  private static final PresenterManager INSTANCE = new PresenterManager();

  private static final String PRESENTER_STATE_KEY = "presenter.id";

  public static PresenterManager getInstance() {
    return INSTANCE;
  }

  private PresenterManager() {

  }

  private final Map<String, Presenter> PRESENTERS = new HashMap<>();

  public <P extends Presenter> P get(@NonNull PresenterFactory<P> factory, @Nullable Bundle savedInstanceState) {
    final P presenter;
    if (savedInstanceState != null) {
      String key = savedInstanceState.getString(PRESENTER_STATE_KEY);
      if (key != null && PRESENTERS.containsKey(key)) {
        //noinspection unchecked
        presenter = (P) PRESENTERS.get(key);
        remove(presenter);
        return presenter;
      }
    }
    presenter = factory.createPresenter();
    presenter.addOnDestroyListener(new Presenter.OnDestroyListener() {
      @Override public void onDestroy() {
        remove(presenter);
      }
    });
    return presenter;
  }

  public void save(@NonNull Bundle outState, @NonNull Presenter presenter) {
    outState.putString(PRESENTER_STATE_KEY, presenter.getId());
    PRESENTERS.put(presenter.getId(), presenter);
  }

  private void remove(@NonNull Presenter presenter) {
    PRESENTERS.remove(presenter.getId());
  }
}
