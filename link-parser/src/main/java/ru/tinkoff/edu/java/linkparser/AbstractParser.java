package ru.tinkoff.edu.java.linkparser;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ru.tinkoff.edu.java.linkparser.dto.ILinkDTO;
import ru.tinkoff.edu.java.linkparser.dto.ResponseContainer;

public abstract non-sealed class AbstractParser implements Parser {

    protected AbstractParser next;

    public static Parser of(AbstractParser... parsers) {
        for (int i = 0; i < parsers.length - 1; i++) {
            parsers[i].setNext(parsers[i + 1]);
        }
        return parsers[0];
    }

    public void setNext(AbstractParser next) {
        this.next = next;
    }

    @Override
    public final ResponseContainer<ILinkDTO> parseChain(URL url) {
        ResponseContainer<ILinkDTO> responseContainer = parseInternal(url);
        if (responseContainer != null) {
            return responseContainer;
        }
        if (next == null) {
            return new ResponseContainer<>(null);
        }
        return next.parseChain(url);
    }

    /**
     * Element of parseChain
     * @return 'null' if url has unsupported host; 'ResponseContainer<>(null)' if uncorrected link, but supported host
     */
    protected abstract ResponseContainer<ILinkDTO> parseInternal(URL url);

    protected List<String> getArrayFromURL(URL url) {
        if (url.getPath().isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(url.getPath().substring(1).split("/"));
    }

}
