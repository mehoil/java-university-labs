package ru.mehoil.beans.service;

import java.util.List;

/**
 * A regular service managing strings.
 *
 * @author Mikhail Dorokhov
 * @version 1.0
 */
public interface RegularService {

    /**
     * Processes a list of strings.
     *
     * @param strings a list of string
     * @return a processed list of strings
     */
    List<String> processStrings(List<String> strings);

}
