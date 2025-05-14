package ru.mehoil.beans.cool;

/**
 * Default {@link CoolBean} implementation.
 *
 * @author Mikhail Dorokhov
 * @version 1.0
 */
public class CoolBeanImpl implements CoolBean {

    @Override
    public String awesomeMethod(final String greatString) {
        return greatString.trim();
    }

}
