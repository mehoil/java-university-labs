package ru.mehoil.beans.service;

import ru.mehoil.beans.AutoInjectable;
import ru.mehoil.beans.cool.CoolBean;

import java.util.List;

/**
 * Default {@link RegularService} implementation.
 *
 * @author Mikhail Dorokhov
 * @version 1.0
 */
public class RegularServiceImpl implements RegularService {

    @AutoInjectable
    private CoolBean coolBean;

    @Override
    public List<String> processStrings(final List<String> strings) {
        return strings.stream()
                .map(coolBean::awesomeMethod)
                .filter(s -> !s.isBlank())
                .toList();
    }
}
