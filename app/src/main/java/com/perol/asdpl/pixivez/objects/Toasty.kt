/*
 * MIT License
 *
 * Copyright (c) 2019 Perol_Notsfsssf
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE
 */

package com.perol.asdpl.pixivez.objects

import android.content.Context
import android.widget.Toast

class Toasty {
    companion object {

        fun success(context: Context, string: String, length: Int): Toast {
            return Toast.makeText(context, string, length)
        }

        fun error(context: Context, string: String): Toast {
            return Toast.makeText(context, string, Toast.LENGTH_SHORT)
        }

        fun info(context: Context, string: String, length: Int): Toast {

            return Toast.makeText(context, string, length)
        }

        fun warning(context: Context, string: String, length: Int): Toast {

            return Toast.makeText(context, string, length)
        }

        fun normal(context: Context, string: String, length: Int): Toast {

            return Toast.makeText(context, string, length)
        }
    }
}