/**
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.cardinalPlanner.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.cardinalPlanner.Filters;

/**
 * ViewModel for {@link com.example.cardinalPlanner.MainActivity}.
 */

public class MainActivityViewModel extends ViewModel {

    private boolean mIsSigningIn;
    private Filters mFilters;

    /**
     * creates a view model
     */
    public MainActivityViewModel() {
        mIsSigningIn = false;
        mFilters = Filters.getDefault();
    }

    /**
     * returns bool on if the user is signed in
     * @return - user signed in status
     */
    public boolean getIsSigningIn() {
        return mIsSigningIn;
    }

    /**
     * sets sign in stauts
     * @param mIsSigningIn - the status
     */
    public void setIsSigningIn(boolean mIsSigningIn) {
        this.mIsSigningIn = mIsSigningIn;
    }

    /**
     *
     * @return the filters in trhe view model
     */
    public Filters getFilters() {
        return mFilters;
    }

    /**
     * sets view model filters
     * @param mFilters - filters
     */
    public void setFilters(Filters mFilters) {
        this.mFilters = mFilters;
    }
}
