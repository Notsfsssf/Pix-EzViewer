package com.perol.asdpl.pixivez.objects

import android.content.Context
import android.widget.Toast

class Toasty {
    companion object {

       fun success(context: Context, string:String, length:Int):Toast{
return Toast.makeText(context,string,length)
        }
        fun error(context: Context, string:String):Toast{
            return Toast.makeText(context,string,Toast.LENGTH_SHORT)
        }
        fun info(context: Context, string:String, length:Int):Toast{

            return Toast.makeText(context,string,length)
        }
        fun warning(context: Context, string:String, length:Int):Toast{

            return Toast.makeText(context,string,length)
        }
        fun normal(context: Context, string:String, length:Int):Toast{

            return Toast.makeText(context,string,length)
        }
    }
}