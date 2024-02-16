package guru.qa.niffler.api.service;

import guru.qa.niffler.model.Category9Json;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Category9Service {

    @POST("/category")
    Call<Category9Json> addCategory(@Body Category9Json category);
}
