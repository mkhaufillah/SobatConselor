package id.sobat.sobatconselor.Model

import java.util.Date

class DataLocal {
    companion object {
        const val RC_SIGN_IN = 9698
        const val TAG_SIGN_IN = "SIGN_IN"
        const val TAG_NICKNAME = "NICKNAME_CHECK"
        const val TAG_QUERY = "QUERY_DATA"
        const val DATA_KEY_SHARE = "SHARE_DJAFYEJSNXDDFS"
        var user: ConselorId? = null
        var width = 0
        var out = false

        /*
         * Copyright 2012 Google Inc.
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

        private const val SECOND_MILLIS = 1000
        private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
        private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
        private const val DAY_MILLIS = 24 * HOUR_MILLIS

        fun getTimeAgo(timePar: Long): String? {
            var time = timePar
            if (time < 1000000000000L) {
                // if timestamp given in seconds, convert to millis
                time *= 1000
            }

            val date = Date()
            val now = date.time
            if (time > now || time <= 0) {
                return null
            }

            // TODO: localize
            val diff = now - time
            return if (diff < MINUTE_MILLIS) {
                "just now"
            } else if (diff < 2 * MINUTE_MILLIS) {
                "a minute ago"
            } else if (diff < 50 * MINUTE_MILLIS) {
                "${diff / MINUTE_MILLIS} minutes ago"
            } else if (diff < 90 * MINUTE_MILLIS) {
                "an hour ago"
            } else if (diff < 24 * HOUR_MILLIS) {
                "${diff / HOUR_MILLIS} hours ago"
            } else if (diff < 48 * HOUR_MILLIS) {
                "yesterday"
            } else {
                "${diff / DAY_MILLIS} days ago"
            }
        }
    }
}