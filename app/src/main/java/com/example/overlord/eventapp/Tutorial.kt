package com.example.overlord.eventapp

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText

/**
 * All examples are true to my knowledge.
 * My knowledge is limited by the amount of kotlin I have read.
 * So please do not take this as the "only way things can be done".
 *
 * Every kotlin class inherits from "Any"
 * Unit is the type if you do not return anything.
 */

//Val is const
val cannotBeNullValue : String = ""
val canBeNullValue : String? = null

var cannotBeNullVariable : String = ""
var canBeNullVariable : String?  = null
lateinit var cannotBeNull_YouMustAssignLater : String

//Smart Casts - Learn later


//No need to specify return type. Return type assumed void.
fun demoFunction(name : String, rollno : Int) {
    Log.d(name, "" + rollno)
}

/*
If you want to return something other than void you need to specify return type.
If you want return type to be inferred, you can use the next variant.
 */
fun squareBlock(n : Int) : Int {
    return n * n
}

//Compiler will infer return type if you give expressions like this
fun squareExpression(n : Int) = n * n

fun maxBlock(a : Int, b : Int) : Int {
    /*
    if is an expression now, which means that it has a value (corresponding statement)
    In this case the if expression results in a or b
     */
    return if (a > b) a else b
}

//You can do this too
fun maxExpression(a : Int, b : Int) = if (a > b) a else b

fun stringTemplate(name : String) {
    Log.d("No need to concat now", "Hello $name")
    //TODO
    Log.d("More complicated Ex", "Hello ")
}



//Main Function has this signature
fun main(args : Array<String>) {
    for (arg in args) {
        Log.d("TAG", arg)
    }
}

/*
Will generate these and their implementations
class Human {
    public Human(String name, boolean isMarried)
    public void setName(String name)
    public void setMarried(boolean isMarried)
    public boolean isMarried()
    public String getName()
}
Neat conventions.

The class name is followed by parenthesis, this is the constructor.
 */
class Human(var name: String, var isMarried : Boolean) {
    //You can also create on-the-fly properties
    //This is a very simple example, but you can compute complicated ones too
    val isBachelor : Boolean
        get() {
            return ! isMarried
        }


    /** Variables in a Java Class are called fields.
     *  Kotlin has properties.
     *  i.e.
     *  if you declare a class variable 'x', it also defines
     *  default implementations of getX and setX
     *  You can modify these implementations
     */
}

//This is immutable.
data class Person(val name : String, val age: Int? = null)

fun personFunction() {
    val persons = listOf(
        Person("Shibasis", 22),
        Person("Diksha", 22),
        Person("Desktop")
    )

    val youngest = persons.minBy { it.age ?: 0 }
}


enum class Color(val r : Int, val g : Int, val b : Int)  {
    //You can declare enum constants like this
    RED(255, 0, 0),
    ORANGE(255, 165, 0),
    YELLOW(255, 255, 0),
    GREEN(0, 255, 0),
    BLUE(0, 0, 255),
    INDIGO(0, 150, 255),
    VIOLET(100, 120, 150);

    //Example of a on-the-fly property
    val rgb : Int
        get() = ((r * 256) + g) * 256 + b

}

/**
 * Replacement of switch => when
 * Very powerful construct
 */
fun getName(color : Color) : String {
    return when (color) {
        Color.RED -> "Shibasis"
        Color.ORANGE -> "Snigdha"
        Color.YELLOW -> "Smita"
        Color.GREEN -> "Gopal"
        Color.BLUE -> "Diksha"
        Color.INDIGO -> "Amrit"
        Color.VIOLET -> "Soubhagya"
    }
}

fun getWarmth(color: Color) = when (color) {
    Color.RED, Color.YELLOW, Color.ORANGE -> "Warm"
    Color.GREEN -> "Normal"
    Color.BLUE, Color.INDIGO, Color.VIOLET -> "Cold"
}


//Android Example
/**
 * We can write this in Java as
 *
 *
public static void onTextChange(EditText editText, Consumer<String> onTextChangeConsumer) {
    editText.addTextChangedListener(new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        onTextChangeConsumer.accept(charSequence.toString());
    }

    @Override
    public void afterTextChanged(Editable editable) {}
    });
}
 *  And then to call this, we can do,
 *  Utils.onTextChange(editText, s -> Log.d(s)); or something
 *
 *  But in kotlin, we don't need utils classes
 *  We can do
 *
 *  editText.onTextChange(s -> Log.d(s));
 *  if we write the following function
 */

fun EditText.onTextChangeDemo(onTextChange: (input : String) -> Unit ) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

        }

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            onTextChange(charSequence.toString())
        }

        override fun afterTextChanged(editable: Editable) {

        }
    })
}

//Then call like
fun demoExtensionFunctions(editText: EditText) {
    editText.onTextChangeDemo { s -> Log.d("blah", s) }
}

/**
 * This will allow us to write much better code.
 * By correcting flaws in the Android Framework.
 */


