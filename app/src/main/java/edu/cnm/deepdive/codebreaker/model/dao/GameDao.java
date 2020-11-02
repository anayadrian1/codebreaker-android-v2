package edu.cnm.deepdive.codebreaker.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import edu.cnm.deepdive.codebreaker.model.entity.Game;
import io.reactivex.Single;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Dao
public interface GameDao {

  String COMPLETED_GAMES_QUERY =
      "SELECT DISTINCT"
      + " gm.* "
      + "FROM Game AS gm "
      + "INNER JOIN Guess AS gs ON gs.game_id = gm.game_id " // foreign key in one table is a primary key in another
      + "WHERE gs.correct = gm.code_length "
      + "ORDER BY gm.started DESC, gs.submitted DESC";

  //creating table in memory that has all columns from game on the left side, and columns from guess on the right side;
  // left side filled in with Game content, and right side will be null
  String INCOMPLETE_GAMES_QUERY =
      "SELECT"
      + " gm.* " // inner join finds match on both sides, left join says if no match is found on right side then make right side null and keep left in result
      + "FROM Game AS gm "
      + "LEFT JOIN Guess AS gs ON gs.game_id = gm.game_id AND gs.correct = gm.code_length " // only join game to guess if foreign key is pointing to game and correct
      + "WHERE gs.guess_id IS NULL "
      + "ORDER BY gm.started ASC";

  String INCOMPLETE_MATCH_GAMES_QUERY =
      "SELECT"
          + " gm.* "
          + "FROM Game AS gm "
          + "LEFT JOIN Guess AS gs ON gs.game_id = gm.game_id AND gs.correct = gm.code_length "
          + "WHERE gs.guess_id IS NULL AND gm.match_id IS NOT NULL " //incomplete games that are a part of the match
          + "ORDER BY gm.started ASC";

  String INCOMPLETE_GAMES_IN_MATCH_QUERY =
      "SELECT"
          + " gm.* "
          + "FROM Game AS gm "
          + "LEFT JOIN Guess AS gs ON gs.game_id = gm.game_id AND gs.correct = gm.code_length "
          + "WHERE gs.guess_id IS NULL AND gm.match_id = :id "
          + "ORDER BY gm.started ASC";

  @Insert
  Single<Long> insert(Game game); // long is primary key value of what was inserted

  @Insert
  Single<List<Long>> insert(Game...games);

  @Insert
  Single<List<Long>> insert(Collection<Game> games);

  @Delete
  Single<Integer> delete(Game game); // # of records affected by operation

  @Delete
  Single<Integer> delete(Game...games);

  @Delete
  Single<Integer> delete(Collection<Game> games);

  @Query("SELECT * FROM Game WHERE game_id = :id")
  LiveData<Game> select(long id); // returns single game object based in ID

  @Query("SELECT * FROM Game WHERE game_key = :key")
  LiveData<Game> select(UUID key);

  @Query("SELECT * FROM Game WHERE match_id = :id")
  LiveData<List<Game>> selectInMatch(long id);

  @Query(COMPLETED_GAMES_QUERY)
  LiveData<List<Game>> selectComplete();

  @Query(INCOMPLETE_GAMES_QUERY)
  LiveData<List<Game>> selectIncomplete();

  @Query(INCOMPLETE_MATCH_GAMES_QUERY)
  LiveData<List<Game>> selectIncompleteInMatches();

  @Query(INCOMPLETE_GAMES_IN_MATCH_QUERY)
  LiveData<List<Game>> selectIncompleteInMatch(long id);
}
