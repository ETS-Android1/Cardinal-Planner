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
package com.example.cardinalPlanner;

import com.google.firebase.firestore.Query;

/**
 * Object for passing filters around.
 */
public class Filters {

    private String type = null;
    private String name = null;
    private int price = -1;
    private String sortBy = null;
    private Query.Direction sortDirection = null;

    /**
     * Default constructor
     */
    public Filters() {}

    /**
     * gets the default filters
     * @return - filters
     */
    public static Filters getDefault() {
        Filters filters = new Filters();

        return filters;
    }


}
