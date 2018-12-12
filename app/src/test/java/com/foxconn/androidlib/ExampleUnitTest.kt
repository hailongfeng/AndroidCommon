package com.foxconn.androidlib

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
       var p1 = Person("a",10)
        var p2 =Person("b",5)
       var  list=listOf<Person>(p1,p2)
        var p3=list.maxBy { it.age }

       assertEquals(p1,p3)
//        print(3 and 8)
    }
}

data class Person(val name: String , val age: Int)

//函数第一种形式 支持.加
infix fun Int.add(x:Int):Int{
    return this+x
}
fun foo(arg1:String="Hello Kotlin",arg2:Boolean,arg3:Int){
    print("arg1="+arg1+"&arg2="+arg2+"arg3="+arg3)
}
//可变数量的参数
fun foo4(vararg args:Int){
    for (arg in args){
        print(arg.toString()+",")
    }
}

fun add2(x:Int=0,y:Int=0):Int{
    return x+y
}

fun operate(x:Int=0,y:Int=0,body:(Int,Int)->Int){//body是一个函数类型，传入两个Int类型参数，返回一个Int类型参数
    print("this result is "+body(x,y))
}

fun getValue(){
    operate(3,7,::add2)
    operate(3,7,{x,y->x-y})//函数参数传入一个lambda表达式
    operate(3,7){
        x,y->x+y
    }
}

fun upCase(str:String,body:(String)->String):String{//body是一个函数参数，传入一个String类型参数，返回一个String类型
    return body(str)
}
fun transform(){
    upCase("HelloKotlin"){//函数字面值只有一个参数，可以省略参数声明，其名称是it
        it.toUpperCase()
    }
}


fun String.upper(body:(String)->String):String{
    return body(this)
}

fun transform1(){
    "HelloKotlin".upper { it.toUpperCase() }//lambda表达式是调用的唯一参数，则调用的圆括号可以省略
    "HelloKotlin".upper(fun(str:String):String{//将匿名函数作为一个函数参数传入
        return str.toUpperCase()
    })
}

fun foo() {
    var ints= arrayOf(1,2,3)
    ints.forEach {
        if (it == 0) return//这个 return 表达式从最直接包围它的函数即 foo 中返回。
        print(it)
    }
}
fun transform3():String{
  return  "HelloKotlin".upper {
        print(it.toUpperCase())
        return@upper it.toUpperCase()//返回必须加标签限制
    }
//    "HelloKotlin".upper(fun(str:String):String{
//        return str.toUpperCase()//从匿名函数返回
//    })
}
