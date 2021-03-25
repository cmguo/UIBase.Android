package com.xhb.uibase.utils;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

public class Validates {

    private static final String DEFAULT_EXCLUSIVE_BETWEEN_EX_MESSAGE =
        "The value %s is not in the specified exclusive range of %s to %s";
    private static final String DEFAULT_INCLUSIVE_BETWEEN_EX_MESSAGE =
        "The value %s is not in the specified inclusive range of %s to %s";
    private static final String DEFAULT_MATCHES_PATTERN_EX = "The string %s does not match the pattern %s";
    private static final String DEFAULT_IS_NULL_EX_MESSAGE = "The validated object is null";
    private static final String DEFAULT_IS_TRUE_EX_MESSAGE = "The validated expression is false";
    private static final String DEFAULT_NO_NULL_ELEMENTS_ARRAY_EX_MESSAGE =
        "The validated array contains null element at index: %d";
    private static final String DEFAULT_NO_NULL_ELEMENTS_COLLECTION_EX_MESSAGE =
        "The validated collection contains null element at index: %d";
    private static final String DEFAULT_NOT_BLANK_EX_MESSAGE = "The validated character sequence is blank";
    private static final String DEFAULT_NOT_EMPTY_ARRAY_EX_MESSAGE = "The validated array is empty";
    private static final String DEFAULT_NOT_EMPTY_CHAR_SEQUENCE_EX_MESSAGE =
        "The validated character sequence is empty";
    private static final String DEFAULT_NOT_EMPTY_COLLECTION_EX_MESSAGE = "The validated collection is empty";
    private static final String DEFAULT_NOT_EMPTY_MAP_EX_MESSAGE = "The validated map is empty";
    private static final String DEFAULT_VALID_INDEX_ARRAY_EX_MESSAGE = "The validated array index is invalid: %d";
    private static final String DEFAULT_VALID_INDEX_CHAR_SEQUENCE_EX_MESSAGE =
        "The validated character sequence index is invalid: %d";
    private static final String DEFAULT_VALID_INDEX_COLLECTION_EX_MESSAGE =
        "The validated collection index is invalid: %d";
    private static final String DEFAULT_VALID_STATE_EX_MESSAGE = "The validated state is false";
    private static final String DEFAULT_IS_ASSIGNABLE_EX_MESSAGE = "Cannot assign a %s to a %s";
    private static final String DEFAULT_IS_INSTANCE_OF_EX_MESSAGE = "Expected type: %s, actual: %s";

    /**
     * Constructor. This class should not normally be instantiated.
     */
    public Validates() {
        super();
    }

    // isTrue
    //---------------------------------------------------------------------------------

    /**
     * <p>Validates that the argument condition is {@code true}; otherwise
     * throwing an exception with the specified message. This method is useful when
     * validating according to an arbitrary boolean expression, such as validating a
     * primitive number or using your own custom validation expression.</p>
     * <p>
     * <pre>Validates.isTrue(i &gt; 0.0, "The value must be greater than zero: &#37;d", i);</pre>
     *
     * <p>For performance reasons, the long value is passed as a separate parameter and
     * appended to the exception message only in the case of an error.</p>
     *
     * @param expression the boolean expression to check
     * @param message    the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param value      the value to append to the message when invalid
     * @throws IllegalArgumentException if expression is {@code false}
     * @see #isTrue(boolean)
     * @see #isTrue(boolean, String, double)
     * @see #isTrue(boolean, String, Object...)
     */
    public static void isTrue(final boolean expression, final String message, final long value) {
        if (expression == false) {
            throw new IllegalArgumentException(String.format(message, Long.valueOf(value)));
        }
    }

    /**
     * <p>Validates that the argument condition is {@code true}; otherwise
     * throwing an exception with the specified message. This method is useful when
     * validating according to an arbitrary boolean expression, such as validating a
     * primitive number or using your own custom validation expression.</p>
     * <p>
     * <pre>Validates.isTrue(d &gt; 0.0, "The value must be greater than zero: &#37;s", d);</pre>
     *
     * <p>For performance reasons, the double value is passed as a separate parameter and
     * appended to the exception message only in the case of an error.</p>
     *
     * @param expression the boolean expression to check
     * @param message    the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param value      the value to append to the message when invalid
     * @throws IllegalArgumentException if expression is {@code false}
     * @see #isTrue(boolean)
     * @see #isTrue(boolean, String, long)
     * @see #isTrue(boolean, String, Object...)
     */
    public static void isTrue(final boolean expression, final String message, final double value) {
        if (expression == false) {
            throw new IllegalArgumentException(String.format(message, Double.valueOf(value)));
        }
    }

    /**
     * <p>Validates that the argument condition is {@code true}; otherwise
     * throwing an exception with the specified message. This method is useful when
     * validating according to an arbitrary boolean expression, such as validating a
     * primitive number or using your own custom validation expression.</p>
     * <p>
     * <pre>
     * Validates.isTrue(i &gt;= min &amp;&amp; i &lt;= max, "The value must be between &#37;d and &#37;d", min, max);
     * Validates.isTrue(myObject.isOk(), "The object is not okay");</pre>
     *
     * @param expression the boolean expression to check
     * @param message    the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values     the optional values for the formatted exception message, null array not recommended
     * @throws IllegalArgumentException if expression is {@code false}
     * @see #isTrue(boolean)
     * @see #isTrue(boolean, String, long)
     * @see #isTrue(boolean, String, double)
     */
    public static void isTrue(final boolean expression, final String message, final Object... values) {
        if (expression == false) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }

    /**
     * <p>Validates that the argument condition is {@code true}; otherwise
     * throwing an exception. This method is useful when validating according
     * to an arbitrary boolean expression, such as validating a
     * primitive number or using your own custom validation expression.</p>
     * <p>
     * <pre>
     * Validates.isTrue(i &gt; 0);
     * Validates.isTrue(myObject.isOk());</pre>
     * <p>
     * <p>The message of the exception is &quot;The validated expression is
     * false&quot;.</p>
     *
     * @param expression the boolean expression to check
     * @throws IllegalArgumentException if expression is {@code false}
     * @see #isTrue(boolean, String, long)
     * @see #isTrue(boolean, String, double)
     * @see #isTrue(boolean, String, Object...)
     */
    public static void isTrue(final boolean expression) {
        if (expression == false) {
            throw new IllegalArgumentException(DEFAULT_IS_TRUE_EX_MESSAGE);
        }
    }

    // notNull
    //---------------------------------------------------------------------------------

    /**
     * <p>Validates that the specified argument is not {@code null};
     * otherwise throwing an exception.
     * <p>
     * <pre>Validates.notNull(myObject, "The object must not be null");</pre>
     *
     * <p>The message of the exception is &quot;The validated object is
     * null&quot;.</p>
     *
     * @param <T>    the object type
     * @param object the object to check
     * @return the validated object (never {@code null} for method chaining)
     * @throws NullPointerException if the object is {@code null}
     * @see #notNull(Object, String, Object...)
     */
    public static <T> T notNull(final T object) {
        return notNull(object, DEFAULT_IS_NULL_EX_MESSAGE);
    }

    /**
     * <p>Validates that the specified argument is not {@code null};
     * otherwise throwing an exception with the specified message.
     * <p>
     * <pre>Validates.notNull(myObject, "The object must not be null");</pre>
     *
     * @param <T>     the object type
     * @param object  the object to check
     * @param message the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message
     * @return the validated object (never {@code null} for method chaining)
     * @throws NullPointerException if the object is {@code null}
     * @see #notNull(Object)
     */
    public static <T> T notNull(final T object, final String message, final Object... values) {
        if (object == null) {
            throw new NullPointerException(String.format(message, values));
        }
        return object;
    }

    // notEmpty array
    //---------------------------------------------------------------------------------

    /**
     * <p>Validates that the specified argument array is neither {@code null}
     * nor a length of zero (no elements); otherwise throwing an exception
     * with the specified message.
     * <p>
     * <pre>Validates.notEmpty(myArray, "The array must not be empty");</pre>
     *
     * @param <T>     the array type
     * @param array   the array to check, validated not null by this method
     * @param message the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message, null array not recommended
     * @return the validated array (never {@code null} method for chaining)
     * @throws NullPointerException     if the array is {@code null}
     * @throws IllegalArgumentException if the array is empty
     * @see #notEmpty(Object[])
     */
    public static <T> T[] notEmpty(final T[] array, final String message, final Object... values) {
        if (array == null) {
            throw new NullPointerException(String.format(message, values));
        }
        if (array.length == 0) {
            throw new IllegalArgumentException(String.format(message, values));
        }
        return array;
    }

    /**
     * <p>Validates that the specified argument array is neither {@code null}
     * nor a length of zero (no elements); otherwise throwing an exception.
     * <p>
     * <pre>Validates.notEmpty(myArray);</pre>
     *
     * <p>The message in the exception is &quot;The validated array is
     * empty&quot;.
     *
     * @param <T>   the array type
     * @param array the array to check, validated not null by this method
     * @return the validated array (never {@code null} method for chaining)
     * @throws NullPointerException     if the array is {@code null}
     * @throws IllegalArgumentException if the array is empty
     * @see #notEmpty(Object[], String, Object...)
     */
    public static <T> T[] notEmpty(final T[] array) {
        return notEmpty(array, DEFAULT_NOT_EMPTY_ARRAY_EX_MESSAGE);
    }

    // notEmpty collection
    //---------------------------------------------------------------------------------

    /**
     * <p>Validates that the specified argument collection is neither {@code null}
     * nor a size of zero (no elements); otherwise throwing an exception
     * with the specified message.
     * <p>
     * <pre>Validates.notEmpty(myCollection, "The collection must not be empty");</pre>
     *
     * @param <T>        the collection type
     * @param collection the collection to check, validated not null by this method
     * @param message    the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values     the optional values for the formatted exception message, null array not recommended
     * @return the validated collection (never {@code null} method for chaining)
     * @throws NullPointerException     if the collection is {@code null}
     * @throws IllegalArgumentException if the collection is empty
     * @see #notEmpty(Object[])
     */
    public static <T extends Collection<?>> T notEmpty(final T collection, final String message, final Object... values) {
        if (collection == null) {
            throw new NullPointerException(String.format(message, values));
        }
        if (collection.isEmpty()) {
            throw new IllegalArgumentException(String.format(message, values));
        }
        return collection;
    }

    /**
     * <p>Validates that the specified argument collection is neither {@code null}
     * nor a size of zero (no elements); otherwise throwing an exception.
     * <p>
     * <pre>Validates.notEmpty(myCollection);</pre>
     *
     * <p>The message in the exception is &quot;The validated collection is
     * empty&quot;.</p>
     *
     * @param <T>        the collection type
     * @param collection the collection to check, validated not null by this method
     * @return the validated collection (never {@code null} method for chaining)
     * @throws NullPointerException     if the collection is {@code null}
     * @throws IllegalArgumentException if the collection is empty
     * @see #notEmpty(Collection, String, Object...)
     */
    public static <T extends Collection<?>> T notEmpty(final T collection) {
        return notEmpty(collection, DEFAULT_NOT_EMPTY_COLLECTION_EX_MESSAGE);
    }

    // notEmpty map
    //---------------------------------------------------------------------------------

    /**
     * <p>Validates that the specified argument map is neither {@code null}
     * nor a size of zero (no elements); otherwise throwing an exception
     * with the specified message.
     * <p>
     * <pre>Validates.notEmpty(myMap, "The map must not be empty");</pre>
     *
     * @param <T>     the map type
     * @param map     the map to check, validated not null by this method
     * @param message the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message, null array not recommended
     * @return the validated map (never {@code null} method for chaining)
     * @throws NullPointerException     if the map is {@code null}
     * @throws IllegalArgumentException if the map is empty
     * @see #notEmpty(Object[])
     */
    public static <T extends Map<?, ?>> T notEmpty(final T map, final String message, final Object... values) {
        if (map == null) {
            throw new NullPointerException(String.format(message, values));
        }
        if (map.isEmpty()) {
            throw new IllegalArgumentException(String.format(message, values));
        }
        return map;
    }

    /**
     * <p>Validates that the specified argument map is neither {@code null}
     * nor a size of zero (no elements); otherwise throwing an exception.
     * <p>
     * <pre>Validates.notEmpty(myMap);</pre>
     *
     * <p>The message in the exception is &quot;The validated map is
     * empty&quot;.</p>
     *
     * @param <T> the map type
     * @param map the map to check, validated not null by this method
     * @return the validated map (never {@code null} method for chaining)
     * @throws NullPointerException     if the map is {@code null}
     * @throws IllegalArgumentException if the map is empty
     * @see #notEmpty(Map, String, Object...)
     */
    public static <T extends Map<?, ?>> T notEmpty(final T map) {
        return notEmpty(map, DEFAULT_NOT_EMPTY_MAP_EX_MESSAGE);
    }

    // notEmpty string
    //---------------------------------------------------------------------------------

    /**
     * <p>Validates that the specified argument character sequence is
     * neither {@code null} nor a length of zero (no characters);
     * otherwise throwing an exception with the specified message.
     * <p>
     * <pre>Validates.notEmpty(myString, "The string must not be empty");</pre>
     *
     * @param <T>     the character sequence type
     * @param chars   the character sequence to check, validated not null by this method
     * @param message the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message, null array not recommended
     * @return the validated character sequence (never {@code null} method for chaining)
     * @throws NullPointerException     if the character sequence is {@code null}
     * @throws IllegalArgumentException if the character sequence is empty
     * @see #notEmpty(CharSequence)
     */
    public static <T extends CharSequence> T notEmpty(final T chars, final String message, final Object... values) {
        if (chars == null) {
            throw new NullPointerException(String.format(message, values));
        }
        if (chars.length() == 0) {
            throw new IllegalArgumentException(String.format(message, values));
        }
        return chars;
    }

    /**
     * <p>Validates that the specified argument character sequence is
     * neither {@code null} nor a length of zero (no characters);
     * otherwise throwing an exception with the specified message.
     * <p>
     * <pre>Validates.notEmpty(myString);</pre>
     *
     * <p>The message in the exception is &quot;The validated
     * character sequence is empty&quot;.</p>
     *
     * @param <T>   the character sequence type
     * @param chars the character sequence to check, validated not null by this method
     * @return the validated character sequence (never {@code null} method for chaining)
     * @throws NullPointerException     if the character sequence is {@code null}
     * @throws IllegalArgumentException if the character sequence is empty
     * @see #notEmpty(CharSequence, String, Object...)
     */
    public static <T extends CharSequence> T notEmpty(final T chars) {
        return notEmpty(chars, DEFAULT_NOT_EMPTY_CHAR_SEQUENCE_EX_MESSAGE);
    }

    // notBlank string
    //---------------------------------------------------------------------------------

    /**
     * <p>Checks if a CharSequence is whitespace, empty ("") or null.</p>
     * <p>
     * <pre>
     * StringUtils.isBlank(null)      = true
     * StringUtils.isBlank("")        = true
     * StringUtils.isBlank(" ")       = true
     * StringUtils.isBlank("bob")     = false
     * StringUtils.isBlank("  bob  ") = false
     * </pre>
     *
     * @param cs the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is null, empty or whitespace
     * @since 2.0
     * @since 3.0 Changed signature from isBlank(String) to isBlank(CharSequence)
     */
    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>Validates that the specified argument character sequence is
     * neither {@code null}, a length of zero (no characters), empty
     * nor whitespace; otherwise throwing an exception with the specified
     * message.
     * <p>
     * <pre>Validates.notBlank(myString, "The string must not be blank");</pre>
     *
     * @param <T>     the character sequence type
     * @param chars   the character sequence to check, validated not null by this method
     * @param message the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message, null array not recommended
     * @return the validated character sequence (never {@code null} method for chaining)
     * @throws NullPointerException     if the character sequence is {@code null}
     * @throws IllegalArgumentException if the character sequence is blank
     * @see #notBlank(CharSequence)
     * @since 3.0
     */
    public static <T extends CharSequence> T notBlank(final T chars, final String message, final Object... values) {
        if (chars == null) {
            throw new NullPointerException(String.format(message, values));
        }
        if (isBlank(chars)) {
            throw new IllegalArgumentException(String.format(message, values));
        }
        return chars;
    }

    /**
     * <p>Validates that the specified argument character sequence is
     * neither {@code null}, a length of zero (no characters), empty
     * nor whitespace; otherwise throwing an exception.
     * <p>
     * <pre>Validates.notBlank(myString);</pre>
     *
     * <p>The message in the exception is &quot;The validated character
     * sequence is blank&quot;.</p>
     *
     * @param <T>   the character sequence type
     * @param chars the character sequence to check, validated not null by this method
     * @return the validated character sequence (never {@code null} method for chaining)
     * @throws NullPointerException     if the character sequence is {@code null}
     * @throws IllegalArgumentException if the character sequence is blank
     * @see #notBlank(CharSequence, String, Object...)
     * @since 3.0
     */
    public static <T extends CharSequence> T notBlank(final T chars) {
        return notBlank(chars, DEFAULT_NOT_BLANK_EX_MESSAGE);
    }

    // noNullElements iterable
    //---------------------------------------------------------------------------------

    /**
     * <p>Adds all the elements of the given arrays into a new array.</p>
     * <p>The new array contains all of the element of {@code array1} followed
     * by all of the elements {@code array2}. When an array is returned, it is always
     * a new array.</p>
     * <p>
     * <pre>
     * ArrayUtils.addAll(null, null)     = null
     * ArrayUtils.addAll(array1, null)   = cloned copy of array1
     * ArrayUtils.addAll(null, array2)   = cloned copy of array2
     * ArrayUtils.addAll([], [])         = []
     * ArrayUtils.addAll([null], [null]) = [null, null]
     * ArrayUtils.addAll(["a", "b", "c"], ["1", "2", "3"]) = ["a", "b", "c", "1", "2", "3"]
     * </pre>
     *
     * @param <T>    the component type of the array
     * @param array1 the first array whose elements are added to the new array, may be {@code null}
     * @param array2 the second array whose elements are added to the new array, may be {@code null}
     * @return The new array, {@code null} if both arrays are {@code null}.
     * The type of the new array is the type of the first array,
     * unless the first array is null, in which case the type is the same as the second array.
     * @throws IllegalArgumentException if the array types are incompatible
     * @since 2.1
     */
    public static <T> T[] addAll(final T[] array1, final T... array2) {
        if (array1 == null) {
            return clone(array2);
        } else if (array2 == null) {
            return clone(array1);
        }
        final Class<?> type1 = array1.getClass().getComponentType();
        @SuppressWarnings("unchecked") // OK, because array is of type T
        final T[] joinedArray = (T[]) Array.newInstance(type1, array1.length + array2.length);
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        try {
            System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        } catch (final ArrayStoreException ase) {
            // Check if problem was due to incompatible types
            /*
             * We do this here, rather than before the copy because:
             * - it would be a wasted check most of the time
             * - safer, in case check turns out to be too strict
             */
            final Class<?> type2 = array2.getClass().getComponentType();
            if (!type1.isAssignableFrom(type2)) {
                throw new IllegalArgumentException("Cannot store " + type2.getName() + " in an array of "
                    + type1.getName(), ase);
            }
            throw ase; // No, so rethrow original
        }
        return joinedArray;
    }

    /**
     * <p>Shallow clones an array returning a typecast result and handling
     * {@code null}.</p>
     * <p>
     * <p>The objects in the array are not cloned, thus there is no special
     * handling for multi-dimensional arrays.</p>
     * <p>
     * <p>This method returns {@code null} for a {@code null} input array.</p>
     *
     * @param <T>   the component type of the array
     * @param array the array to shallow clone, may be {@code null}
     * @return the cloned array, {@code null} if {@code null} input
     */
    public static <T> T[] clone(final T[] array) {
        if (array == null) {
            return null;
        }
        return array.clone();
    }

    /**
     * <p>Validates that the specified argument iterable is neither
     * {@code null} nor contains any elements that are {@code null};
     * otherwise throwing an exception with the specified message.
     * <p>
     * <pre>Validates.noNullElements(myCollection, "The collection contains null at position %d");</pre>
     *
     * <p>If the iterable is {@code null}, then the message in the exception
     * is &quot;The validated object is null&quot;.</p>
     *
     * <p>If the iterable has a {@code null} element, then the iteration
     * index of the invalid element is appended to the {@code values}
     * argument.</p>
     *
     * @param <T>      the iterable type
     * @param iterable the iterable to check, validated not null by this method
     * @param message  the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values   the optional values for the formatted exception message, null array not recommended
     * @return the validated iterable (never {@code null} method for chaining)
     * @throws NullPointerException     if the array is {@code null}
     * @throws IllegalArgumentException if an element is {@code null}
     * @see #noNullElements(Iterable)
     */
    public static <T extends Iterable<?>> T noNullElements(final T iterable, final String message, final Object... values) {
        Validates.notNull(iterable);
        int i = 0;
        for (final Iterator<?> it = iterable.iterator(); it.hasNext(); i++) {
            if (it.next() == null) {
                final Object[] values2 = addAll(values, Integer.valueOf(i));
                throw new IllegalArgumentException(String.format(message, values2));
            }
        }
        return iterable;
    }

    /**
     * <p>Validates that the specified argument iterable is neither
     * {@code null} nor contains any elements that are {@code null};
     * otherwise throwing an exception.
     * <p>
     * <pre>Validates.noNullElements(myCollection);</pre>
     *
     * <p>If the iterable is {@code null}, then the message in the exception
     * is &quot;The validated object is null&quot;.</p>
     *
     * <p>If the array has a {@code null} element, then the message in the
     * exception is &quot;The validated iterable contains null element at index:
     * &quot; followed by the index.</p>
     *
     * @param <T>      the iterable type
     * @param iterable the iterable to check, validated not null by this method
     * @return the validated iterable (never {@code null} method for chaining)
     * @throws NullPointerException     if the array is {@code null}
     * @throws IllegalArgumentException if an element is {@code null}
     * @see #noNullElements(Iterable, String, Object...)
     */
    public static <T extends Iterable<?>> T noNullElements(final T iterable) {
        return noNullElements(iterable, DEFAULT_NO_NULL_ELEMENTS_COLLECTION_EX_MESSAGE);
    }

    // validIndex array
    //---------------------------------------------------------------------------------

    /**
     * <p>Validates that the index is within the bounds of the argument
     * array; otherwise throwing an exception with the specified message.</p>
     * <p>
     * <pre>Validates.validIndex(myArray, 2, "The array index is invalid: ");</pre>
     *
     * <p>If the array is {@code null}, then the message of the exception
     * is &quot;The validated object is null&quot;.</p>
     *
     * @param <T>     the array type
     * @param array   the array to check, validated not null by this method
     * @param index   the index to check
     * @param message the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message, null array not recommended
     * @return the validated array (never {@code null} for method chaining)
     * @throws NullPointerException      if the array is {@code null}
     * @throws IndexOutOfBoundsException if the index is invalid
     * @see #validIndex(Object[], int)
     * @since 3.0
     */
    public static <T> T[] validIndex(final T[] array, final int index, final String message, final Object... values) {
        Validates.notNull(array);
        if (index < 0 || index >= array.length) {
            throw new IndexOutOfBoundsException(String.format(message, values));
        }
        return array;
    }

    /**
     * <p>Validates that the index is within the bounds of the argument
     * array; otherwise throwing an exception.</p>
     * <p>
     * <pre>Validates.validIndex(myArray, 2);</pre>
     *
     * <p>If the array is {@code null}, then the message of the exception
     * is &quot;The validated object is null&quot;.</p>
     *
     * <p>If the index is invalid, then the message of the exception is
     * &quot;The validated array index is invalid: &quot; followed by the
     * index.</p>
     *
     * @param <T>   the array type
     * @param array the array to check, validated not null by this method
     * @param index the index to check
     * @return the validated array (never {@code null} for method chaining)
     * @throws NullPointerException      if the array is {@code null}
     * @throws IndexOutOfBoundsException if the index is invalid
     * @see #validIndex(Object[], int, String, Object...)
     * @since 3.0
     */
    public static <T> T[] validIndex(final T[] array, final int index) {
        return validIndex(array, index, DEFAULT_VALID_INDEX_ARRAY_EX_MESSAGE, Integer.valueOf(index));
    }

    // validIndex collection
    //---------------------------------------------------------------------------------

    /**
     * <p>Validates that the index is within the bounds of the argument
     * collection; otherwise throwing an exception with the specified message.</p>
     * <p>
     * <pre>Validates.validIndex(myCollection, 2, "The collection index is invalid: ");</pre>
     *
     * <p>If the collection is {@code null}, then the message of the
     * exception is &quot;The validated object is null&quot;.</p>
     *
     * @param <T>        the collection type
     * @param collection the collection to check, validated not null by this method
     * @param index      the index to check
     * @param message    the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values     the optional values for the formatted exception message, null array not recommended
     * @return the validated collection (never {@code null} for chaining)
     * @throws NullPointerException      if the collection is {@code null}
     * @throws IndexOutOfBoundsException if the index is invalid
     * @see #validIndex(Collection, int)
     * @since 3.0
     */
    public static <T extends Collection<?>> T validIndex(final T collection, final int index, final String message, final Object... values) {
        Validates.notNull(collection);
        if (index < 0 || index >= collection.size()) {
            throw new IndexOutOfBoundsException(String.format(message, values));
        }
        return collection;
    }

    /**
     * <p>Validates that the index is within the bounds of the argument
     * collection; otherwise throwing an exception.</p>
     * <p>
     * <pre>Validates.validIndex(myCollection, 2);</pre>
     *
     * <p>If the index is invalid, then the message of the exception
     * is &quot;The validated collection index is invalid: &quot;
     * followed by the index.</p>
     *
     * @param <T>        the collection type
     * @param collection the collection to check, validated not null by this method
     * @param index      the index to check
     * @return the validated collection (never {@code null} for method chaining)
     * @throws NullPointerException      if the collection is {@code null}
     * @throws IndexOutOfBoundsException if the index is invalid
     * @see #validIndex(Collection, int, String, Object...)
     * @since 3.0
     */
    public static <T extends Collection<?>> T validIndex(final T collection, final int index) {
        return validIndex(collection, index, DEFAULT_VALID_INDEX_COLLECTION_EX_MESSAGE, Integer.valueOf(index));
    }

    // validIndex string
    //---------------------------------------------------------------------------------

    /**
     * <p>Validates that the index is within the bounds of the argument
     * character sequence; otherwise throwing an exception with the
     * specified message.</p>
     * <p>
     * <pre>Validates.validIndex(myStr, 2, "The string index is invalid: ");</pre>
     *
     * <p>If the character sequence is {@code null}, then the message
     * of the exception is &quot;The validated object is null&quot;.</p>
     *
     * @param <T>     the character sequence type
     * @param chars   the character sequence to check, validated not null by this method
     * @param index   the index to check
     * @param message the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message, null array not recommended
     * @return the validated character sequence (never {@code null} for method chaining)
     * @throws NullPointerException      if the character sequence is {@code null}
     * @throws IndexOutOfBoundsException if the index is invalid
     * @see #validIndex(CharSequence, int)
     * @since 3.0
     */
    public static <T extends CharSequence> T validIndex(final T chars, final int index, final String message, final Object... values) {
        Validates.notNull(chars);
        if (index < 0 || index >= chars.length()) {
            throw new IndexOutOfBoundsException(String.format(message, values));
        }
        return chars;
    }

    /**
     * <p>Validates that the index is within the bounds of the argument
     * character sequence; otherwise throwing an exception.</p>
     * <p>
     * <pre>Validates.validIndex(myStr, 2);</pre>
     *
     * <p>If the character sequence is {@code null}, then the message
     * of the exception is &quot;The validated object is
     * null&quot;.</p>
     *
     * <p>If the index is invalid, then the message of the exception
     * is &quot;The validated character sequence index is invalid: &quot;
     * followed by the index.</p>
     *
     * @param <T>   the character sequence type
     * @param chars the character sequence to check, validated not null by this method
     * @param index the index to check
     * @return the validated character sequence (never {@code null} for method chaining)
     * @throws NullPointerException      if the character sequence is {@code null}
     * @throws IndexOutOfBoundsException if the index is invalid
     * @see #validIndex(CharSequence, int, String, Object...)
     * @since 3.0
     */
    public static <T extends CharSequence> T validIndex(final T chars, final int index) {
        return validIndex(chars, index, DEFAULT_VALID_INDEX_CHAR_SEQUENCE_EX_MESSAGE, Integer.valueOf(index));
    }

    // validState
    //---------------------------------------------------------------------------------

    /**
     * <p>Validates that the stateful condition is {@code true}; otherwise
     * throwing an exception. This method is useful when validating according
     * to an arbitrary boolean expression, such as validating a
     * primitive number or using your own custom validation expression.</p>
     * <p>
     * <pre>
     * Validates.validState(field &gt; 0);
     * Validates.validState(this.isOk());</pre>
     * <p>
     * <p>The message of the exception is &quot;The validated state is
     * false&quot;.</p>
     *
     * @param expression the boolean expression to check
     * @throws IllegalStateException if expression is {@code false}
     * @see #validState(boolean, String, Object...)
     * @since 3.0
     */
    public static void validState(final boolean expression) {
        if (expression == false) {
            throw new IllegalStateException(DEFAULT_VALID_STATE_EX_MESSAGE);
        }
    }

    /**
     * <p>Validates that the stateful condition is {@code true}; otherwise
     * throwing an exception with the specified message. This method is useful when
     * validating according to an arbitrary boolean expression, such as validating a
     * primitive number or using your own custom validation expression.</p>
     * <p>
     * <pre>Validates.validState(this.isOk(), "The state is not OK: %s", myObject);</pre>
     *
     * @param expression the boolean expression to check
     * @param message    the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values     the optional values for the formatted exception message, null array not recommended
     * @throws IllegalStateException if expression is {@code false}
     * @see #validState(boolean)
     * @since 3.0
     */
    public static void validState(final boolean expression, final String message, final Object... values) {
        if (expression == false) {
            throw new IllegalStateException(String.format(message, values));
        }
    }

    // matchesPattern
    //---------------------------------------------------------------------------------

    /**
     * <p>Validates that the specified argument character sequence matches the specified regular
     * expression pattern; otherwise throwing an exception.</p>
     * <p>
     * <pre>Validates.matchesPattern("hi", "[a-z]*");</pre>
     *
     * <p>The syntax of the pattern is the one used in the {@link Pattern} class.</p>
     *
     * @param input   the character sequence to validate, not null
     * @param pattern the regular expression pattern, not null
     * @throws IllegalArgumentException if the character sequence does not match the pattern
     * @see #matchesPattern(CharSequence, String, String, Object...)
     * @since 3.0
     */
    public static void matchesPattern(final CharSequence input, final String pattern) {
        //  when breaking BC, consider returning input
        if (Pattern.matches(pattern, input) == false) {
            throw new IllegalArgumentException(String.format(DEFAULT_MATCHES_PATTERN_EX, input, pattern));
        }
    }

    /**
     * <p>Validates that the specified argument character sequence matches the specified regular
     * expression pattern; otherwise throwing an exception with the specified message.</p>
     * <p>
     * <pre>Validates.matchesPattern("hi", "[a-z]*", "%s does not match %s", "hi" "[a-z]*");</pre>
     *
     * <p>The syntax of the pattern is the one used in the {@link Pattern} class.</p>
     *
     * @param input   the character sequence to validate, not null
     * @param pattern the regular expression pattern, not null
     * @param message the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message, null array not recommended
     * @throws IllegalArgumentException if the character sequence does not match the pattern
     * @see #matchesPattern(CharSequence, String)
     * @since 3.0
     */
    public static void matchesPattern(final CharSequence input, final String pattern, final String message, final Object... values) {
        //  when breaking BC, consider returning input
        if (Pattern.matches(pattern, input) == false) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }

    // inclusiveBetween
    //---------------------------------------------------------------------------------

    /**
     * <p>Validates that the specified argument object fall between the two
     * inclusive values specified; otherwise, throws an exception.</p>
     * <p>
     * <pre>Validates.inclusiveBetween(0, 2, 1);</pre>
     *
     * @param <T>   the type of the argument object
     * @param start the inclusive start value, not null
     * @param end   the inclusive end value, not null
     * @param value the object to validate, not null
     * @throws IllegalArgumentException if the value falls outside the boundaries
     * @see #inclusiveBetween(Object, Object, Comparable, String, Object...)
     * @since 3.0
     */
    public static <T> void inclusiveBetween(final T start, final T end, final Comparable<T> value) {
        //  when breaking BC, consider returning value
        if (value.compareTo(start) < 0 || value.compareTo(end) > 0) {
            throw new IllegalArgumentException(String.format(DEFAULT_INCLUSIVE_BETWEEN_EX_MESSAGE, value, start, end));
        }
    }

    /**
     * <p>Validates that the specified argument object fall between the two
     * inclusive values specified; otherwise, throws an exception with the
     * specified message.</p>
     * <p>
     * <pre>Validates.inclusiveBetween(0, 2, 1, "Not in boundaries");</pre>
     *
     * @param <T>     the type of the argument object
     * @param start   the inclusive start value, not null
     * @param end     the inclusive end value, not null
     * @param value   the object to validate, not null
     * @param message the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message, null array not recommended
     * @throws IllegalArgumentException if the value falls outside the boundaries
     * @see #inclusiveBetween(Object, Object, Comparable)
     * @since 3.0
     */
    public static <T> void inclusiveBetween(final T start, final T end, final Comparable<T> value, final String message, final Object... values) {
        //  when breaking BC, consider returning value
        if (value.compareTo(start) < 0 || value.compareTo(end) > 0) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }

    /**
     * Validates that the specified primitive value falls between the two
     * inclusive values specified; otherwise, throws an exception.
     * <p>
     * <pre>Validates.inclusiveBetween(0, 2, 1);</pre>
     *
     * @param start the inclusive start value
     * @param end   the inclusive end value
     * @param value the value to validate
     * @throws IllegalArgumentException if the value falls outside the boundaries (inclusive)
     * @since 3.3
     */
    @SuppressWarnings("boxing")
    public static void inclusiveBetween(long start, long end, long value) {
        //  when breaking BC, consider returning value
        if (value < start || value > end) {
            throw new IllegalArgumentException(String.format(DEFAULT_INCLUSIVE_BETWEEN_EX_MESSAGE, value, start, end));
        }
    }

    /**
     * Validates that the specified primitive value falls between the two
     * inclusive values specified; otherwise, throws an exception with the
     * specified message.
     * <p>
     * <pre>Validates.inclusiveBetween(0, 2, 1, "Not in range");</pre>
     *
     * @param start   the inclusive start value
     * @param end     the inclusive end value
     * @param value   the value to validate
     * @param message the exception message if invalid, not null
     * @throws IllegalArgumentException if the value falls outside the boundaries
     * @since 3.3
     */
    public static void inclusiveBetween(long start, long end, long value, String message) {
        //  when breaking BC, consider returning value
        if (value < start || value > end) {
            throw new IllegalArgumentException(String.format(message));
        }
    }

    /**
     * Validates that the specified primitive value falls between the two
     * inclusive values specified; otherwise, throws an exception.
     * <p>
     * <pre>Validates.inclusiveBetween(0.1, 2.1, 1.1);</pre>
     *
     * @param start the inclusive start value
     * @param end   the inclusive end value
     * @param value the value to validate
     * @throws IllegalArgumentException if the value falls outside the boundaries (inclusive)
     * @since 3.3
     */
    @SuppressWarnings("boxing")
    public static void inclusiveBetween(double start, double end, double value) {
        //  when breaking BC, consider returning value
        if (value < start || value > end) {
            throw new IllegalArgumentException(String.format(DEFAULT_INCLUSIVE_BETWEEN_EX_MESSAGE, value, start, end));
        }
    }

    /**
     * Validates that the specified primitive value falls between the two
     * inclusive values specified; otherwise, throws an exception with the
     * specified message.
     * <p>
     * <pre>Validates.inclusiveBetween(0.1, 2.1, 1.1, "Not in range");</pre>
     *
     * @param start   the inclusive start value
     * @param end     the inclusive end value
     * @param value   the value to validate
     * @param message the exception message if invalid, not null
     * @throws IllegalArgumentException if the value falls outside the boundaries
     * @since 3.3
     */
    public static void inclusiveBetween(double start, double end, double value, String message) {
        //  when breaking BC, consider returning value
        if (value < start || value > end) {
            throw new IllegalArgumentException(String.format(message));
        }
    }

    // exclusiveBetween
    //---------------------------------------------------------------------------------

    /**
     * <p>Validates that the specified argument object fall between the two
     * exclusive values specified; otherwise, throws an exception.</p>
     * <p>
     * <pre>Validates.exclusiveBetween(0, 2, 1);</pre>
     *
     * @param <T>   the type of the argument object
     * @param start the exclusive start value, not null
     * @param end   the exclusive end value, not null
     * @param value the object to validate, not null
     * @throws IllegalArgumentException if the value falls outside the boundaries
     * @see #exclusiveBetween(Object, Object, Comparable, String, Object...)
     * @since 3.0
     */
    public static <T> void exclusiveBetween(final T start, final T end, final Comparable<T> value) {
        //  when breaking BC, consider returning value
        if (value.compareTo(start) <= 0 || value.compareTo(end) >= 0) {
            throw new IllegalArgumentException(String.format(DEFAULT_EXCLUSIVE_BETWEEN_EX_MESSAGE, value, start, end));
        }
    }

    /**
     * <p>Validates that the specified argument object fall between the two
     * exclusive values specified; otherwise, throws an exception with the
     * specified message.</p>
     * <p>
     * <pre>Validates.exclusiveBetween(0, 2, 1, "Not in boundaries");</pre>
     *
     * @param <T>     the type of the argument object
     * @param start   the exclusive start value, not null
     * @param end     the exclusive end value, not null
     * @param value   the object to validate, not null
     * @param message the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message, null array not recommended
     * @throws IllegalArgumentException if the value falls outside the boundaries
     * @see #exclusiveBetween(Object, Object, Comparable)
     * @since 3.0
     */
    public static <T> void exclusiveBetween(final T start, final T end, final Comparable<T> value, final String message, final Object... values) {
        //  when breaking BC, consider returning value
        if (value.compareTo(start) <= 0 || value.compareTo(end) >= 0) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }

    /**
     * Validates that the specified primitive value falls between the two
     * exclusive values specified; otherwise, throws an exception.
     * <p>
     * <pre>Validates.exclusiveBetween(0, 2, 1);</pre>
     *
     * @param start the exclusive start value
     * @param end   the exclusive end value
     * @param value the value to validate
     * @throws IllegalArgumentException if the value falls out of the boundaries
     * @since 3.3
     */
    @SuppressWarnings("boxing")
    public static void exclusiveBetween(long start, long end, long value) {
        //  when breaking BC, consider returning value
        if (value <= start || value >= end) {
            throw new IllegalArgumentException(String.format(DEFAULT_EXCLUSIVE_BETWEEN_EX_MESSAGE, value, start, end));
        }
    }

    /**
     * Validates that the specified primitive value falls between the two
     * exclusive values specified; otherwise, throws an exception with the
     * specified message.
     * <p>
     * <pre>Validates.exclusiveBetween(0, 2, 1, "Not in range");</pre>
     *
     * @param start   the exclusive start value
     * @param end     the exclusive end value
     * @param value   the value to validate
     * @param message the exception message if invalid, not null
     * @throws IllegalArgumentException if the value falls outside the boundaries
     * @since 3.3
     */
    public static void exclusiveBetween(long start, long end, long value, String message) {
        //  when breaking BC, consider returning value
        if (value <= start || value >= end) {
            throw new IllegalArgumentException(String.format(message));
        }
    }

    /**
     * Validates that the specified primitive value falls between the two
     * exclusive values specified; otherwise, throws an exception.
     * <p>
     * <pre>Validates.exclusiveBetween(0.1, 2.1, 1.1);</pre>
     *
     * @param start the exclusive start value
     * @param end   the exclusive end value
     * @param value the value to validate
     * @throws IllegalArgumentException if the value falls out of the boundaries
     * @since 3.3
     */
    @SuppressWarnings("boxing")
    public static void exclusiveBetween(double start, double end, double value) {
        //  when breaking BC, consider returning value
        if (value <= start || value >= end) {
            throw new IllegalArgumentException(String.format(DEFAULT_EXCLUSIVE_BETWEEN_EX_MESSAGE, value, start, end));
        }
    }

    /**
     * Validates that the specified primitive value falls between the two
     * exclusive values specified; otherwise, throws an exception with the
     * specified message.
     * <p>
     * <pre>Validates.exclusiveBetween(0.1, 2.1, 1.1, "Not in range");</pre>
     *
     * @param start   the exclusive start value
     * @param end     the exclusive end value
     * @param value   the value to validate
     * @param message the exception message if invalid, not null
     * @throws IllegalArgumentException if the value falls outside the boundaries
     * @since 3.3
     */
    public static void exclusiveBetween(double start, double end, double value, String message) {
        //  when breaking BC, consider returning value
        if (value <= start || value >= end) {
            throw new IllegalArgumentException(String.format(message));
        }
    }

    // isInstanceOf
    //---------------------------------------------------------------------------------

    /**
     * Validates that the argument is an instance of the specified class, if not throws an exception.
     * <p>
     * <p>This method is useful when validating according to an arbitrary class</p>
     * <p>
     * <pre>Validates.isInstanceOf(OkClass.class, object);</pre>
     *
     * <p>The message of the exception is &quot;Expected type: {type}, actual: {obj_type}&quot;</p>
     *
     * @param type the class the object must be validated against, not null
     * @param obj  the object to check, null throws an exception
     * @throws IllegalArgumentException if argument is not of specified class
     * @see #isInstanceOf(Class, Object, String, Object...)
     * @since 3.0
     */
    public static void isInstanceOf(final Class<?> type, final Object obj) {
        //  when breaking BC, consider returning obj
        if (type.isInstance(obj) == false) {
            throw new IllegalArgumentException(String.format(DEFAULT_IS_INSTANCE_OF_EX_MESSAGE, type.getName(),
                obj == null ? "null" : obj.getClass().getName()));
        }
    }

    /**
     * <p>Validates that the argument is an instance of the specified class; otherwise
     * throwing an exception with the specified message. This method is useful when
     * validating according to an arbitrary class</p>
     * <p>
     * <pre>Validates.isInstanceOf(OkClass.classs, object, "Wrong class, object is of class %s",
     *   object.getClass().getName());</pre>
     *
     * @param type    the class the object must be validated against, not null
     * @param obj     the object to check, null throws an exception
     * @param message the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values  the optional values for the formatted exception message, null array not recommended
     * @throws IllegalArgumentException if argument is not of specified class
     * @see #isInstanceOf(Class, Object)
     * @since 3.0
     */
    public static void isInstanceOf(final Class<?> type, final Object obj, final String message, final Object... values) {
        //  when breaking BC, consider returning obj
        if (type.isInstance(obj) == false) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }

    // isAssignableFrom
    //---------------------------------------------------------------------------------

    /**
     * Validates that the argument can be converted to the specified class, if not, throws an exception.
     * <p>
     * <p>This method is useful when validating that there will be no casting errors.</p>
     * <p>
     * <pre>Validates.isAssignableFrom(SuperClass.class, object.getClass());</pre>
     *
     * <p>The message format of the exception is &quot;Cannot assign {type} to {superType}&quot;</p>
     *
     * @param superType the class the class must be validated against, not null
     * @param type      the class to check, not null
     * @throws IllegalArgumentException if type argument is not assignable to the specified superType
     * @see #isAssignableFrom(Class, Class, String, Object...)
     * @since 3.0
     */
    public static void isAssignableFrom(final Class<?> superType, final Class<?> type) {
        //  when breaking BC, consider returning type
        if (superType.isAssignableFrom(type) == false) {
            throw new IllegalArgumentException(String.format(DEFAULT_IS_ASSIGNABLE_EX_MESSAGE, type == null ? "null" : type.getName(),
                superType.getName()));
        }
    }

    /**
     * Validates that the argument can be converted to the specified class, if not throws an exception.
     * <p>
     * <p>This method is useful when validating if there will be no casting errors.</p>
     * <p>
     * <pre>Validates.isAssignableFrom(SuperClass.class, object.getClass());</pre>
     *
     * <p>The message of the exception is &quot;The validated object can not be converted to the&quot;
     * followed by the name of the class and &quot;class&quot;</p>
     *
     * @param superType the class the class must be validated against, not null
     * @param type      the class to check, not null
     * @param message   the {@link String#format(String, Object...)} exception message if invalid, not null
     * @param values    the optional values for the formatted exception message, null array not recommended
     * @throws IllegalArgumentException if argument can not be converted to the specified class
     * @see #isAssignableFrom(Class, Class)
     */
    public static void isAssignableFrom(final Class<?> superType, final Class<?> type, final String message, final Object... values) {
        //  when breaking BC, consider returning type
        if (superType.isAssignableFrom(type) == false) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }
}
