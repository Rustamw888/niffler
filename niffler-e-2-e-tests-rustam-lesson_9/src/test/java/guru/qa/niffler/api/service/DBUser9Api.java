package guru.qa.niffler.api.service;

import guru.qa.niffler.model.User9Json;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DBUser9Api {

  @GET("/currentUser")
  Call<User9Json> getCurrentUser(@Query("username") String username);

}
