package com.mcordero.interviews.models.api;

import org.apache.http.HttpResponse;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

abstract class Response {

  protected JsonObject extractJsonBody(final HttpResponse response) throws IOException {
    InputStream targetStream = new BufferedInputStream((response.getEntity().getContent()));
    JsonReader rdr = Json.createReader(targetStream);
    return rdr.readObject();
  }
}
