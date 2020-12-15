package com.es.phoneshop.web.entity;

import java.util.List;

public class QuickOrderEntriesForm {
    List<QuickOrderEntry> entries;

    public List<QuickOrderEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<QuickOrderEntry> entries) {
        this.entries = entries;
    }
}
