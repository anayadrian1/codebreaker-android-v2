package edu.cnm.deepdive.codebreaker.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import java.util.Date;
import java.util.UUID;

@SuppressWarnings("NotNullFieldNotInitialized")
@Entity(
    indices = {
        @Index(value = {"game_id"}, unique = true)
    },
    foreignKeys = {
        @ForeignKey(
            entity = Match.class,
            parentColumns = {"match_id"}, childColumns = {"match_id"},
            onDelete = ForeignKey.CASCADE
        ) // foreign key lives in the child
    }
)
public class Game {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "game_id")
  private long id;

  @ColumnInfo(name = "game_key", typeAffinity = ColumnInfo.BLOB)
  private UUID gameKey;

  @NonNull
  private String pool;

  @NonNull
  private String code;

  @ColumnInfo(name = "match_id", index = true)
  private Long matchId; // many to one with optional, foreign key must be nullable

  @ColumnInfo(name = "code_length", index = true)
  private int codeLength;

  @NonNull
  @ColumnInfo(index = true)
  private Date started = new Date();

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public UUID getGameKey() {
    return gameKey;
  }

  public void setGameKey(UUID gameKey) {
    this.gameKey = gameKey;
  }

  @NonNull
  public String getPool() {
    return pool;
  }

  public void setPool(@NonNull String pool) {
    this.pool = pool;
  }

  @NonNull
  public String getCode() {
    return code;
  }

  public void setCode(@NonNull String code) {
    this.code = code;
  }

  public Long getMatchId() {
    return matchId;
  }

  public void setMatchId(Long matchId) {
    this.matchId = matchId;
  }

  public int getCodeLength() {
    return codeLength;
  }

  public void setCodeLength(int codeLength) {
    this.codeLength = codeLength;
  }

  @NonNull
  public Date getStarted() {
    return started;
  }

  public void setStarted(@NonNull Date started) {
    this.started = started;
  }
}
