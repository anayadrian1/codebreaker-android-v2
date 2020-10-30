package edu.cnm.deepdive.codebreaker.model.pojo;

import androidx.room.Relation;
import edu.cnm.deepdive.codebreaker.model.entity.Game;
import edu.cnm.deepdive.codebreaker.model.entity.Guess;

public class GuessWithGame extends Guess {

  // I want to populate this field from a join, join the table from Game.class between the parent class, and the one on this side
  // many to one here says its only one value
  // want to accompany it with @Transaction in the Dao
  @Relation(
      entity = Game.class,
      entityColumn = "game_id",
      parentColumn = "game_id"
  )
  private Game game;

  public Game getGame() {
    return game;
  }

  public void setGame(Game game) {
    this.game = game;
  }

  public boolean isSolution() {
    return getCorrect() == game.getCodeLength();
  }
}
