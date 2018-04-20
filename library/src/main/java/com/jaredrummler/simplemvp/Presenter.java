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
import android.app.Fragment;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The base presenter class.
 *
 * @param <View> a type of view to return with {@link #getView()}.
 */
public class Presenter<View> {

  private final CopyOnWriteArrayList<OnDestroyListener> onDestroyListeners = new CopyOnWriteArrayList<>();

  @Nullable private View view;
  @Nullable private String id;

  /**
   * Set or attach the view to this presenter
   */
  @CallSuper
  @UiThread
  public void attach(View view) {
    this.view = view;
    if (id == null) {
      id = createId();
    }
  }

  /**
   * Detach the view from the presenter.
   *
   * Typically this method will be invoked from {@link Activity#onDestroy()}
   * or {@link Fragment#onDestroy()} or {@link android.view.View#onDetachedFromWindow()}.
   */
  @CallSuper
  @UiThread
  public void detach() {
    view = null;
  }

  /**
   * Called when the presenter should be destroyed. This should be called if the activity is
   * {@link Activity#isFinishing() finishing}.
   */
  @CallSuper
  @UiThread
  public void destroy() {
    for (OnDestroyListener onDestroyListener : onDestroyListeners) {
      onDestroyListener.onDestroy();
    }
  }

  public boolean isAttached() {
    return view != null;
  }

  /**
   * Adds a listener observing {@link #destroy}.
   *
   * @param listener
   *     a listener to add.
   */
  public final void addOnDestroyListener(OnDestroyListener listener) {
    onDestroyListeners.add(listener);
  }

  /**
   * Removed a listener observing {@link #destroy}.
   *
   * @param listener
   *     a listener to remove.
   */
  public final void removeOnDestroyListener(OnDestroyListener listener) {
    onDestroyListeners.remove(listener);
  }

  /**
   * Get the view attached to this presenter.
   *
   * @return The view
   */
  @Nullable public View getView() {
    return view;
  }

  /**
   * Get the presenter's generated id.
   *
   * @return The generated id or {@code null} if the {@link #attach(Object)} hasn't been called yet.
   */
  @Nullable public String getId() {
    return id;
  }

  private String createId() {
    return getClass().getName() + ":" + (view == null ? "" : view.getClass().getName()) + ":" + System.nanoTime();
  }

  /**
   * A callback to be invoked when a presenter is about to be destroyed.
   */
  public interface OnDestroyListener {

    /**
     * Callback to be notified when the presenter's {@link Presenter#destroy()} method is invoked.
     */
    void onDestroy();

  }

}
