package guru.qa.niffler.api.service;

import guru.qa.niffler.model.Spend9Json;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Spend9Service {

    @POST("/addSpend")
    Call<Spend9Json> addSpend(@Body Spend9Json spend);
}
