package fr.afnic.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

/**
 * Support class for {@link Objects#toStringHelper}.
 * 
 * @author Julien Alaphilippe
 * @since 2
 */
public class ToStringHelper {
    private final List<String> fieldString = new ArrayList<String>();
    private String className = null;

    public ToStringHelper(final Class<?> clazz) {
        this(clazz.getSimpleName());
    }

    /**
     * Use {@link Objects#toStringHelper(Object)} to create an instance.
     */
    public ToStringHelper(final String className) {
        this.className = Preconditions.checkNotNull(className);
    }

    public ToStringHelper() {
    }

    /**
     * 
     * 
     * @param objectInstance
     * @return
     */
    public ToStringHelper addAllObjectAttributes(final Object objectInstance) {
        Preconditions.checkNotNull(objectInstance);
        if (this.className == null) {
            this.className = objectInstance.getClass().getSimpleName();
        }
        final Class<?> classInstance = objectInstance.getClass();
        for (final Field field : classInstance.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) {

                final String name = field.getName();
                String value = "";

                try {
                    final boolean pAccessible = field.isAccessible();
                    if (!pAccessible) {
                        field.setAccessible(true);
                    }
                    if (field.get(objectInstance) != null) {
                        value = field.get(objectInstance).toString();
                    }
                } catch (final IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (final IllegalAccessException e) {
                    e.printStackTrace();
                }
                this.add(name, value);
            }
        }
        return this;
    }

    /**
     * Adds a name/value pair to the formatted output in {@code name=value} format. If {@code value} is {@code null}, the string {@code "null"} is
     * used.
     */
    public ToStringHelper add(final String name, final Object value) {
        return this.addValue(Preconditions.checkNotNull(name) + "[" + value + "]");
    }

    /**
     * Adds a value to the formatted output in {@code value} format.
     * <p/>
     * 
     * It is strongly encouraged to use {@link #add(String, Object)} instead and give value a readable name.
     */
    public ToStringHelper addValue(final Object value) {
        this.fieldString.add(String.valueOf(value));
        return this;
    }

    private static final Joiner JOINER = Joiner.on(" ");

    /**
     * Returns the formatted string.
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(100).append(this.className).append(": ");
        return ToStringHelper.JOINER.appendTo(builder, this.fieldString)
        // .append('}')
                .toString();
    }
}
