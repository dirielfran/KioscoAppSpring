package com.eareiza.springAngular.utileria;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PaginationComponent {

    public static final String CONTENT = "content";
    public static final String TOTAL_PAGES = "totalPages";
    public static final String FIRST_PAGE = "firstPage";
    public static final String LAST_PAGE = "lastPage";

    public String changePaginationResponse(Page<?> page){
        Gson gson = new Gson();
        JsonObject json = new JsonObject();

        json.add(CONTENT, gson.toJsonTree(page.getContent()));
        json.addProperty(TOTAL_PAGES, page.getTotalPages());
        json.addProperty(FIRST_PAGE, page.isFirst());
        json.addProperty(LAST_PAGE, page.isLast());

        return gson.toJson(json);
    }
}
