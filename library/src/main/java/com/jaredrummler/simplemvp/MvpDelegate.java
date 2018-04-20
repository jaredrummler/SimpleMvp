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

public class MvpDelegate<P extends Presenter> {

  public static <P extends Presenter> MvpDelegate<P> create(PresenterFactory<P> factory) {
    return new MvpDelegate<>(factory);
  }

  public static <P extends Presenter> MvpDelegate<P> create(Object view) {
    RequiresPresenter annotation = view.getClass().getAnnotation(RequiresPresenter.class);
    if (annotation == null) {
      throw new IllegalArgumentException("Missing RequiresPresenter annotation");
    }
    //noinspection unchecked
    final Class<P> presenterClass = (Class<P>) annotation.value();
    return new MvpDelegate<>(new PresenterFactory<P>() {
      @NonNull @Override public P createPresenter() {
        try {
          return presenterClass.newInstance();
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    });
  }

  private final PresenterFactory<P> factory;
  private P presenter;

  private MvpDelegate(PresenterFactory<P> factory) {
    this.factory = factory;
  }

  public void onCreate(Object view, Bundle savedInstanceState) {
    presenter = PresenterManager.getInstance().get(factory, savedInstanceState);
    //noinspection unchecked
    presenter.attach(view);
  }

  public void onSaveInstanceState(Bundle outState) {
    if (presenter != null) {
      PresenterManager.getInstance().save(outState, presenter);
    }
  }

  public void onDestroy(boolean isFinishing) {
    if (presenter != null) {
      presenter.detach();
      if (isFinishing) {
        presenter.destroy();
        presenter = null;
      }
    }
  }

  public P getPresenter() {
    return presenter;
  }
}
