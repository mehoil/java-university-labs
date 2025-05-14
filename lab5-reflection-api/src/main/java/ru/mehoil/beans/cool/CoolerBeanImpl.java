package ru.mehoil.beans.cool;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Other {@link CoolBean} implementation.
 *
 * @author Mikhail Dorokhov
 * @version 1.0
 */
public class CoolerBeanImpl implements CoolBean {

    @Override
    public String awesomeMethod(final String greatString) {
        return Arrays.stream(greatString.split(" "))
                .filter(s -> s.equalsIgnoreCase("great"))
                .map(String::toUpperCase)
                .collect(Collectors.joining());
    }

}
