package edu.cnm.deepdive.codebreaker.service;

import android.content.Context;
import edu.cnm.deepdive.codebreaker.model.dao.UserDao;
import edu.cnm.deepdive.codebreaker.model.entity.User;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class UserRepository {

  private final Context context;
  private final UserDao userDao;
  private final CodebreakerWebService webService;
  private final GoogleSignInService signInService;

  public UserRepository(Context context) {
    this.context = context;
    userDao = CodebreakerDatabase.getInstance().getUserDao();
    webService = CodebreakerWebService.getInstance();
    signInService = GoogleSignInService.getInstance();
  }

  public Single<User> getCurrentUser() {
    return Single.fromCallable(signInService::getAccount) // get account from google
        .flatMap((account) -> // map an account by daisy chaining the webservice by invoking the token for the user
            webService.getProfile(getBearerToken(account.getIdToken()))
            .flatMap((user) -> // map to single
                userDao.selectByOauthKey(account.getId())
                    .switchIfEmpty(
                        userDao.insert(user)
                            .map((id) -> {
                              user.setId(id);
                              return user;
                            })
                    ) // if theres not a user then create
            )
            .flatMap((user) ->
                userDao.update(user)
                .map((numRecords) -> user)
            )
        )
    .subscribeOn(Schedulers.io()); // oauthkey from account give us if there is a user

  }

  private String getBearerToken(String idToken) {
    return String.format("Bearer %s", idToken);
  }
}
