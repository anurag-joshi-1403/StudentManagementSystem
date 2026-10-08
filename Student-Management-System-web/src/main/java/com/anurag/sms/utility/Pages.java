package com.anurag.sms.utility;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * One place for how list pages are cut (#11).
 */
public final class Pages {

    public static final int PAGE_SIZE = 5;

    private Pages() {
    }

    /**
     * @param pageNo 1-based, as shown in the pager. Anything below 1 (a typed
     *               URL) is treated as page 1 instead of failing.
     * @return the page request, sorted by id so rows cannot shift between pages:
     *         without an ORDER BY, MySQL may return them in a different order
     *         for each page query
     */
    public static Pageable of(int pageNo) {
        return PageRequest.of(Math.max(pageNo, 1) - 1, PAGE_SIZE, Sort.by("id"));
    }
}
