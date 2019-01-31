package com.phoenixoverlord.eventapp.xtra

import android.util.Log
import android.text.Editable
import android.widget.EditText
import android.text.TextWatcher
import java.lang.IllegalArgumentException
import java.util.*

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
    public String getSimpleName()
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
        //No else(default) required if exhaustive
    }
}

fun getWarmth(color: Color) = when (color) {
    Color.RED, Color.YELLOW, Color.ORANGE -> "Warm"
    Color.GREEN -> "Normal"
    Color.BLUE, Color.INDIGO, Color.VIOLET -> "Cold"
}

fun mix(one : Color, two : Color) = when(setOf(one, two)) {
    setOf(
        Color.RED,
        Color.YELLOW
    ) -> Color.ORANGE
    setOf(
        Color.YELLOW,
        Color.BLUE
    ) -> Color.GREEN
    else -> Color.VIOLET
}

/*
Now some object oriented example. Inheritance based.
 */

interface Expr
class Num(val value : Int) : Expr
class Sum(val left : Expr, val right : Expr) :
    Expr

/*
JAVA equivalent

public int eval(Expr e) {
    if ( e instanceOf Num )
        // This explicit cast is not required in Kotlin
        // If you have checked type once, you don't need to cast
        return (Num)e;
    else if ( e instanceOf Sum )
        // Ugly
        return eval(((Sum)e).left) + eval(((Sum)e).right);
    else
        throw error;
}
 */


fun eval(e : Expr) : Int = when(e) {
    is Num -> e.value
    is Sum -> eval(e.left) + eval(
        e.right
    )
    else -> throw IllegalArgumentException("Unknown")
}

fun evalBlock(e : Expr) : Int {
    // It will complain about return. Ignore
    // Basically if you are in a block form, you'll have to explicitly return
    when (e) {
        is Num -> return e.value
        is Sum -> return eval(e.left) + eval(
            e.right
        )
        else -> throw IllegalArgumentException("Unknown")
    }
}

//This form of when does not have parenthesis
fun anotherFormOfWhen(n : Int, m : Int) = when {

    n % 15 == 0 -> "3 and 5"
    n % 3 == 0 -> "3"
    n % 5 == 0 -> "5"
    //You can have expressions from other variables
    m % 3 == 0 -> "3"
    //From other functions
    squareBlock(n) == m -> "squares"
    //Just take care while writing.
    else -> "none"
}

// Range types in Kotlin must be [low, high]
// The following is [1, 100] (includes 100)
val range = 1..100
val alphaRange = 'A'..'Z'

// One form of for loop
fun demoForForms() {
    for (i in range)
        Log.d("", i.toString())

    // This starts from 100 and decreases 2 at a time until it reaches 1
    for (i in 100 downTo 1 step 2)
        Log.d("", i.toString())

    // for (i in 0..100)
    for (i in 0 until 100)
        Log.d("", i.toString())

    // Map iteration example
    val binaryRepresentations = TreeMap<Char, String>()
    // Map creation
    for (c in 'A'..'Z') {
        binaryRepresentations[c] = Integer.toBinaryString(c.toInt())
    }
    // for ((key, value) in binaryRepresentations)
    for ((char, binary) in binaryRepresentations)
        // String template Matching
        Log.d("", "$char = $binary")

    //This form is like repeat(i, n) and range-for combined
    val friendList = arrayListOf("shibasis", "diksha", "soubhagya")
    for ((index, element) in friendList.withIndex())
        Log.d("", "$index: $element")

    //TODO More forms
}

//It translates to the normal c >= 'a' && c <= 'z' || ...you know
fun isAlpha(c : Char) = c in 'a'..'z' || c in 'A'..'Z'

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
 *  And addOnSuccessListener to call this, we can do,
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
/**
 * This will allow us to write much better code.
 * By correcting flaws in the Android Framework.
 */

//Then call like
fun demoExtensionFunctions(editText: EditText) {
    editText.onTextChangeDemo { s -> Log.d("blah", s) }
}


fun<T> join(
    collection : Collection<T>,
    prefix : String = "",
    postfix : String = "",
    separator : String = ""
    ) : String {

    val builder = StringBuilder(prefix)
    for ( (idx, element) in collection.withIndex() ) {
        if (idx > 0) builder.append(separator)
        builder.append(element)
    }
    builder.append(postfix)

    return builder.toString()
}

fun testJoin() {
    val joined = join(listOf(1, 2, 3), "(", ")", ", ")
    //You can convert this join easily to an Extension function
}

/**
 * Extensions functions are stored in a class as static functions
 * Ex : See extensions/activities
 * There is a line at the start
 * @file:JvmName("ActivityUtils")
 * This is how you specify the name of the class that will house these functions for calling from Java
 * All those functions will stored in a class ActivityUtils as static functions
 *
 */

// Be very careful while implementing different Extension functions on Base and Sub classes.
// Ex : Activity and AppCompatActivity having the same function signature but different implementation
// The declaration time type of the parameter will be used, not the runtime.
// Ex Activity act = AppCompatActivity()
// act.Extension will call the one in Activity not in AppCompatActivity

// Do not write Extension function with the same name as a member function. Always the member will be called.

/*
Example of INFIX function use
*/

enum class Suit {
    HEARTS,
    SPADES,
    CLUBS,
    DIAMONDS
}
enum class Rank {
    TWO, THREE, FOUR, FIVE,
    SIX, SEVEN, EIGHT, NINE,
    TEN, JACK, QUEEN, KING, ACE
}

data class Card(val rank: Rank, val suit: Suit)

infix fun Rank.of(suit : Suit) =
    Card(this, suit)

val card = Rank.QUEEN of Suit.HEARTS

/** I have to find more uses. This is new */

private interface testIface {

}








