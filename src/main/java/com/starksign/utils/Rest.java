package com.starksign.utils;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.*;


public final class Rest {
    public static <T extends Resource> T getId(Resource.ClassData resource, String id, Map<String, Object> query) throws Exception {
        String content = Response.fetch(Api.endpoint(resource, id), "GET", null, query).content();
        Gson gson = GsonEvent.getInstance();
        JsonObject contentJson = gson.fromJson(content, JsonObject.class);
        JsonObject jsonObject = contentJson.get(Api.getLastName(resource)).getAsJsonObject();
        return gson.fromJson(jsonObject, (Type) resource.cls);
    }

    public static <T extends SubResource> T postSubResource(Resource.ClassData resource, String id, SubResource.ClassData subResource, SubResource entity) throws Exception {
        JsonObject payload = (JsonObject) new Gson().toJsonTree((entity));
        String content = Response.fetch(
            Api.endpoint(resource, id) + Api.endpoint(subResource),
            "POST",
            payload,
            new HashMap<>()
        ).content();
        JsonObject contentJson = new Gson().fromJson(content, JsonObject.class);
        JsonObject jsonObject = contentJson.get(Api.getLastName(subResource)).getAsJsonObject();
        return GsonEvent.getInstance().fromJson(jsonObject, (Type) subResource.cls);
    }
}
